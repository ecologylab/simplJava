/*
 * Created on Mar 30, 2006
 */
package ecologylab.services.authentication.messages;

import java.net.InetAddress;

import ecologylab.generic.ObjectRegistry;
import ecologylab.services.authentication.Authenticatable;
import ecologylab.services.authentication.AuthenticationListEntry;
import ecologylab.services.authentication.registryobjects.AuthServerRegistryObjects;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;

/**
 * Used to log into a server that requires authentication; carries username and
 * password information in strings, and checks them against "authenticationList"
 * in the objectRegistry.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public class Login extends RequestMessage implements AuthMessages,
        AuthServerRegistryObjects
{

    public AuthenticationListEntry entry         = new AuthenticationListEntry(
                                                         "", "");

    public InetAddress             clientAddress = null;

    /**
     * Should not normally be used; only for XML translations.
     */
    public Login()
    {
        super();
    }

    /**
     * Creates a new Login object using the given AuthenticationListEntry.
     * 
     * @param entry -
     *            the entry to use for the Login object.
     */
    public Login(AuthenticationListEntry entry)
    {
        super();
        this.entry = entry;
    }

    /**
     * Creates a new Login object using the given username and password; the
     * password is hashed, per AuthenticationListEntry, before it is stored.
     * 
     * @param username -
     *            the username to use for the Login object.
     * @param password -
     *            the password to hash, and then use for the Login object.
     */
    public Login(String username, String password)
    {
        this(new AuthenticationListEntry(username, password));
    }

    /**
     * Determines if the supplied username and password are contained in the
     * list of usernames and passwords in the object registry.
     * 
     * @return A ResponseMessage indicating whether or not the username/password
     *         were accepted.
     */
    public ResponseMessage performService(ObjectRegistry objectRegistry)
    {
        Authenticatable server = (Authenticatable) objectRegistry.lookupObject(MAIN_AUTHENTICATABLE);
        
        // set to the default failure message
        LoginStatusResponse loginConfirm = new LoginStatusResponse(
                LOGIN_FAILED_PASSWORD); 

        boolean loginSuccess = server.login(this.entry);

        if (loginSuccess)
        { // we're logged in!
            loginConfirm.setResponseMessage(LOGIN_SUCCESSFUL);
        }
        else
        {
            // figure out why it failed
            if (server.isLoggedIn(entry.getUsername()))
            {
                loginConfirm.setResponseMessage(LOGIN_FAILED_LOGGEDIN);
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
     * @param entry
     *            The entry to set.
     */
    public void setEntry(AuthenticationListEntry entry)
    {
        this.entry = entry;
    }

    /**
     * @return Returns the clientAddress.
     */
    public InetAddress getClientAddress()
    {
        return clientAddress;
    }

    /**
     * @param clientAddress
     *            The clientAddress to set.
     */
    public void setClientAddress(InetAddress clientAddress)
    {
        this.clientAddress = clientAddress;
    }
}
