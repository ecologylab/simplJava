/*
 * Created on Mar 30, 2006
 */
package ecologylab.services.authentication;


/**
 * Represents a list of usernames and passwords used to authenticate with a service.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public interface AuthenticationList<E extends AuthenticationListEntry>
{
	/**
	 * Adds the given entry to this.
	 */
	public boolean add(E entry);

	/**
	 * Checks to see if this contains the username given in entry; returns true
	 * if it does.
	 * 
	 * @param entry
	 * @return
	 */
	public boolean contains(E entry);

	/**
	 * Checks to see if this contains the given username; returns true if it
	 * does.
	 * 
	 * @param username
	 * @return
	 */
	public boolean contains(String username);

	/**
	 * Retrieves the access level for the given entry.
	 * 
	 * @param entry
	 * @return
	 */
	public int getAccessLevel(E entry);

	/**
	 * Checks entry against the entries contained in this. Verifies that the
	 * username exists, and the password matches; returns true if both are true.
	 * 
	 * @param entry
	 * @return
	 */
	public boolean isValid(E entry);

	/**
	 * Attempts to remove the given object; this will succeed if and only if the
	 * following are true:
	 * 
	 * 1.) the Object is of type AuthenticationListEntry 2.) this list contains
	 * the AuthenticationListEntry 3.) the AuthenticationListEntry's username and
	 * password both match the one in this list
	 * 
	 * @param entry
	 *           the AuthenticationListEntry (username / password) to attempt to
	 *           remove.
	 */
	public boolean remove(E entry);

	/**
	 * Returns a String indicating the number of entries in the
	 * AuthenticationList.
	 */
	public String toString();
}
