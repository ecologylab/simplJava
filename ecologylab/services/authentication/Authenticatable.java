/*
 * Created on Oct 31, 2006
 */
package ecologylab.services.authentication;

/**
 * Indicates that the implementer can be logged-into and out-of. Includes ability to add and remove
 * users.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public interface Authenticatable<E extends User>
{
	/**
	 * Determines whether a user can log in (based on the content of entry and its originating IP
	 * address (address)). Handles any necessary bookkeeping (such as a list of authenticated
	 * clients).
	 * 
	 * @param entry
	 *          the information about the user attempting to log in (username / password, etc.).
	 * @param sessionId
	 *          the session id for the connected socket.
	 * @return true if the user successfully logged in, false otherwise.
	 */
	public boolean login(E entry, String sessionId);

	/**
	 * Logs the user out of the system (based on the content of entry and its originating IP address
	 * (address)). Handles necessary bookkeeping (such as a list of authenticated clients).
	 * 
	 * @param entry
	 *          the information about the user attempting to log out (username / password, etc.).
	 * @param address
	 *          the originating IP address for the logout attempt.
	 * @return true if the user successfully logged out, false otherwise. This method may return false
	 *         if the user was never logged in, or if the attempt appears to be a spoof.
	 */
	public boolean logout(E entry, String sessionId);

	/**
	 * Indicates whether or not the supplied username is currently logged-in to the system.
	 * 
	 * @param username
	 *          the username to check.
	 * @return
	 */
	public boolean isLoggedIn(E entry);

	/**
	 * Adds the given entry to this.
	 */
	public boolean addNewUser(E entry);

	/**
	 * Attempts to remove the given object; this will succeed if and only if the following are true:
	 * 
	 * 1.) the Object is of type AuthenticationListEntry 2.) the backing authentication list contains
	 * the AuthenticationListEntry 3.) the AuthenticationListEntry's username and password both match
	 * the one in this list
	 * 
	 * @param entry
	 *          the AuthenticationListEntry (username / password) to attempt to remove.
	 */
	public boolean removeExistingUser(E entry);
}
