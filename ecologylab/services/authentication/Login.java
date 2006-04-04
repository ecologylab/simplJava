/*
 * Created on Mar 30, 2006
 */
package ecologylab.services.authentication;

import java.util.HashMap;

import ecologylab.generic.ObjectRegistry;
import ecologylab.services.messages.ErrorResponse;
import ecologylab.services.messages.OKResponse;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;

/**
 * Used to log into a server that requires authentication; carries username and password information in strings, and checks them against "authenticationList" in the objectRegistry.
 * @author Zach Toups (toupsz@gmail.com)
 */
public class Login extends RequestMessage implements AuthenticationMessages 
{

    public AuthenticationListEntry entry = new AuthenticationListEntry("", "");
    
    public Login() 
    {
        super();
    }
    
    public Login(AuthenticationListEntry entry) 
    {
        super();
        this.entry = entry;
    }
    
    public Login(String username, String password) 
    {
        this(new AuthenticationListEntry(username, password));
    }

    /**
     * Determines if the supplied username and password are contained in the list of usernames and passwords in the object registry.
     * @return A ResponseMessage indicating whether or not the username/password were accepted.
     */
    public ResponseMessage performService(ObjectRegistry objectRegistry) 
    {
        AuthenticationList authList = (AuthenticationList) objectRegistry.lookupObject("authenticationList");
        HashMap authedClients = (HashMap) objectRegistry.lookupObject("authenticatedClients");
        
        ResponseMessage loginConfirm = new ErrorResponse(LOGIN_FAILED_PASSWORD); // set to the default failure
        
        if (authList != null) 
        {
            // make sure the username is in the list
            if (authList.containsKey(entry.getUsername())) 
            {
//                System.err.println("username found!");
                
                // if it is, then compare the passwords                
                if (((AuthenticationListEntry)(authList.get(entry.getUsername()))).compareEncryptedPassword(entry.getPassword())) 
                {
                    
                    debug("password match!");
                    
                    // now make sure that the user isn't already logged-in
                    if (authedClients.containsKey(entry.getUsername())) 
                    {
                        loginConfirm = new ErrorResponse(LOGIN_FAILED_LOGGEDIN);
                    } else 
                    {
                        // we want to let the client know that it's logged in...
                    	// TODO not sure if this is right; we might want to be more specific about what we're saying OK to...
                        loginConfirm = OKResponse.get();
                    }
                }
            }
        }
        
        return loginConfirm;
    }

    /**
     * @return Returns the entry.
     */
    public AuthenticationListEntry getEntry() 
    {
        return entry;
    }

    /**
     * @param entry The entry to set.
     */
    public void setEntry(AuthenticationListEntry entry) 
    {
        this.entry = entry;
    }
}
