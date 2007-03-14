/*
 * Created on Oct 31, 2006
 */
package ecologylab.services.authentication;

import java.net.InetAddress;
import java.util.HashMap;

import ecologylab.generic.Debug;

/**
 * Encapsulates all authentication actions, so that Servers don't need to worry
 * about them too much. Requires a backend database of users with passwords.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public class Authenticator extends Debug
{
    protected AuthenticationList         authList       = new AuthenticationList();

    private HashMap<String, InetAddress> authedNameToIP = new HashMap<String, InetAddress>();

    private HashMap<InetAddress, String> authedIPToName = new HashMap<InetAddress, String>();

    /**
     * Creates a new Authenticator using the given AuthenticationList as a
     * backend database of usernames and passwords.
     * 
     * @param source -
     *            the AuthenticationList of usernames and passwords to use for
     *            authentication.
     */
    public Authenticator(AuthenticationList source)
    {
        authList = source;
    }

    /**
     * Attempts to log-in the given AuthenticationListEntry object. In order for
     * it to be authenticated, the following MUST be true:
     * 
     * 1.) authList must contain a username entry that matches
     * entry.getUsername().
     * 
     * 2.) the entry in authList that matches the username MUST have an
     * identical hashed password.
     * 
     * 3.) the username must not already be contained in authedClientsIdToKey
     * (i.e., the username must not already be logged in).
     * 
     * @param entry -
     *            the AuthenticationListEntry containing a username and password
     *            that is attempting to authenticate.
     * 
     * @return
     */
    public boolean login(AuthenticationListEntry entry, InetAddress address)
    {
        System.out.println("*****************************************");
        boolean loggedInSuccessfully = false;
        System.out.println("entry: "+entry);
        System.out.println(entry.getUsername());
        System.out.println(authList.contains(entry));

        // first see if the username exists
        if (entry != null && authList.contains(entry.getUsername()))
        {
            // check password
            if (authList.isValid(entry))
            {
                // now make sure that the user isn't already logged-in
                if (!authedNameToIP.containsKey(entry.getUsername()))
                {
                    // mark login successful
                    loggedInSuccessfully = true;

                    // and add to collections
                    add(entry.getUsername(), address);
                }
            }
        }
        else if (entry == null)
        {
            debug("<null> attempted login.");
            loggedInSuccessfully = false;
        }

        return loggedInSuccessfully;
    }

    /**
     * Removes the given username from all authenticated client lists if the IP
     * address matches the one currently stored for the entry.
     * 
     * @param entry
     */
    public boolean logout(AuthenticationListEntry entry, InetAddress address)
    {
        try
        {
            if (this.authedIPToName.get(address).equals(entry.getUsername()))
            {
                remove(entry.getUsername());
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks to see if the given username is already logged-in.
     * 
     * @param username
     * @return
     */
    public boolean isLoggedIn(String username)
    {
        return (authedNameToIP.containsKey(username));
    }

    protected void remove(String username)
    {
        InetAddress key = authedNameToIP.remove(username);

        if (key != null)
        {
            this.authedIPToName.remove(key);
        }
    }

    public void remove(InetAddress address)
    {
        String key = authedIPToName.remove(address);

        if (key != null)
        {
            this.authedNameToIP.remove(key);
        }
    }

    protected void add(String username, InetAddress address)
    {
        this.authedIPToName.put(address, username);
        this.authedNameToIP.put(username, address);
    }
}
