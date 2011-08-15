package ecologylab.net;

import java.io.File;

/**
 * Provides callbacks during {@link ecologylab.net.ParsedURL#connect(ConnectionHelper) ParsedURL.connect},
 * to enable filtering and custom processing as the connect operation unfolds.
 *
 * @author andruid
 */
public interface ConnectionHelper extends ConnectionHelperJustRemote
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
	 * @return	true if files with this suffix should be parsed; false if they should be ignored.
	 */
	public boolean	parseFilesWithSuffix(String suffix);
	
}
