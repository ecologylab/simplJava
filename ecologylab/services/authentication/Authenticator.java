/*
 * Created on Oct 31, 2006
 */
package ecologylab.services.authentication;

import java.util.HashMap;
import java.util.Set;

import ecologylab.generic.Debug;

/**
 * Encapsulates all authentication actions, so that Servers don't need to. Requires a backend database of users with
 * passwords (an AuthenticationList).
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public class Authenticator<A extends AuthenticationListEntry> extends Debug
{
	protected AuthenticationList<A>			authList			= new AuthenticationList<A>();

	private HashMap<String, Object>	authedNameToSessionId	= new HashMap<String, Object>();

	private HashMap<Object, String>	authedSessionIdToName	= new HashMap<Object, String>();

	/**
	 * Creates a new Authenticator using the given AuthenticationList as a backend database of usernames and passwords.
	 * 
	 * @param source -
	 *           the AuthenticationList of usernames and passwords to use for authentication.
	 */
	public Authenticator(AuthenticationList<A> source)
	{
		authList = source;
	}

	/**
	 * Attempts to log-in the given AuthenticationListEntry object. In order for it to be authenticated, the following
	 * MUST be true:
	 * 
	 * 1.) authList must contain a username entry that matches entry.getUsername().
	 * 
	 * 2.) the entry in authList that matches the username MUST have an identical hashed password.
	 * 
	 * 3.) the username must not already be contained in authedClientsIdToKey (i.e., the username must not already be
	 * logged in).
	 * 
	 * @param entry -
	 *           the AuthenticationListEntry containing a username and password that is attempting to authenticate.
	 * 
	 * @return
	 */
	public boolean login(A entry, String sessionId)
	{
		System.out.println("*****************************************");
		boolean loggedInSuccessfully = false;
		System.out.println("entry: " + entry);
		System.out.println(entry.getUsername());
		System.out.println(authList.contains(entry));

		// first see if the username exists
		if (entry != null && authList.contains(entry.getUsername()))
		{
			// check password
			if (authList.isValid(entry))
			{
				// now make sure that the user isn't already logged-in
				if (!authedSessionIdToName.containsKey(entry.getUsername()))
				{
					// mark login successful
					loggedInSuccessfully = true;

					// and add to collections
					add(entry.getUsername(), sessionId);
				}
				else
				{
					debug("already logged in.");
				}
			}
			else
			{
				debug("invalid entry");
			}
		}
		else if (entry == null)
		{
			debug("<null> attempted login.");
			loggedInSuccessfully = false;
		}
		else
		{
			debug("username: " + entry.getUsername() + " does not exist in authentication list.");
		}

		return loggedInSuccessfully;
	}

	/**
	 * Looks up the authentication level, if any, of entry. Returns -1 if entry is not authenticatable on this.
	 * 
	 * @param entry -
	 *           an instance of a subclass of AuthenticationListEntry with a username and password.
	 * @return
	 */
	public int verifyCredentials(A entry)
	{
		if (authList.isValid(entry))
		{
			return authList.getAccessLevel(entry);
		}
		else
		{
			return -1;
		}
	}

	/**
	 * Looks up a list of logged-in users for an administrator.
	 * 
	 * @param administrator -
	 *           the username and password of an administrator.
	 * @return if administrator is valid, an ArrayList<String> of usernames for users that are logged-in; else null.
	 */
	public Set<String> usersLoggedIn(A administrator)
	{
		if (this.verifyCredentials(administrator) >= AuthLevels.ADMINISTRATOR)
		{
			return this.authedNameToSessionId.keySet();
		}
		else
		{
			return null;
		}
	}

	/**
	 * Removes the given username from all authenticated client lists if the IP address matches the one currently stored
	 * for the entry.
	 * 
	 * @param entry
	 */
	public boolean logout(A entry, String sessionId)
	{
		try
		{
			if (entry.getUsername().equals(this.authedSessionIdToName.get(sessionId)))
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
		return (authedNameToSessionId.containsKey(username));
	}

	protected void remove(String username)
	{
		Object key = authedNameToSessionId.remove(username);

		if (key != null)
		{
			this.authedSessionIdToName.remove(key);
		}
	}

	public void removeBySessionId(Object sessionId)
	{
		Object key = authedSessionIdToName.remove(sessionId);

		if (key != null)
		{
			this.authedNameToSessionId.remove(key);
		}
	}

	protected void add(String username, String sessionId)
	{
		this.authedSessionIdToName.put(sessionId, username);
		this.authedNameToSessionId.put(username, sessionId);
	}
}
