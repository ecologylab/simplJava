/**
 * 
 */
package ecologylab.services.authentication.listener;


/**
 * @author Zachary O. Toups (zach@ecologylab.net)
 *
 */
public interface AuthenticationListener
{
	public void userLoggedIn(String username, String sessionId);
	public void userLoggedOut(String username, String sessionId);
}
