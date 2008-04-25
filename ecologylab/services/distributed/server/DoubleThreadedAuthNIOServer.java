/**
 * 
 */
package ecologylab.services.distributed.server;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.nio.channels.SelectionKey;
import java.util.LinkedList;
import java.util.List;

import ecologylab.collections.Scope;
import ecologylab.services.authentication.Authenticatable;
import ecologylab.services.authentication.AuthenticationList;
import ecologylab.services.authentication.AuthenticationListEntry;
import ecologylab.services.authentication.AuthenticationTranslations;
import ecologylab.services.authentication.Authenticator;
import ecologylab.services.authentication.listener.AuthenticationListener;
import ecologylab.services.authentication.logging.AuthLogging;
import ecologylab.services.authentication.logging.AuthenticationOp;
import ecologylab.services.authentication.messages.AuthMessages;
import ecologylab.services.authentication.nio.AuthClientSessionManager;
import ecologylab.services.authentication.registryobjects.AuthServerRegistryObjects;
import ecologylab.services.distributed.server.clientsessionmanager.AbstractClientSessionManager;
import ecologylab.services.logging.Logging;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationScope;
import ecologylab.xml.XMLTranslationException;

/**
 * An authenticating server that uses NIO and two threads (one for handling IO,
 * the other for handling interfacing with messages).
 * 
 * Any clients attempting to communicate with this server must either first
 * provide a Login request, or otherwise have previously been logged in;
 * otherwise, no requests are processed from the client.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public class DoubleThreadedAuthNIOServer<A extends AuthenticationListEntry>
		extends DoubleThreadedNIOServer implements AuthServerRegistryObjects,
		AuthMessages, AuthLogging, Authenticatable<A>
{
	/**
	 * Optional Logging listeners may record authentication events, such as users
	 * logging-in.
	 */
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
	 *           a file name indicating the location of the authentication list;
	 *           this should be an XML file of an AuthenticationList object.
	 * @return A server instance, or null if it was not possible to open a
	 *         ServerSocket on the port on this machine.
	 */
	public static DoubleThreadedAuthNIOServer getInstance(int portNumber,
			InetAddress[] inetAddress, TranslationScope requestTranslationSpace,
			Scope objectRegistry, int idleConnectionTimeout, int maxPacketSize,
			String authListFilename)
	{
		DoubleThreadedAuthNIOServer newServer = null;

		try
		{
			newServer = new DoubleThreadedAuthNIOServer(portNumber, inetAddress,
					requestTranslationSpace, objectRegistry, idleConnectionTimeout,
					maxPacketSize, (AuthenticationList) ElementState
							.translateFromXML(authListFilename,
									AuthenticationTranslations.get()));
		}
		catch (IOException e)
		{
			println("ServicesServer ERROR: can't open ServerSocket on port "
					+ portNumber);
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
	 *           the AuthorizationList object to be used to determine possible
	 *           users.
	 * @return A server instance, or null if it was not possible to open a
	 *         ServerSocket on the port on this machine.
	 */
	public static DoubleThreadedAuthNIOServer getInstance(int portNumber,
			InetAddress[] inetAddress, TranslationScope requestTranslationSpace,
			Scope objectRegistry, int idleConnectionTimeout, int maxPacketSize,
			AuthenticationList authList)
	{
		DoubleThreadedAuthNIOServer newServer = null;

		try
		{
			newServer = new DoubleThreadedAuthNIOServer(portNumber, inetAddress,
					requestTranslationSpace, objectRegistry, idleConnectionTimeout,
					maxPacketSize, authList);
		}
		catch (IOException e)
		{
			println("ServicesServer ERROR: can't open ServerSocket on port "
					+ portNumber);
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
	protected DoubleThreadedAuthNIOServer(int portNumber,
			InetAddress[] inetAddress, TranslationScope requestTranslationSpace,
			Scope objectRegistry, int idleConnectionTimeout, int maxPacketSize,
			AuthenticationList authList) throws IOException, BindException
	{
		// MODEL for translation space
		super(portNumber, inetAddress, AuthenticationTranslations.get(
				"double_threaded_auth " + inetAddress[0].toString() + ":"
						+ portNumber, requestTranslationSpace), objectRegistry,
				idleConnectionTimeout, maxPacketSize);

		this.applicationObjectScope.put(MAIN_AUTHENTICATABLE, this);

		authenticator = new Authenticator(authList);
	}

	/**
	 * 
	 * @param sessionId
	 * @param translationSpace
	 * @param registry
	 * @return
	 */
	@Override protected AbstractClientSessionManager generateContextManager(
			Object sessionId, SelectionKey sk, TranslationScope translationSpace,
			Scope registry)
	{
		try
		{
			return new AuthClientSessionManager(sessionId, maxMessageSize, getBackend(),
					this, sk, translationSpace, registry, this, authenticator);
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

	protected void fireLogoutEvent(String username, String sessionId)
	{
		for (AuthenticationListener a : authListeners)
		{
			a.userLoggedOut(username, sessionId);
		}
	}

	protected void fireLoginEvent(String username, String sessionId)
	{
		for (AuthenticationListener a : authListeners)
		{
			a.userLoggedIn(username, sessionId);
		}
	}

	public void fireLoggingEvent(AuthenticationOp op)
	{
		for (Logging logListener : logListeners)
		{
			logListener.logAction(op);
		}
	}

	/**
	 * Force logout of an entry; do not require the session id.
	 * @param entry
	 * @return
	 */
	protected boolean logout(A entry)
	{
		Object sessionId = authenticator.getSessionId(entry);
		
		return this.logout(entry, (String) sessionId);
	}
	
	public boolean logout(A entry, String sessionId)
	{
		boolean logoutSuccess = authenticator.logout(entry, sessionId);

		if (logoutSuccess)
		{
			debug(entry.getUsername() + " has been logged out.");
			fireLogoutEvent(entry.getUsername(), sessionId);
		}

		return logoutSuccess;
	}

	public boolean isLoggedIn(String username)
	{
		return authenticator.isLoggedIn(username);
	}

	public boolean login(A entry, String sessionId)
	{
		boolean loginSuccess = authenticator.login(entry, sessionId);

		if (loginSuccess)
		{
			fireLoginEvent(entry.getUsername(), sessionId);
		}

		return loginSuccess;
	}

	private void remove(String sessionId)
	{
		authenticator.removeBySessionId(sessionId);
	}

	/**
	 * Ensure that the user associated with sc has been logged out of the
	 * authenticator, then call super.invalidate().
	 * 
	 * @see ecologylab.services.distributed.server.DoubleThreadedNIOServer#invalidate(java.lang.Object,
	 *      ecologylab.services.distributed.impl.NIOServerIOThread,
	 *      java.nio.channels.SocketChannel)
	 */
	@Override public boolean invalidate(Object sessionId, boolean forcePermanent)
	{
		boolean retVal = super.invalidate(sessionId, forcePermanent);

		if (retVal)
		{
			this.remove((String) sessionId);
		}

		return retVal;
	}
}
