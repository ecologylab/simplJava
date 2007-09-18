/**
 * 
 */
package ecologylab.services.logging.playback;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Log files should be XML files; this file filter only displays files whose filetype is XML.
 * 
 * @author Zach Toups
 */
public class LogFileFilter extends FileFilter
{
    
    /**
     * 
     */
    public LogFileFilter()
    {
        super();
    }

    /**
     * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
     */
    public boolean accept(File arg0)
    {
        if (arg0.isDirectory())
            return true;
        
        String fileName = arg0.toString();
        return ("xml".equalsIgnoreCase(fileName.substring(fileName.lastIndexOf('.')+1)));
    }

    /**
     * @see javax.swing.filechooser.FileFilter#getDescription()
     */
    public String getDescription()
    {
        return "Log files (.xml)";
    }
}
