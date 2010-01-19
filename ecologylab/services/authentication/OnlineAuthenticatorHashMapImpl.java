/*
 * Created on Oct 31, 2006
 */
package ecologylab.services.authentication;

import java.util.HashMap;
import java.util.Set;

import ecologylab.generic.Debug;

/**
 * Encapsulates all authentication actions (tracking who is online, etc.), so that Servers don't
 * need to. Requires a backend database of users with passwords (an AuthenticationList).
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public class OnlineAuthenticatorHashMapImpl<A extends AuthenticationListEntry> extends Debug
		implements OnlineAuthenticator<A>
{
	protected AuthenticationList<A>	authList;

	private HashMap<String, String>	authedNameToSessionId	= new HashMap<String, String>();

	private HashMap<String, String>	authedSessionIdToName	= new HashMap<String, String>();

	/**
	 * Creates a new Authenticator using the given AuthenticationList as a backend database of
	 * usernames and passwords.
	 * 
	 * @param source
	 *          - the AuthenticationList of usernames and passwords to use for authentication.
	 */
	public OnlineAuthenticatorHashMapImpl(AuthenticationList<A> source)
	{
		authList = source;
	}

	/**
	 * @see ecologylab.services.authentication.OnlineAuthenticator#login(A, java.lang.String)
	 */
	public boolean login(A entry, String sessionId)
	{
		System.out.println("*****************************************");
		System.out.println("entry: " + entry.toString());

		boolean loggedInSuccessfully = false;

		// first see if the username exists
		// TODO removed the check if the username exists; isValid checks already
		if (entry != null)
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
					addAuthenticatedSession(entry.getUsername(), sessionId);
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
	 * @see ecologylab.services.authentication.OnlineAuthenticator#lookupUserLevel(A)
	 */
	public int lookupUserLevel(A entry)
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
	 * @see ecologylab.services.authentication.OnlineAuthenticator#usersLoggedIn(A)
	 */
	public Set<String> usersLoggedIn(A administrator)
	{
		if (this.lookupUserLevel(administrator) >= AuthLevels.ADMINISTRATOR)
		{
			return this.authedNameToSessionId.keySet();
		}
		else
		{
			return null;
		}
	}

	/**
	 * @see ecologylab.services.authentication.OnlineAuthenticator#logout(A, java.lang.String)
	 */
	public boolean logout(A entry, String sessionId)
	{
		try
		{
			if (entry.getUsername().equals(this.authedSessionIdToName.get(sessionId)))
			{
				removeSessionByUsername(entry.getUsername());
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

	public String getSessionId(A entry)
	{
		return this.authedNameToSessionId.get(entry.getUsername());
	}

	/**
	 * @see ecologylab.services.authentication.OnlineAuthenticator#isLoggedIn(java.lang.String)
	 */
	public boolean isLoggedIn(A entry)
	{
		return (authedNameToSessionId.containsKey(entry.getUsername()));
	}

	protected void removeSessionByUsername(String username)
	{
		Object key = authedNameToSessionId.remove(username);

		if (key != null)
		{
			this.authedSessionIdToName.remove(key);
		}
	}

	public void logoutBySessionId(String sessionId)
	{
		String key = authedSessionIdToName.remove(sessionId);

		if (key != null)
		{
			this.authedNameToSessionId.remove(key);
		}
	}

	/**
	 * Adds a username + sessionId to the appropriate tracking objects. Should be called only after a
	 * session has been created (such as by login()).
	 * 
	 * @param username
	 * @param sessionId
	 */
	protected void addAuthenticatedSession(String username, String sessionId)
	{
		this.authedSessionIdToName.put(sessionId, username);
		this.authedNameToSessionId.put(username, sessionId);
	}

	@Override
	public boolean sessionValid(String sessionId)
	{
		return this.authedSessionIdToName.containsKey(sessionId);
	}

	/**
	 * @see ecologylab.services.authentication.AuthenticationList#addEntry(ecologylab.services.authentication.AuthenticationListEntry)
	 */
	@Override
	public boolean addEntry(A entry)
	{
		return this.authList.addEntry(entry);
	}

	/**
	 * @see ecologylab.services.authentication.AuthenticationList#contains(ecologylab.services.authentication.AuthenticationListEntry)
	 */
	@Override
	public boolean contains(A entry)
	{
		return this.authList.contains(entry);
	}

	/**
	 * @see ecologylab.services.authentication.AuthenticationList#contains(java.lang.String)
	 */
	@Override
	public boolean contains(String username)
	{
		return this.authList.contains(username);
	}

	/**
	 * @see ecologylab.services.authentication.AuthenticationList#getAccessLevel(ecologylab.services.authentication.AuthenticationListEntry)
	 */
	@Override
	public int getAccessLevel(A entry)
	{
		return this.authList.getAccessLevel(entry);
	}

	/**
	 * @see ecologylab.services.authentication.AuthenticationList#isValid(ecologylab.services.authentication.AuthenticationListEntry)
	 */
	@Override
	public boolean isValid(A entry)
	{
		return this.authList.isValid(entry);
	}

	/**
	 * @see ecologylab.services.authentication.AuthenticationList#remove(ecologylab.services.authentication.AuthenticationListEntry)
	 */
	@Override
	public boolean remove(A entry)
	{
		return this.authList.remove(entry);
	}
}
