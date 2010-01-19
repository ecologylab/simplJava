/*
 * Created on Mar 30, 2006
 */
package ecologylab.services.authentication;

/**
 * Represents a list of usernames and passwords used to authenticate with a service. Subclasses may
 * use another key than "username", but the effect is still a String to hashed String matching for
 * authentication.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public interface AuthenticationList<A extends AuthenticationListEntry>
{
	/**
	 * Adds a new entry to the AuthenticationList, if the entry's key does not otherwise exist.
	 * Multiple users with the same username are not allowed.
	 * 
	 * @return true if the user was successfully added; false otherwise.
	 */
	public boolean addEntry(A entry);

	/**
	 * Checks to see if this contains the username given in entry; returns true if it does.
	 * 
	 * @param entry
	 * @return
	 */
	public boolean contains(A entry);

	/**
	 * Checks to see if this contains the given username; returns true if it does.
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
	public int getAccessLevel(A entry);

	/**
	 * Checks entry against the entries contained in this. Verifies that the username exists, and the
	 * password matches; returns true if both are true.
	 * 
	 * @param entry
	 * @return true if the entry is valid (username and password match those on file).
	 */
	public boolean isValid(A entry);

	/**
	 * Attempts to remove the given object; this will succeed if and only if the entry's username and
	 * password match those on file.
	 * 
	 * @param entry
	 *          the AuthenticationListEntry (username / password) to attempt to remove.
	 * @return true if the entry was removed from the AuthenticationList.
	 */
	public boolean remove(A entry);

	/**
	 * Returns a String indicating the number of entries in the AuthenticationList.
	 */
	public String toString();
}
