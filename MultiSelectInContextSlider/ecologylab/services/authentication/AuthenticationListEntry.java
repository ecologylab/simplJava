/*
 * Created on Mar 30, 2006
 */
package ecologylab.services.authentication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

import ecologylab.xml.ElementState;

/**
 * An entry for an AuthenticationList. Contains a username matched with a
 * password (which is stored and checked as a SHA-256 hash).
 * 
 * This class can be extended to include other pieces of information, such as
 * real names and email addresses; if desired.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public class AuthenticationListEntry extends ElementState
{

    public String username;

    public String password; // password automatically encrypted when added

    /**
     * Param-free constructor; normally used for translating from XML.
     * 
     */
    public AuthenticationListEntry()
    {
        super();
    }

    /**
     * Creates a new AuthenticationListEntry with the given username and
     * password.
     * 
     * @param username -
     *            the name of the user.
     * @param password -
     *            the password; will be hashed before it is stored.
     */
    public AuthenticationListEntry(String username, String password)
    {
        super();

        this.username = username;
        this.password = hashPassword(password);
    }

    /**
     * Sets the username of the AuthenticationListEntry.
     * 
     * @param username -
     *            the username to set.
     */
    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     * Uses SHA-256 encryption to store the password passed to it.
     * 
     * @param password -
     *            the password to hash and store.
     */
    public void setAndHashPassword(String password)
    {
        this.password = hashPassword(password);
    }

    /**
     * Compares the given hashed password (such as the kind from the
     * getPassword() method) to the one contained in this object.
     * 
     * @param hashedPassword -
     *            the password to check.
     * @return true if the passwords are identical, false otherwise.
     */
    public boolean compareHashedPassword(String hashedPassword)
    {
        return password.equals(hashedPassword);
    }

    /**
     * Compares the given unhashed password against the one stored here by
     * hashing it, then comparing it.
     * 
     * @param password -
     *            the unhashed password to check.
     * @return true if the passwords are identical, false otherwise.
     */
    public boolean comparePassword(String password)
    {
        return this.password.equals(hashPassword(password));
    }

    /**
     * Hashes the given password using SHA-256 and returns it as a String.
     * 
     * @param password -
     *            the password to hash.
     * @return a password hashed using SHA-256.
     */
    private static String hashPassword(String password)
    {

        try
        {
            MessageDigest encrypter = MessageDigest.getInstance("SHA-256");

            encrypter.update(password.getBytes());

            // convert to normal characters and return as a String
            return new String((new BASE64Encoder()).encode(encrypter.digest()));

        } catch (NoSuchAlgorithmException e)
        {
            // this won't happen in practice, once we have the right one! :D
            e.printStackTrace();
        }

        // this should never occur
        return password;
    }

    /**
     * @return Returns the password (hashed).
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * @return Returns the username.
     */
    public String getUsername()
    {
        return username;
    }
}
