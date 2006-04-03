/*
 * Created on Mar 30, 2006
 */
package ecologylab.services.authentication;

import java.util.HashSet;

import ecologylab.generic.ObjectRegistry;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;

/**
 * Used to log into a server that requires authentication; carries username and password information in strings, and checks them against "authenticationList" in the objectRegistry.
 * @author Zach Toups (toupsz@gmail.com)
 */
public class Login extends RequestMessage implements AuthenticationMessages {

    public AuthenticationListEntry entry = new AuthenticationListEntry("", "");
    
    public Login() {
        super();
    }
    
    public Login(AuthenticationListEntry entry) {
        super();
        this.entry = entry;
    }
    
    public Login(String username, String password) {
        super();
        this.entry = new AuthenticationListEntry(username, password);
    }

    /**
     * Determines if the supplied username and password are contained in the list of usernames and passwords in the object registry.
     * @return A ResponseMessage indicating whether or not the username/password were accepted.
     */
    public ResponseMessage performService(ObjectRegistry objectRegistry) {
        AuthenticationList authList = (AuthenticationList) objectRegistry.lookupObject("authenticationList");
        HashSet authedClients = (HashSet) objectRegistry.lookupObject("authenticatedClients");
        ResponseMessage loginConfirm = new ResponseMessage(LOGIN_FAILED_PASSWORD); // set to the default failure
        
        if (authList != null) {
            // make sure the username is in the list
            if (authList.containsKey(entry.getUsername())) {
//                System.err.println("username found!");
                
                // if it is, then compare the passwords                
                if (((AuthenticationListEntry)(authList.get(entry.getUsername()))).compareEncryptedPassword(entry.getPassword())) {
                    
                    debug("password match!");
                    
                    // now make sure that the user isn't already logged-in
                    if (authedClients.contains(entry.getUsername())) {
                        loginConfirm.setResponse(LOGIN_FAILED_LOGGEDIN);
                    } else {
                        // we want to let the client know that it's logged in...
                        loginConfirm.setResponse(LOGIN_SUCCESSFUL);
                        // ...and add it to the list of logged-in clients
                        authedClients.add(entry.getUsername());
                    }
                }
            }
        }
        
        return loginConfirm;
    }

    /**
     * @return Returns the entry.
     */
    public AuthenticationListEntry getEntry() {
        return entry;
    }

    /**
     * @param entry The entry to set.
     */
    public void setEntry(AuthenticationListEntry entry) {
        this.entry = entry;
    }


}
