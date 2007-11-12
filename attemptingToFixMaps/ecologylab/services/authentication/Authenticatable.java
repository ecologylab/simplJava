/*
 * Created on Oct 31, 2006
 */
package ecologylab.services.authentication;

import java.net.InetAddress;

/**
 * Indicates that the implementer can be logged-into and out-of.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public interface Authenticatable<E extends AuthenticationListEntry>
{
	/**
	 * Determines whether a user can log in (based on the content of entry and its originating IP address (address)).
	 * Handles any necessary bookkeeping (such as a list of authenticated clients).
	 * 
	 * @param entry
	 *           the information about the user attempting to log in (username / password, etc.).
	 * @param address
	 *           the originating IP address for the login attempt.
	 * @return true if the user successfully logged in, false otherwise.
	 */
	public boolean login(E entry, InetAddress address);

	/**
	 * Logs the user out of the system (based on the content of entry and its originating IP address (address)). Handles
	 * necessary bookkeeping (such as a list of authenticated clients).
	 * 
	 * @param entry
	 *           the information about the user attempting to log out (username / password, etc.).
	 * @param address
	 *           the originating IP address for the logout attempt.
	 * @return true if the user successfully logged out, false otherwise. This method may return false if the user was
	 *         never logged in, or if the attempt appears to be a spoof.
	 */
	public boolean logout(E entry, InetAddress address);

	/**
	 * Indicates whether or not the supplied username is currently logged-in to the system.
	 * 
	 * @param username
	 *           the username to check.
	 * @return
	 */
	public boolean isLoggedIn(String username);
}
