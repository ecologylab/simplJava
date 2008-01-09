/**
 * 
 */
package ecologylab.services.distributed.server;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.services.authentication.Authenticatable;
import ecologylab.services.authentication.AuthenticationList;
import ecologylab.services.authentication.AuthenticationListEntry;
import ecologylab.services.authentication.AuthenticationTranslations;
import ecologylab.services.authentication.Authenticator;
import ecologylab.services.authentication.listener.AuthenticationListener;
import ecologylab.services.authentication.logging.AuthLogging;
import ecologylab.services.authentication.logging.AuthenticationOp;
import ecologylab.services.authentication.messages.AuthMessages;
import ecologylab.services.authentication.nio.AuthContextManager;
import ecologylab.services.authentication.registryobjects.AuthServerRegistryObjects;
import ecologylab.services.distributed.impl.NIOServerBackend;
import ecologylab.services.distributed.server.contextmanager.AbstractContextManager;
import ecologylab.services.logging.Logging;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XMLTranslationException;

/**
 * An authenticating server that uses NIO and two threads (one for handling IO, the other for handling interfacing with
 * messages).
 * 
 * Any clients attempting to communicate with this server must either first provide a Login request, or otherwise have
 * previously been logged in; otherwise, no requests are processed from the client.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public class DoubleThreadedAuthNIOServer<A extends AuthenticationListEntry> extends DoubleThreadedNIOServer implements
		AuthServerRegistryObjects, AuthMessages, AuthLogging, Authenticatable<A>
{
	/** Optional Logging listeners may record authentication events, such as users logging-in. */
	private List<Logging>						logListeners	= new LinkedList<Logging>();

	private List<AuthenticationListener>	authListeners	= new LinkedList<AuthenticationListener>();

	protected Authenticator<A>					authenticator	= null;

	/**
	 * This is the actual way to create an instance of this.
	 * 
	 * @param portNumber
	 * @param translationSpace
	 * @param objectRegistry
	 * @param authListFilename -
	 *           a file name indicating the location of the authentication list; this should be an XML file of an
	 *           AuthenticationList object.
	 * @return A server instance, or null if it was not possible to open a ServerSocket on the port on this machine.
	 */
	public static DoubleThreadedAuthNIOServer getInstance(int portNumber, InetAddress[] inetAddress,
			TranslationSpace requestTranslationSpace, ObjectRegistry objectRegistry, int idleConnectionTimeout,
			int maxPacketSize, String authListFilename)
	{
		DoubleThreadedAuthNIOServer newServer = null;

		try
		{
			newServer = new DoubleThreadedAuthNIOServer(portNumber, inetAddress, requestTranslationSpace, objectRegistry,
					idleConnectionTimeout, maxPacketSize, (AuthenticationList) ElementState.translateFromXML(
							authListFilename, AuthenticationTranslations.get()));
		}
		catch (IOException e)
		{
			println("ServicesServer ERROR: can't open ServerSocket on port " + portNumber);
			e.printStackTrace();
		}
		catch (XMLTranslationException e)
		{
			e.printStackTrace();
		}

		return newServer;
	}

	/**
	 * This is the actual way to create an instance of this.
	 * 
	 * @param portNumber
	 * @param translationSpace
	 * @param objectRegistry
	 * @param authList -
	 *           the AuthorizationList object to be used to determine possible users.
	 * @return A server instance, or null if it was not possible to open a ServerSocket on the port on this machine.
	 */
	public static DoubleThreadedAuthNIOServer getInstance(int portNumber, InetAddress[] inetAddress,
			TranslationSpace requestTranslationSpace, ObjectRegistry objectRegistry, int idleConnectionTimeout,
			int maxPacketSize, AuthenticationList authList)
	{
		DoubleThreadedAuthNIOServer newServer = null;

		try
		{
			newServer = new DoubleThreadedAuthNIOServer(portNumber, inetAddress, requestTranslationSpace, objectRegistry,
					idleConnectionTimeout, maxPacketSize, authList);
		}
		catch (IOException e)
		{
			println("ServicesServer ERROR: can't open ServerSocket on port " + portNumber);
			e.printStackTrace();
		}

		return newServer;
	}

	/**
	 * @param portNumber
	 * @param inetAddress
	 * @param translationSpace
	 * @param objectRegistry
	 * @throws IOException
	 * @throws BindException
	 */
	protected DoubleThreadedAuthNIOServer(int portNumber, InetAddress[] inetAddress,
			TranslationSpace requestTranslationSpace, ObjectRegistry objectRegistry, int idleConnectionTimeout,
			int maxPacketSize, AuthenticationList authList) throws IOException, BindException
	{
		// super(portNumber, inetAddress, translationSpace, objectRegistry, idleConnectionTimeout, maxPacketSize);
		// MODEL: from Andruid to Zach
		super(portNumber, inetAddress, AuthenticationTranslations.get("double_threaded_auth " + inetAddress[0].toString()
				+ ":" + portNumber, requestTranslationSpace), objectRegistry, idleConnectionTimeout, maxPacketSize);

		this.registry.registerObject(MAIN_AUTHENTICATABLE, this);

		authenticator = new Authenticator(authList);
	}

	/**
	 * 
	 * @param token
	 * @param translationSpace
	 * @param registry
	 * @return
	 */
	@Override protected AbstractContextManager generateContextManager(Object token, SocketChannel sc,
			TranslationSpace translationSpace, ObjectRegistry registry)
	{
		try
		{
			return new AuthContextManager(token, maxPacketSize, getBackend(), this, sc, translationSpace, registry, this);
		}
		catch (ClassCastException e)
		{
			debug("ATTEMPT TO USE AuthMessageProcessor WITH A NON-AUTHENTICATING SERVER!");
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * @see ecologylab.services.authentication.logging.AuthLogging#addLoggingListener(ecologylab.services.logging.Logging)
	 */
	public void addLoggingListener(Logging log)
	{
		logListeners.add(log);
	}

	public void addAuthenticationListener(AuthenticationListener authListener)
	{
		authListeners.add(authListener);
	}
	
	protected void fireLogoutEvent(String username, InetAddress addr)
	{
		for (AuthenticationListener a : authListeners)
		{
			a.userLoggedOut(username, addr);
		}
	}

	protected void fireLoginEvent(String username, InetAddress addr)
	{
		for (AuthenticationListener a : authListeners)
		{
			a.userLoggedIn(username, addr);
		}
	}
 
	public void fireLoggingEvent(AuthenticationOp op)
	{
		for (Logging logListener : logListeners)
		{
			logListener.logAction(op);
		}
	}

	public boolean logout(A entry, InetAddress address)
	{
		boolean logoutSuccess = authenticator.logout(entry, address);
		
		if (logoutSuccess)
		{
			fireLogoutEvent(entry.getUsername(), address);
		}
		
		return logoutSuccess;
	}

	public boolean isLoggedIn(String username)
	{
		return authenticator.isLoggedIn(username);
	}

	public boolean login(A entry, InetAddress address)
	{
		boolean loginSuccess = authenticator.login(entry, address);
		
		if (loginSuccess)
		{
			fireLoginEvent(entry.getUsername(), address);
		}
		
		return loginSuccess;
	}

	private void remove(InetAddress address)
	{
		authenticator.remove(address);
	}

	/**
	 * Ensure that the user associated with sc has been logged out of the authenticator, then call super.invalidate().
	 * 
	 * @see ecologylab.services.distributed.server.DoubleThreadedNIOServer#invalidate(java.lang.Object,
	 *      ecologylab.services.distributed.impl.NIOServerBackend, java.nio.channels.SocketChannel)
	 */
	@Override public AbstractContextManager invalidate(Object token, NIOServerBackend base, SocketChannel sc,
			boolean permanent)
	{
		if (permanent)
		{
			InetAddress addr = sc.socket().getInetAddress();

			this.remove(addr);
		}
		return super.invalidate(token, base, sc, permanent);
	}
}
