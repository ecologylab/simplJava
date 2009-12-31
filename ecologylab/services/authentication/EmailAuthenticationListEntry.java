/*
 * Created on Mar 30, 2006
 */
package ecologylab.services.authentication;

import ecologylab.xml.xml_inherit;

/**
 * An entry for an AuthenticationList. Contains a username matched with a password (which is stored
 * and checked as a SHA-256 hash).
 * 
 * This class can be extended to include other pieces of information, such as real names and email
 * addresses; if desired.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
@xml_inherit
public class EmailAuthenticationListEntry extends AuthenticationListEntry
{
	@xml_attribute
	private String	email	= "";

	/**
	 * No-argument constructor for serialization.
	 */
	public EmailAuthenticationListEntry()
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
	public EmailAuthenticationListEntry(String email, String username, String plaintextPassword)
	{
		super(username, plaintextPassword);

		this.email = email.toLowerCase();
	}

	/**
	 * Sets the email of the AuthenticationListEntry.
	 * 
	 * @param email
	 *          - the email to set.
	 */
	public void setEmail(String email)
	{
		this.email = email.toLowerCase();
	}

	/**
	 * @see ecologylab.generic.Debug#toString()
	 */
	@Override
	public String toString()
	{
		return super.toString()+" ("+email+")";
	}

	/**
	 * @return the email
	 */
	public String getEmail()
	{
		return email;
	}
}
