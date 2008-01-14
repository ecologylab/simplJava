/**
 * 
 */
package ecologylab.services.logging.playback;

import java.io.File;

/**
 * Log files should be XML files; this file filter only displays files whose filetype is XML.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public class LogFileFilter extends javax.swing.filechooser.FileFilter implements java.io.FileFilter
{
	public static LogFileFilter staticInstance = new LogFileFilter();
	
	/**
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	@Override public boolean accept(File arg0)
	{
		if (arg0.isDirectory())
			return true;

		String fileName = arg0.toString();
		return ("xml".equalsIgnoreCase(fileName.substring(fileName.lastIndexOf('.') + 1)));
	}

	/**
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	@Override public String getDescription()
	{
		return "Log files (.xml)";
	}
}
