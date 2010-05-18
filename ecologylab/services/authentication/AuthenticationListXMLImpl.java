/*
 * Created on Mar 30, 2006
 */
package ecologylab.services.authentication;

import java.util.HashMap;

import ecologylab.services.exceptions.SaveFailedException;
import ecologylab.xml.SaverState;
import ecologylab.xml.ElementState.xml_attribute;
import ecologylab.xml.ElementState.xml_map;


/**
 * Contains a HashMap of AuthenticationListEntry's that are hashed on their user key values. Raw
 * passwords are never serialized using this object, only one-way hashes of them (see
 * {@link ecologylab.services.authentication.User AuthenticationListEntry}).
 * 
 * Instances of this should be used by a server to determine valid usernames and passwords;
 * generally, a serialized instance of this is used as a backing store for such servers.
 * 
 * Most methods in this class are synchronized, so that they cannot be interleaved on multiple
 * threads. This should prevent consistency errors.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public class AuthenticationListXMLImpl<U extends User> extends SaverState 
//FIXME: Zach, how does this change for TNG ?
//implements AuthenticationList<U>
{
	@xml_map
	private HashMap<String, U>	authList	= new HashMap<String, U>();

	@xml_attribute
	private long										lastUID		= 0;

	@xml_attribute
	private boolean									autoSave	= false;

	/**
	 * No-argument constructor for XML translation.
	 */
	public AuthenticationListXMLImpl()
	{
		super();
	}

	public AuthenticationListXMLImpl(String backingFilename)
	{
		super(backingFilename);

		autoSave = true;
	}

	/**
	 * Adds the given entry to this.
	 * 
	 * @throws SaveFailedException
	 */
	public synchronized boolean addUser(U entry) throws SaveFailedException
	{
		if (!this.authList.containsKey(entry.getUserKey()))
		{
			authList.put(entry.getUserKey(), entry);
			entry.setUid(this.getNextUID());

			if (autoSave)
				this.save();

			return true;
		}

		return false;
	}

	private synchronized long getNextUID()
	{
		return lastUID++;
	}

	/**
	 * Cloning AuthenticationLists is not allowed, because it is a security violation.
	 * 
	 * This method just throws an UnsupportedOperationException.
	 */
	@Override
	public final Object clone() throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException(
				"Cannot clone an AuthenticationList, for security reasons.");
	}

	/**
	 * Checks to see if this contains the username given in entry; returns true if it does.
	 * 
	 * @param entry
	 * @return
	 */
	public synchronized boolean contains(U entry)
	{
		return this.contains(entry.getUserKey());
	}

	/**
	 * Checks to see if this contains the given username; returns true if it does.
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
	public synchronized int getAccessLevel(U entry)
	{
		return authList.get(entry.getUserKey()).getLevel();
	}

	/**
	 * Checks entry against the entries contained in this. Verifies that the username exists, and the
	 * password matches; returns true if both are true.
	 * 
	 * @param entry
	 * @return
	 */
	public synchronized boolean isValid(U entry)
	{
		return (authList.containsKey(entry.getUserKey()) && authList.get(entry.getUserKey())
				.compareHashedPassword(entry.getPassword()));
	}

	/**
	 * Attempts to remove the given object; this will succeed if and only if the following are true:
	 * 
	 * 1.) the Object is of type AuthenticationListEntry 2.) this list contains the
	 * AuthenticationListEntry 3.) the AuthenticationListEntry's username and password both match the
	 * one in this list
	 * 
	 * @param entry
	 *          the AuthenticationListEntry (username / password) to attempt to remove.
	 * @throws SaveFailedException
	 */
	public synchronized boolean removeUser(U entry) throws SaveFailedException
	{
		if (this.isValid(entry) && entry.equals(authList.remove(entry.getUserKey())))
		{
			if (autoSave)
				this.save();

			return true;
		}

		return false;
	}

	/**
	 * Returns a String indicating the number of entries in the AuthenticationList.
	 */
	@Override
	public String toString()
	{
		return "AuthenticationList containing " + authList.size() + " entries.";
	}

	/**
	 * @see ecologylab.services.authentication.AuthenticationList#setUID(ecologylab.services.authentication.User)
	 */
	public void setUID(U entry)
	{
		entry.setUid(this.authList.get(entry.getUserKey()).getUid());
	}
}
