/**
 * 
 */
package ecologylab.services.authentication.listener;

import java.net.InetAddress;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 *
 */
public interface AuthenticationListener
{
	public void userLoggedIn(String username, InetAddress addr);
	public void userLoggedOut(String username, InetAddress addr);
}
