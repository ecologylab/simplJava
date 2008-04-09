/*
 * Created on Mar 30, 2006
 */
package ecologylab.services.authentication;

import ecologylab.xml.ElementState;
import ecologylab.xml.types.element.HashMapState;

/**
 * Contains a HashMap of AuthenticationListEntry's that are hashed on their
 * username values. Raw passwords are never serialized using this object, only
 * one-way hashes of them (see
 * {@link ecologylab.services.authentication.AuthenticationListEntry AuthenticationListEntry}).
 * 
 * Instances of this should be used by a server to determine valid usernames and
 * passwords; generally, a serialized instance of this is used as a backing
 * store for such servers.
 * 
 * Most methods in this class are synchronized, so that they cannot be
 * interleaved on multiple threads. This should prevent consistency errors.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public class AuthenticationList<E extends AuthenticationListEntry> extends
		ElementState
{
	@xml_nested private HashMapState<String, E>	authList	= new HashMapState<String, E>();

	/** No-argument constructor for XML translation. */
	public AuthenticationList()
	{
		super();
	}

	/**
	 * Adds the given entry to this.
	 */
	public synchronized boolean add(E entry)
	{
		if (!this.authList.containsKey(entry.getUsername()))
		{
			authList.put(entry.getUsername(), entry);

			return true;
		}

		return false;
	}

	/**
	 * Cloning AuthenticationLists is not allowed, because it is a security
	 * violation.
	 * 
	 * This method just throws an UnsupportedOperationException.
	 */
	@Override public final Object clone() throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException(
				"Cannot clone an AuthenticationList, for security reasons.");
	}

	/**
	 * Checks to see if this contains the username given in entry; returns true
	 * if it does.
	 * 
	 * @param entry
	 * @return
	 */
	public synchronized boolean contains(AuthenticationListEntry entry)
	{
		return this.contains(entry.getUsername());
	}

	/**
	 * Checks to see if this contains the given username; returns true if it
	 * does.
	 * 
	 * @param username
	 * @return
	 */
	public synchronized boolean contains(String username)
	{
		return authList.containsKey(username);
	}

	/**
	 * Retrieves the access level for the given entry.
	 * 
	 * @param entry
	 * @return
	 */
	public synchronized int getAccessLevel(AuthenticationListEntry entry)
	{
		return authList.get(entry.getUsername()).getLevel();
	}

	/**
	 * Checks entry against the entries contained in this. Verifies that the
	 * username exists, and the password matches; returns true if both are true.
	 * 
	 * @param entry
	 * @return
	 */
	public synchronized boolean isValid(AuthenticationListEntry entry)
	{
		return (authList.containsKey(entry.getUsername()) && authList.get(
				entry.getUsername()).compareHashedPassword(entry.getPassword()));
	}

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
	public synchronized boolean remove(AuthenticationListEntry entry)
	{
		if (this.isValid(entry))
		{
			return entry.equals(authList.remove(entry.getUsername()));
		}

		return false;
	}

	/**
	 * Returns a String indicating the number of entries in the
	 * AuthenticationList.
	 */
	@Override public String toString()
	{
		return "AuthenticationList containing " + authList.size() + " entries.";
	}
}
