package ecologylab.services.distributed.server;

import java.util.LinkedList;
import java.util.List;

import ecologylab.collections.Scope;
import ecologylab.services.authentication.Authenticatable;
import ecologylab.services.authentication.AuthenticationList;
import ecologylab.services.authentication.AuthenticationListEntry;
import ecologylab.services.authentication.AuthenticationTranslations;
import ecologylab.services.authentication.OnlineAuthenticatorHashMapImpl;
import ecologylab.services.authentication.listener.AuthenticationListener;
import ecologylab.services.authentication.logging.AuthLogging;
import ecologylab.services.authentication.logging.AuthenticationOp;
import ecologylab.services.authentication.messages.AuthMessages;
import ecologylab.services.authentication.registryobjects.AuthServerRegistryObjects;
import ecologylab.services.logging.Logging;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationScope;
import ecologylab.xml.XMLTranslationException;

public class NIODatagramAuthServer<A extends AuthenticationListEntry, S extends Scope> extends
		NIODatagramServer<S> implements AuthServerRegistryObjects, AuthMessages, AuthLogging,
		Authenticatable<A>
{

	/**
	 * Optional Logging listeners may record authentication events, such as users logging-in.
	 */
	private List<Logging>												logListeners	= new LinkedList<Logging>();

	private List<AuthenticationListener>				authListeners	= new LinkedList<AuthenticationListener>();

	protected OnlineAuthenticatorHashMapImpl<A>	authenticator	= null;

	public static NIODatagramAuthServer getInstance(int portNumber,
			TranslationScope translationScope, Scope objectRegistry, String authListFileName,
			boolean useCompression)
	{
		NIODatagramAuthServer server = null;

		try
		{
			server = new NIODatagramAuthServer(portNumber, translationScope, objectRegistry,
					(AuthenticationList) ElementState.translateFromXML(authListFileName,
							AuthenticationTranslations.get()), useCompression);
		}
		catch (XMLTranslationException e)
		{
			e.printStackTrace();
		}

		return server;
	}

	public static NIODatagramAuthServer getInstance(int portNumber,
			TranslationScope translationScope, Scope objectRegistry, String authListFileName)
	{
		return getInstance(portNumber, translationScope, objectRegistry, authListFileName, false);
	}

	public static NIODatagramAuthServer getInstance(int portNumber,
			TranslationScope translationScope, Scope objectRegistry, AuthenticationList authList,
			boolean useCompression)
	{
		NIODatagramAuthServer server = null;

		server = new NIODatagramAuthServer(portNumber, translationScope, objectRegistry, authList,
				useCompression);
		return server;
	}

	public static NIODatagramAuthServer getInstance(int portNumber,
			TranslationScope translationScope, Scope objectRegistry, AuthenticationList authList)
	{
		return getInstance(portNumber, translationScope, objectRegistry, authList, false);
	}

	protected NIODatagramAuthServer(int portNumber, TranslationScope translationScope,
			S objectRegistry, AuthenticationList<A> authList, boolean useCompression)
	{
		super(portNumber, translationScope, objectRegistry, useCompression);

		authenticator = new OnlineAuthenticatorHashMapImpl<A>(authList);
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
	 * 
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

	public boolean isLoggedIn(A entry)
	{
		return authenticator.isLoggedIn(entry);
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

	/**
	 * XXX unimplemented
	 * 
	 * @see ecologylab.services.authentication.Authenticatable#addNewUser(ecologylab.services.authentication.AuthenticationListEntry)
	 */
	@Override
	public boolean addNewUser(A entry)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * XXX unimplemented
	 * 
	 * @see ecologylab.services.authentication.Authenticatable#removeExistingUser(ecologylab.services.authentication.AuthenticationListEntry)
	 */
	@Override
	public boolean removeExistingUser(A entry)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
