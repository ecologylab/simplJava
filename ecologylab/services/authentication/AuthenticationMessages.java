package ecologylab.services.authentication;

public interface AuthenticationMessages 
{
	public static String OK 	= "ok";
	public static String BADTransmission 	= "bad";
    
    public static String LOGIN_SUCCESSFUL = "Successfully logged in.";
    
    public static String LOGIN_FAILED_PASSWORD = "Cannot log in: username/password combination not found.";
    public static String LOGIN_FAILED_LOGGEDIN = "Cannot log in: username already logged-in.";
    public static String LOGOUT_FAILED_NOT_LOGGEDIN = "Cannot log out: username was not logged-in.";

    public static String REQUEST_FAILED_NOT_AUTHENTICATED = "Cannot process request, connection not yet authenticated.";
}
