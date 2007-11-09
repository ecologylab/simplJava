package ecologylab.services.authentication.logging;

import ecologylab.services.authentication.messages.AuthMessages;
import ecologylab.services.logging.MixedInitiativeOp;

/**
 * Logging operation that indicates when a user logs in or out of the server.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public class AuthenticationOp extends MixedInitiativeOp implements AuthMessages
{
	/** Login action. */
	static final String				LOGIN		= "login";

	/** Logout action. */
	static final String				LOGOUT	= "logout";

	@xml_attribute private String	username;

	@xml_attribute private long	currentTimeMillis;

	@xml_attribute private String	action;

	@xml_attribute private String	response;

	@xml_attribute private String	ipAddress;

	@xml_attribute private int		port;

	public AuthenticationOp()
	{
		super();
	}

	/**
	 * Creates a new AuthenticationOp to indicate that a user logged either in or out of the server.
	 * 
	 * @param username
	 *           the username of the user.
	 * @param loggingIn
	 *           true if the user is logging in; false for logging out.
	 * @param response
	 *           the response the server gave to the attempt.
	 * @param ipAddress
	 *           the IP address from which the attempt to log in or out originated.
	 * @param port
	 *           the port on which the attempt to log in or out was made.
	 */
	public AuthenticationOp(String username, boolean loggingIn, String response, String ipAddress, int port)
	{
		this.username = username;

		if (loggingIn)
		{
			action = LOGIN;
		}
		else
		{
			action = LOGOUT;
		}

		this.response = response;

		this.ipAddress = ipAddress;

		this.port = port;

		this.currentTimeMillis = System.currentTimeMillis();
	}

	/**
	 * @return the username
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * @return the action
	 */
	public String getAction()
	{
		return action;
	}

	/**
	 * @return the response
	 */
	public String getResponse()
	{
		return response;
	}

	/**
	 * @return the ipAddress
	 */
	public String getIpAddress()
	{
		return ipAddress;
	}

	/**
	 * @return the port
	 */
	public int getPort()
	{
		return port;
	}

	/**
	 * @see ecologylab.services.logging.MixedInitiativeOp#performAction(boolean)
	 */
	@Override public void performAction(boolean invert)
	{
	}
}
