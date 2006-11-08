/*
 * Created on Oct 31, 2006
 */
package ecologylab.services.authentication;

import java.util.HashSet;

import ecologylab.generic.Debug;

/**
 * Encapsulates all authentication actions, so that Servers don't need to worry
 * about them too much. Requires a backend database of users with passwords.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public class Authenticator extends Debug
{
    protected AuthenticationList      authList             = new AuthenticationList();

    private HashSet<String> authedClients = new HashSet<String>();

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
     * @param keyAttachment -
     *            the attachment indicating the SelectionKey that matches the
     *            connection that is authenticating.
     * @return
     */
    public boolean login(AuthenticationListEntry entry)
    {
        boolean loggedInSuccessfully = false;

        // first see if the username exists
        if (authList.containsKey(entry.getUsername()))
        {
            // check password
            if (((AuthenticationListEntry) (authList.get(entry.getUsername())))
                    .compareHashedPassword(entry.getPassword()))
            {

                // now make sure that the user isn't already logged-in
                if (!authedClients.contains(entry.getUsername()))
                {
                    // mark login successful
                    loggedInSuccessfully = true;

                    // and add to collections
                    authedClients.add(entry.getUsername());
                }
            }
        }

        return loggedInSuccessfully;
    }

    /**
     * Removes the given username from all authenticated client lists.
     * 
     * @param entry
     */
    public void logout(AuthenticationListEntry entry)
    {
        authedClients.remove(entry.getUsername());
    }

    /**
     * Checks to see if the given username is already logged-in.
     * 
     * @param username
     * @return
     */
    public boolean isLoggedIn(String username)
    {
        return (authedClients.contains(username));
    }
}
