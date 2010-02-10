/*
 * Created on Mar 30, 2006
 */
package ecologylab.services.authentication;

import ecologylab.xml.xml_inherit;

/**
 * Subclass of User that provides an email address as auxiliary information. Email address may be
 * stored in the database, but is not used as a key.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
@xml_inherit
public class UserWithEmail extends User
{
	/**
	 * The email address for the user. Not used as a key, only provided as additional information.
	 * Always stored in lowercase.
	 */
	@xml_attribute
	private String	email	= "";

	/** No-argument constructor for serialization. */
	public UserWithEmail()
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
	public UserWithEmail(String userKey, String plaintextPassword, String email)
	{
		super(userKey, plaintextPassword);
		
		this.setEmail(email);
	}

	/**
	 * Sets the email of the AuthenticationListEntry.
	 * 
	 * @param email
	 *          - the email to set.
	 */
	public void setEmail(String email)
	{
		if (email != null)
		this.email = email.toLowerCase();
	}

	/**
	 * @see ecologylab.generic.Debug#toString()
	 */
	@Override
	public String toString()
	{
		return super.toString() + " (" + email + ")";
	}

	/**
	 * @return the email
	 */
	public String getEmail()
	{
		return email;
	}
}
