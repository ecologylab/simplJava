/*
 * Created on Mar 30, 2006
 */
package ecologylab.services.messages;

import java.util.HashSet;

import ecologylab.generic.ObjectRegistry;
import ecologylab.services.AuthenticationListEntry;
import ecologylab.services.messages.RequestMessage;

public class Logout extends RequestMessage implements ResponseTypes {

    public AuthenticationListEntry entry = new AuthenticationListEntry("", "");
    
    /** 
     * Should not normally be used; only for XML translations.
     *
     */
    public Logout() {
        super();
    }
    
    public Logout(AuthenticationListEntry entry) {
        super();
        
        this.entry = entry;
    }

    /**
     * Attempts to log the user specified by entry from the system; if they are already logged in; if not, sends a failure response.
     */
    public ResponseMessage performService(ObjectRegistry objectRegistry) {
        HashSet authedClients = (HashSet) objectRegistry.lookupObject("authenticatedClients");
        ResponseMessage logoutConfirm = new ResponseMessage(LOGOUT_FAILED_NOT_LOGGEDIN); // set to the default failure
        
        if (authedClients != null) {
            if (authedClients.contains(entry.getUsername())) {
                logoutConfirm.setResponse(OK);
                
                authedClients.remove(entry.getUsername());
            }
        }
        
        return logoutConfirm;
    }

}
