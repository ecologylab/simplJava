/*
 * Created on Mar 30, 2006
 */
package ecologylab.services.authentication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

import ecologylab.xml.ElementState;
import ecologylab.xml.xml_inherit;
import ecologylab.xml.types.element.Mappable;

/**
 * An entry for an AuthenticationList. Contains a username matched with a password (which is stored
 * and checked as a SHA-256 hash).
 * 
 * This class can be extended to include other pieces of information, such as real names and email
 * addresses; if desired.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public @xml_inherit
class AuthenticationListEntry extends ElementState implements AuthLevels, Mappable<String>
{
	private @xml_attribute
	String	username	= "";

	/**
	 * Represents the password for this username. It is automatically converted to a hash when added
	 * via methods so it should never be modified through any other way!
	 */
	private @xml_attribute
	String	password	= "";

	/**
	 * Represents the administrator level of the user.
	 * 
	 * 0 = normal user (NORMAL_USER) (Others can be added here as necessary.) 10 = administrator
	 * (ADMINISTRATOR)
	 */
	private @xml_attribute
	int			level			= NORMAL_USER;

	/**
	 * No-argument constructor for serialization.
	 */
	public AuthenticationListEntry()
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
	public AuthenticationListEntry(String username, String plaintextPassword)
	{
		this();

		this.username = username.toLowerCase();
		this.password = hashPassword(plaintextPassword);
	}

	/**
	 * Sets the username of the AuthenticationListEntry.
	 * 
	 * @param username
	 *          - the username to set.
	 */
	public void setUsername(String username)
	{
		this.username = username.toLowerCase();
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
		try
		{
			MessageDigest encrypter = MessageDigest.getInstance("SHA-256");

			encrypter.update(plaintextPassword.getBytes());

			// convert to normal characters and return as a String
			return new String((new BASE64Encoder()).encode(encrypter.digest()));

		}
		catch (NoSuchAlgorithmException e)
		{
			// this won't happen in practice, once we have the right one! :D
			e.printStackTrace();
		}

		// this should never occur
		return plaintextPassword;
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
	public String getUsername()
	{
		return username.toLowerCase();
	}

	/**
	 * Returns hashCode() called on username.
	 */
	@Override
	public int hashCode()
	{
		return username.hashCode();
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
		return "AuthenticationListEntry: " + username;
	}

	public String key()
	{
		return username;
	}
}
