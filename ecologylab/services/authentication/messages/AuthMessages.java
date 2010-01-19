package ecologylab.services.authentication.messages;

/**
 * Interface that contains the list of constant Strings used for Authentication messages. Implement
 * this interface if your class uses Login or Logout.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public interface AuthMessages
{
	/** Indicates that a LOGIN was successful. */
	public static final String	LOGIN_SUCCESSFUL									= "Successfully logged in.";

	/**
	 * Indicates that login failed because either the username was not found, or because the password
	 * did not match a username.
	 */
	public static final String	LOGIN_FAILED_PASSWORD							= "Cannot log in: username/password combination not found.";

	/** Indicates that login failed because the username is already logged in. */
	public static final String	LOGIN_FAILED_LOGGEDIN							= "Cannot log in: username already logged-in.";

	/**
	 * Indicates that the login failed because the server did not attach an IP address to the request.
	 */
	public static final String	LOGIN_FAILED_NO_IP_SUPPLIED				= "Cannot log in: server unable to determine IP address.";

	/** Indicates that creating a user failed, because the user already exists. */
	public static final String	CREATE_USER_FAILED_ALREADY_EXISTS	= "Could not create new user; user already exists.";

	/** Indicates that a LOGOUT was successful. */
	public static final String	LOGOUT_SUCCESSFUL									= "Successfully logged out.";

	/** Indicates that the LOGOUT was unsuccessful because the message came from the wrong IP. */
	public static final String	LOGOUT_FAILED_IP_MISMATCH					= "Cannot log out: username and IP do not match.";

	/**
	 * Indicates that LOGOUT failed because the given username was never logged in in the first place!
	 */
	public static final String	LOGOUT_FAILED_NOT_LOGGEDIN				= "Cannot log out: username was not logged-in.";

	/** Indicates that a request failed because no one has logged in yet. */
	public static final String	REQUEST_FAILED_NOT_AUTHENTICATED	= "Cannot process request, connection not yet authenticated.";
}
