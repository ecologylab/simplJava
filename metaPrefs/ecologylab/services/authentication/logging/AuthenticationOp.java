package ecologylab.services.authentication.logging;

import ecologylab.services.authentication.messages.AuthMessages;
import ecologylab.services.logging.MixedInitiativeOp;


public class AuthenticationOp extends MixedInitiativeOp implements AuthMessages
{
    static final String LOGIN = "login";
    static final String LOGOUT = "logout";
    
    public String  username;

    public long    currentTimeMillis;
    
    public String action;
    
    public String response;
    
    public String ipAddress;
    
    public int port;

    public AuthenticationOp()
    {
        super();
    }

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

    public void performAction(boolean invert)
    {
    }

}
