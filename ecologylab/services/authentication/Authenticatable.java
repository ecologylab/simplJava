/*
 * Created on Oct 31, 2006
 */
package ecologylab.services.authentication;

import java.net.InetAddress;

/**
 * Indicates that the implementer can be logged-into and out-of.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public interface Authenticatable
{
    public boolean login(AuthenticationListEntry entry, InetAddress address);
    
    public boolean logout(AuthenticationListEntry entry, InetAddress address);
    
    public boolean isLoggedIn(String username);
}
