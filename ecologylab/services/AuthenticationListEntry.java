/*
 * Created on Mar 30, 2006
 */
package ecologylab.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

import ecologylab.xml.ElementState;

public class AuthenticationListEntry extends ElementState {

    public String username;
    public String password; // password automatically encrypted when added
    
    public AuthenticationListEntry() {
        super();
    }
    
    public AuthenticationListEntry(String username, String password) {
        super();
        
        this.username = username;
        this.password = encryptPassword(password);
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Uses SHA-256 encryption to store the password passed to it.
     * @param password
     */
    public void setAndEncryptPassword(String password) {
        this.password = encryptPassword(password);
    }
    
    public boolean compareEncryptedPassword(String encryptedPassword) {
        return password.equals(encryptedPassword);
    }
    
    public boolean compareUnencryptedPassword(String unencryptedPassword) {
        return password.equals(encryptPassword(unencryptedPassword));
    }
    
    private static String encryptPassword(String password) {
        
        try {
            MessageDigest encrypter = MessageDigest.getInstance("SHA-256");
            
            encrypter.update(password.getBytes());
            
            // convert to normal characters and return as a String
            return new String((new BASE64Encoder()).encode(encrypter.digest()));
            
        } catch (NoSuchAlgorithmException e) {
            // this won't happen in practice, once we have the right one!  :D
            e.printStackTrace();
        }
        
        // this should never occur
        return password;
    }

    /**
     * @return Returns the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return Returns the username.
     */
    public String getUsername() {
        return username;
    }
}
