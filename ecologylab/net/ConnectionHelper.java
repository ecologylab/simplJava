package ecologylab.net;

import java.io.File;
import java.net.URL;

/**
 * Provides callbacks during {@link ecologylab.net.ParsedURL#connect(ConnectionHelper) ParsedURL.connect},
 * to enable filtering and custom processing as the connect operation unfolds.
 *
 * @author andruid
 */
public interface ConnectionHelper
{
	/**
	 * When this method is called, you know the file is a directory.
	 * Process it if you wish.
	 * connect() will return null in this special case.
	 * 
	 * @param file
	 */
	public void		handleFileDirectory(File file);
	/**
	 * Tells the connect() method that it should go ahead and create a PURLConnection
	 * for files that have this suffix.
	 * 
	 * @param suffix
	 * @return
	 */
	public boolean	parseFilesWithSuffix(String suffix);
	
	/**
	 * Called at the end of processing, if it turns out that something went wrong
	 * while opening the connection.
	 */
	public void badResult();
	
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
	public boolean	processRedirect(URL connectionURL) throws Exception;
}
