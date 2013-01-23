package ecologylab.net;

import java.io.IOException;
import java.net.URL;

/**
 * Helps for connect when it only handles net-based connections, not file-based ones.
 * 
 * @author andruid
 */
public interface ConnectionHelperJustRemote
{
	/**
	 * Used to provid status feedback to the user.
	 * 
	 * @param message
	 */
	public void		displayStatus(String message);
	
	/**
	 * Shuffle referential models when a redirect is observed, if you like.
	 * 
	 * @param connectionURL
	 * 
	 * @return		true if the redirect is o.k., and we should continue processing the connect().
	 * 				false if the redirect is unacceptable, and we should terminate processing.
	 * @throws Exception 
	 */
	public boolean	processRedirect(URL connectionURL) throws IOException;

}
