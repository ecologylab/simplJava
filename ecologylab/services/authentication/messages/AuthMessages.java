package ecologylab.services.authentication.messages;

/**
 * Interface that contains the list of constant Strings used for Authentication
 * messages. Implement this interface if your class uses Login or Logout.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public interface AuthMessages
{
    /**
     * Indicates that a LOGIN was successful.
     */
    public static final String LOGIN_SUCCESSFUL = "Successfully logged in.";

    /**
     * Indicates that login failed because either the username was not found, or
     * because the password did not match a username.
     */
    public static final String LOGIN_FAILED_PASSWORD = "Cannot log in: username/password combination not found.";

    /**
     * Indicates that login failed because the username is already logged in.
     */
    public static final String LOGIN_FAILED_LOGGEDIN = "Cannot log in: username already logged-in.";

    /**
     * Indicates that LOGOUT failed because the given username was never logged
     * in in the first place!
     */
    public static final String LOGOUT_FAILED_NOT_LOGGEDIN = "Cannot log out: username was not logged-in.";

    /**
     * Indicates that a request failed because no one has logged in yet.
     */
    public static final String REQUEST_FAILED_NOT_AUTHENTICATED = "Cannot process request, connection not yet authenticated.";
}
