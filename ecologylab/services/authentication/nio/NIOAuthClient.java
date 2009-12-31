/*
 * Created on May 12, 2006
 */
package ecologylab.services.authentication.nio;

import java.io.IOException;

import ecologylab.collections.Scope;
import ecologylab.generic.BooleanSlot;
import ecologylab.services.authentication.AuthConstants;
import ecologylab.services.authentication.AuthenticationListEntry;
import ecologylab.services.authentication.AuthenticationTranslations;
import ecologylab.services.authentication.messages.AuthMessages;
import ecologylab.services.authentication.messages.Login;
import ecologylab.services.authentication.messages.Logout;
import ecologylab.services.authentication.registryobjects.AuthClientRegistryObjects;
import ecologylab.services.distributed.client.NIOClient;
import ecologylab.services.distributed.exception.MessageTooLargeException;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.TranslationScope;

/**
 * A client application that uses authentication and communicates using NIO.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public class NIOAuthClient<S extends Scope> extends NIOClient<S> implements
		AuthClientRegistryObjects, AuthConstants, AuthMessages
{
	/** The username / password information supplied by the user. */
	protected AuthenticationListEntry	entry				= null;

	/** Indicates that this is logging in. */
	private boolean										loggingIn		= false;

	/** Indicates that this is logging out. */
	private boolean										loggingOut	= false;

	/**
	 * Creates a new AuthClient object using the given parameters.
	 * 
	 * @param server
	 * @param port
	 * @param messageSpace
	 * @param objectRegistry
	 * @throws IOException
	 */
	public NIOAuthClient(String server, int port, TranslationScope messageSpace, S objectRegistry,
			int interval, RequestMessage messageToSend) throws IOException
	{
		this(server, port, messageSpace, objectRegistry, null, interval, messageToSend);
	}

	public NIOAuthClient(String server, int port, TranslationScope messageSpace, S objectRegistry,
			AuthenticationListEntry entry) throws IOException
	{
		this(server, port, messageSpace, objectRegistry, entry, 0, null);
	}

	/**
	 * Main constructor; creates a new AuthClient using the parameters.
	 * 
	 * @param server
	 * @param port
	 * @param messageSpace
	 * @param objectRegistry
	 * @param entry
	 * @throws IOException
	 */
	public NIOAuthClient(String server, int port, TranslationScope messageSpace, S objectRegistry,
			AuthenticationListEntry entry, int interval, RequestMessage messageToSend) throws IOException
	{
		super(server, port, AuthenticationTranslations.get("AuthClient", messageSpace), objectRegistry);

		objectRegistry.put(LOGIN_STATUS, new BooleanSlot(false));
		objectRegistry.put(LOGIN_STATUS_STRING, null);

		this.entry = entry;
	}

	/**
	 * @param entry
	 *          The entry to set.
	 */
	public void setEntry(AuthenticationListEntry entry)
	{
		this.entry = entry;
	}

	/**
	 * Attempts to connect to the server using the AuthenticationListEntry that is associated with the
	 * client's side of the connection. Does not block for connection.
	 * 
	 * @throws IOException
	 * @throws MessageTooLargeException
	 */
	public boolean login() throws IOException, MessageTooLargeException
	{
		// if we have an entry (username + password), then we can try to connect
		// to the server.
		if (entry != null)
		{
			loggingOut = false;
			loggingIn = true;

			debug("sending login message.");
			
			// Login response will handle changing the LOGIN_STATUS
			sendLoginMessage();
		}
		else
		{
			debug("ENTRY NOT SET!");
		}

		if (!isLoggedIn())
		{ // login failed, clear session id
			this.clearSessionId();
		}

		return isLoggedIn();
	}

	/**
	 * Attempts to log out of the server using the AuthenticationListEntry that is associated with the
	 * client's side of the connection. Blocks until a response is received or until LOGIN_WAIT_TIME
	 * passes, whichever comes first.
	 * 
	 * @throws IOException
	 * @throws MessageTooLargeException
	 */
	protected boolean logout() throws IOException, MessageTooLargeException
	{
		// if we have an entry (username + password), then we can try to logout of
		// the server.
		if (entry != null)
		{
			loggingIn = false;
			loggingOut = true;

			// Login response will handle changing the LOGIN_STATUS
			sendLogoutMessage();
		}

		if (!isLoggedIn())
		{ // clear session id
			this.clearSessionId();
		}

		return isLoggedIn();
	}

	/**
	 * Sends a Logout message to the server; may be overridden by subclasses that need to add
	 * additional information to the Logout message.
	 * 
	 * @throws MessageTooLargeException
	 * 
	 */
	protected ResponseMessage sendLogoutMessage() throws IOException, MessageTooLargeException
	{
		return this.sendMessage(new Logout(entry), 5000);
	}

	/**
	 * Sends a Login message to the server; may be overridden by subclasses that need to add
	 * additional information to the Login message.
	 * 
	 * @throws MessageTooLargeException
	 * 
	 */
	protected ResponseMessage sendLoginMessage() throws IOException, MessageTooLargeException
	{
		ResponseMessage temp = this.sendMessage(new Login(entry), 5000);

		return temp;
	}

	/**
	 * @return Returns the loggingIn.
	 */
	public boolean isLoggingIn()
	{
		return loggingIn;
	}

	/**
	 * @return Returns the loggingOut.
	 */
	public boolean isLoggingOut()
	{
		return loggingOut;
	}

	/**
	 * @return The response message from the server regarding the last attempt to log in; if login
	 *         fails, will indicate why.
	 */
	public String getExplanation()
	{
		String temp = (String) objectRegistry.get(LOGIN_STATUS_STRING);

		if (temp == null)
		{
			return "";
		}

		return temp;
	}

	/**
	 * @return Returns whether or not this client is logged in to a server.
	 */
	public boolean isLoggedIn()
	{
		if (this.connected())
			return ((BooleanSlot) objectRegistry.get(LOGIN_STATUS)).value;

		return false;
	}

	/**
	 * @see ecologylab.services.distributed.client.NIOClient#handleDisconnectingMessages()
	 */
	@Override
	protected void handleDisconnectingMessages()
	{
		try
		{
			this.logout();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (MessageTooLargeException e)
		{
			e.printStackTrace();
		}
	}
}
