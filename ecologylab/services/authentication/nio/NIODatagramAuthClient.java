package ecologylab.services.authentication.nio;

import java.io.IOException;
import java.net.InetSocketAddress;

import ecologylab.collections.Scope;
import ecologylab.generic.BooleanSlot;
import ecologylab.services.authentication.AuthConstants;
import ecologylab.services.authentication.User;
import ecologylab.services.authentication.messages.AuthMessages;
import ecologylab.services.authentication.messages.Login;
import ecologylab.services.authentication.messages.Logout;
import ecologylab.services.authentication.registryobjects.AuthClientRegistryObjects;
import ecologylab.services.distributed.client.NIODatagramClient;
import ecologylab.services.distributed.exception.MessageTooLargeException;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.TranslationScope;

public class NIODatagramAuthClient<S extends Scope> extends NIODatagramClient<S> implements
AuthClientRegistryObjects, AuthConstants, AuthMessages
{

	/** The username / password information supplied by the user. */
	protected User	entry			= null;

	/** Indicates that this is logging in. */
	private boolean							loggingIn	= false;

	/** Indicates that this is logging out. */
	private boolean							loggingOut	= false;

	
	public NIODatagramAuthClient(InetSocketAddress serverAddress,
			InetSocketAddress localAddress, TranslationScope translationScope,
			S objectRegistry, User entry, boolean useCompression, int timeout)
	{
		super(serverAddress, localAddress, translationScope, objectRegistry, useCompression, timeout);
		
		objectRegistry.put(LOGIN_STATUS, new BooleanSlot(false));
		objectRegistry.put(LOGIN_STATUS_STRING, null);
		
		this.entry = entry;
	}

	public NIODatagramAuthClient(InetSocketAddress serverAddress,
										  TranslationScope translationScope, S objectRegistry,
										  User entry, boolean useCompression, int timeout)
	{
		super(serverAddress, translationScope, objectRegistry, useCompression, timeout);
		
		objectRegistry.put(LOGIN_STATUS, new BooleanSlot(false));
		objectRegistry.put(LOGIN_STATUS_STRING, null);
		
		this.entry = entry;
	}
	
	public NIODatagramAuthClient(InetSocketAddress serverAddress,
			  TranslationScope translationScope, S objectRegistry, boolean useCompression,
			  int timeout)
	{
		this(serverAddress, translationScope, objectRegistry, null, useCompression, timeout);
	}
	
	public NIODatagramAuthClient(InetSocketAddress serverAddress,
			InetSocketAddress localAddress, TranslationScope translationScope,
			S objectRegistry, boolean useCompression, int timeout)
	{
		this(serverAddress, localAddress, translationScope, objectRegistry, null, useCompression, timeout);
	}
	/**
	 * @param entry
	 *           The entry to set.
	 */
	public void setEntry(User entry)
	{
		this.entry = entry;
	}

	/**
	 * Attempts to connect to the server using the AuthenticationListEntry that
	 * is associated with the client's side of the connection. Does not block for
	 * connection.
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

			// Login response will handle changing the LOGIN_STATUS
			sendLoginMessage();				
		}
		else
		{
			debug("ENTRY NOT SET!");
		}

		return isLoggedIn();
	}

	/**
	 * Attempts to log out of the server using the AuthenticationListEntry that
	 * is associated with the client's side of the connection. Blocks until a
	 * response is received or until LOGIN_WAIT_TIME passes, whichever comes
	 * first.
	 * 
	 * @throws IOException
	 * @throws MessageTooLargeException
	 */
	protected boolean logout()
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

		return isLoggedIn();
	}

	/**
	 * Sends a Logout message to the server; may be overridden by subclasses that
	 * need to add addtional information to the Logout message.
	 * 
	 * @throws MessageTooLargeException
	 * 
	 */
	protected ResponseMessage sendLogoutMessage() 
	{
		return this.sendMessage(new Logout(entry));
	}

	/**
	 * Sends a Login message to the server; may be overridden by subclasses that
	 * need to add addtional information to the Login message.
	 * 
	 * @throws MessageTooLargeException
	 * 
	 */
	protected ResponseMessage sendLoginMessage() throws IOException,
			MessageTooLargeException
	{
		ResponseMessage temp = this.sendMessage(new Login(entry));

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
	 * @return The response message from the server regarding the last attempt to
	 *         log in; if login fails, will indicate why.
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
		return ((BooleanSlot) objectRegistry.get(LOGIN_STATUS)).value;
	}

}
