/*
 * Created on Oct 31, 2006
 */
package ecologylab.services.authentication;

/**
 * Indicates that the implementer can be logged-into and out-of.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public interface Authenticatable
{
    public boolean login(AuthenticationListEntry entry);
    
    public void logout(AuthenticationListEntry entry);
    
    public boolean isLoggedIn(String username);
}
