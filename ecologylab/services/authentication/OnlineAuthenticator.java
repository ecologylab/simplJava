/**
 * 
 */
package ecologylab.services.authentication;

import java.util.Set;

/**
 * Wrapper for an AuthenticationList that adds higher-level semantic actions: Login / Logout, etc.,
 * while providing access to AuthenticationList methods.
 * 
 * Typically, OnlineAuthenticators will add extra abilities as well, such as logging.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 * 
 * @param <A>
 */
public interface OnlineAuthenticator<A extends AuthenticationListEntry> extends
		AuthenticationList<A>
{

	/**
	 * Attempts to log-in the given AuthenticationListEntry object.
	 * 
	 * @param entry
	 *          the AuthenticationListEntry containing a username and password that is attempting to
	 *          authenticate.
	 * @param sessionId
	 *          the session identifier for the connection, provided by OODSS.
	 * @return true if the login was successful; false if it was not.
	 */
	public boolean login(A entry, String sessionId);

	/**
	 * Looks up the authentication level, if any, of entry. Returns -1 if entry is not Authenticatable
	 * on this.
	 * 
	 * @param entry
	 *          - an instance of a subclass of AuthenticationListEntry with a username and password.
	 * @return
	 */
	public int lookupUserLevel(A entry);

	/**
	 * Looks up a list of logged-in users for an administrator.
	 * 
	 * @param administrator
	 *          the username and password of an administrator.
	 * @return if administrator is valid, a Set<String> of usernames for users that are logged-in;
	 *         else null.
	 */
	public Set<String> usersLoggedIn(A administrator);

	public boolean isLoggedIn(A entry);

	/**
	 * Removes the given username from all authenticated client lists if the sessionId matches the one
	 * currently stored for the entry.
	 * 
	 * @param entry
	 *          the entry to log out of the system.
	 * @param sessionId
	 *          the session identifier for the connection.
	 */
	public boolean logout(A entry, String sessionId);

	/**
	 * Retrieves the session identifier for a given entry. Generally, it is a security violation to
	 * lookup this information without being an administrator or the underlying application logic.
	 * 
	 * @param entry
	 * @return
	 */
	public String getSessionId(A entry);

	/**
	 * Logs a session out of the system without requiring administrator clearance.
	 * 
	 * @param sessionId
	 */
	public void logoutBySessionId(String sessionId);

	/**
	 * Looks up whether or not the session is logged-in. 
	 * 
	 * @param sessionId
	 * @return
	 */
	public boolean sessionValid(String sessionId);
}