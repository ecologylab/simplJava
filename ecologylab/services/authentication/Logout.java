/*
 * Created on Mar 30, 2006
 */
package ecologylab.services.authentication;

import java.util.HashMap;

import ecologylab.generic.ObjectRegistry;
import ecologylab.services.messages.ErrorResponse;
import ecologylab.services.messages.OkResponse;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;

public class Logout extends RequestMessage implements AuthenticationMessages {

    public AuthenticationListEntry entry = new AuthenticationListEntry("", "");
    
    /** 
     * Should not normally be used; only for XML translations.
     */
    public Logout() { super(); }
    
    public Logout(AuthenticationListEntry entry)
    {
        super();
        this.entry = entry;
    }

    /**
     * Attempts to log the user specified by entry from the system; if they are already logged in; if not, sends a failure response.
     */
    public ResponseMessage performService(ObjectRegistry objectRegistry)
    {
    	    HashMap authedClients = (HashMap) objectRegistry.lookupObject("authenticatedClients");
        ResponseMessage responseMessage;
        
        if ((authedClients != null) && authedClients.containsKey(entry.getUsername()))
        {
    		responseMessage	= OkResponse.get();
            
            authedClients.remove(entry.getUsername());
        }
        else
        	responseMessage	=  new ErrorResponse(LOGOUT_FAILED_NOT_LOGGEDIN); 
        
        return responseMessage;
    }

}
