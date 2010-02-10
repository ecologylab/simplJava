/*
 * Created on Mar 30, 2006
 */
package ecologylab.services.authentication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lib.Base64Coder;
import ecologylab.xml.ElementState;
import ecologylab.xml.xml_inherit;
import ecologylab.xml.ElementState.xml_other_tags;
import ecologylab.xml.types.element.Mappable;

/**
 * An entry for an AuthenticationList. Contains a user key matched with a password (which is stored
 * and checked as a SHA-256 hash).
 * 
 * This class can be extended to include other pieces of information, such as real names and email
 * addresses; if desired.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public @xml_inherit
@xml_other_tags("authentication_list_entry")
class User extends ElementState implements AuthLevels, Mappable<String>
{
	/**
	 * The user's key in the backing store. This key must be provided, along with the password, to
	 * gain access to the system.
	 * 
	 * This field is *always* lowercase, but supports any character and any length. Calling classes
	 * must ensure that other conditions are met.
	 * 
	 * Backwards compatibility for XML translate from is provided for "username", as this was the
	 * historical name for this field.
	 */
	@xml_attribute
	@xml_other_tags("username")
	private String	userKey		= "";

	/**
	 * Represents the password for this user key. It is automatically converted to a hash when added
	 * via methods so it should never be modified through any other way!
	 */
	@xml_attribute
	private String	password	= "";

	/**
	 * Represents the administrator level of the user.
	 * 
	 * 0 = normal user (NORMAL_USER) (Others can be added here as necessary.) 10 = administrator
	 * (ADMINISTRATOR)
	 */
	@xml_attribute
	private int			level			= NORMAL_USER;

	/**
	 * Unique identifier for the user. Created by the AuthenticationList when added. This attribute
	 * should not be manually assigned.
	 */
	@xml_attribute
	private long		uid;

	/**
	 * The current session associated with this object. If the User is logged-in, then this will have
	 * a value; otherwise, it will be null.
	 */
	private String	sessionId	= null;

	/**
	 * No-argument constructor for serialization.
	 */
	public User()
	{
		super();
	}

	/**
	 * Creates a new AuthenticationListEntry with the given username and password.
	 * 
	 * @param username
	 *          - the name of the user.
	 * @param plaintextPassword
	 *          - the password; will be hashed before it is stored.
	 */
	public User(String username, String plaintextPassword)
	{
		this();

		this.userKey = username.toLowerCase();
		this.password = hashPassword(plaintextPassword);
	}

	/**
	 * Sets the username of the AuthenticationListEntry.
	 * 
	 * @param username
	 *          - the username to set.
	 */
	public void setUserKey(String username)
	{
		this.userKey = username.toLowerCase();
	}

	/**
	 * Uses SHA-256 encryption to store the password passed to it.
	 * 
	 * @param plaintextPassword
	 *          - the password to hash and store.
	 */
	public void setAndHashPassword(String plaintextPassword)
	{
		this.password = hashPassword(plaintextPassword);
	}

	/**
	 * Compares the given hashed password (such as the kind from the getPassword() method) to the one
	 * contained in this object.
	 * 
	 * @param hashedPassword
	 *          - the password to check.
	 * @return true if the passwords are identical, false otherwise.
	 */
	public boolean compareHashedPassword(String hashedPassword)
	{
		return password.equals(hashedPassword);
	}

	/**
	 * Compares the given unhashed password against the one stored here by hashing it, then comparing
	 * it.
	 * 
	 * @param plaintextPassword
	 *          - the unhashed password to check.
	 * @return true if the passwords are identical, false otherwise.
	 */
	public boolean comparePassword(String plaintextPassword)
	{
		return this.password.equals(hashPassword(plaintextPassword));
	}

	/**
	 * Hashes the given password using SHA-256 and returns it as a String.
	 * 
	 * @param plaintextPassword
	 *          - the password to hash.
	 * @return a password hashed using SHA-256.
	 */
	private static String hashPassword(String plaintextPassword)
	{
		if (plaintextPassword != null)
		{
			try
			{
				MessageDigest encrypter = MessageDigest.getInstance("SHA-256");

				encrypter.update(plaintextPassword.getBytes());

				// convert to normal characters and return as a String
				return new String(Base64Coder.encode(encrypter.digest()));

			}
			catch (NoSuchAlgorithmException e)
			{
				// this won't happen in practice, once we have the right one! :D
				e.printStackTrace();
			}

			// this should never occur
			return plaintextPassword;
		}
		
		return null;
	}

	/**
	 * @return Returns the password (hashed).
	 */
	String getPassword()
	{
		return password;
	}

	/**
	 * @return Returns the username.
	 */
	public String getUserKey()
	{
		return userKey.toLowerCase();
	}

	/**
	 * Returns hashCode() called on username.
	 */
	@Override
	public int hashCode()
	{
		return userKey.hashCode();
	}

	/**
	 * @return the level
	 */
	public int getLevel()
	{
		return level;
	}

	/**
	 * @param level
	 *          the level to set
	 */
	public void setLevel(int level)
	{
		this.level = level;
	}

	/**
	 * @see ecologylab.generic.Debug#toString()
	 */
	@Override
	public String toString()
	{
		return "AuthenticationListEntry: " + userKey;
	}

	public String key()
	{
		return userKey;
	}

	/**
	 * @return the uid
	 */
	public long getUid()
	{
		return uid;
	}

	/**
	 * @param uid
	 *          the uid to set
	 */
	void setUid(long uid)
	{
		this.uid = uid;
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId()
	{
		return sessionId;
	}

	/**
	 * @param sessionId
	 *          the sessionId to set
	 */
	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}
}
