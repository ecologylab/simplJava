/*
 * Created on Mar 30, 2006
 */
package ecologylab.services.authentication;

import ecologylab.services.exceptions.SaveFailedException;

/**
 * Represents a list of user keys and passwords used to authenticate with a service. A user key is
 * typically an email address or username, depending on system design.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public interface AuthenticationList<U extends User>
{
	/**
	 * Adds a new entry to the AuthenticationList, if the entry's user key does not otherwise exist.
	 * Multiple users with the same key are not allowed.
	 * 
	 * @return true if the user was successfully added; false otherwise.
	 * @throws SaveFailedException
	 *           if the method call was unable to save() the AuthenticationList. In this case, the
	 *           in-memory copy will be correct, but the backing store will not reflect the changes.
	 */
	public boolean addUser(U user) throws SaveFailedException;

	/**
	 * Checks to see if this contains the user key given in entry; returns true if it does.
	 * 
	 * @param entry
	 * @return
	 */
	public boolean contains(U user);

	/**
	 * Retrieves the access level for the given entry.
	 * 
	 * @param entry
	 * @return
	 */
	public int getAccessLevel(U user);

	/**
	 * Checks entry against the entries contained in this. Verifies that the username exists, and the
	 * password matches; returns true if both are true.
	 * 
	 * @param entry
	 * @return true if the entry is valid (username and password match those on file).
	 */
	public boolean isValid(U entry);

	/**
	 * Attempts to remove the given entry; this will succeed if and only if the entry's user key and
	 * password match those on file.
	 * 
	 * @param entry
	 *          the AuthenticationListEntry (user key / password) to attempt to remove.
	 * @return true if the entry was removed from the AuthenticationList.
	 * @throws SaveFailedException
	 *           if the method call was unable to save() the AuthenticationList. In this case, the
	 *           in-memory copy will be correct, but the backing store will not reflect the changes.
	 */
	public boolean removeUser(U user) throws SaveFailedException;

	/**
	 * Returns a String indicating the number of entries in the AuthenticationList.
	 */
	public String toString();

	/**
	 * Uses the backing store to set the UID for the given entry. This is used after logging-in a
	 * user; the user will have a UID in the backing store, but the current entry will not have it.
	 * 
	 * @param entry
	 */
	void setUID(U user);

	/**
	 * Indicates to the AuthenticationList that it should save itself to the backing store. In the
	 * case of a database implementation, this will do nothing. In the case of an XML implementation,
	 * this will cause the AuthenticationList to write itself to the filesystem using the file
	 * specified in the class.
	 * 
	 * This method is automatically called by the addUser and removeUser methods.
	 * 
	 * @throws SaveFailedException
	 *           if there was an error when trying to write.
	 */
	public void save() throws SaveFailedException;
}
