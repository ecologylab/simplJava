/**
 * 
 */
package ecologylab.serialization;

import java.io.File;
import java.io.FilenameFilter;

/**
 * A filename filter for XML files, based on their <b>final</b> extension.
 * 
 * @author Zachary O. Toups (toupsz@ecologylab.net)
 */
public class XMLFilenameFilter implements FilenameFilter
{
	public final static XMLFilenameFilter	staticInstance	= new XMLFilenameFilter();

	/**
	 * 
	 */
	public XMLFilenameFilter()
	{
	}

	/**
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 * 
	 * Matches files whose <b>final</b> filename extension is ".xml" (case
	 * ignored). This will not, for example, match "foo.xml.bar", but will match
	 * "foo.bar.xml".
	 */
	@Override
	public boolean accept(File dir, String name)
	{
		int dot = name.lastIndexOf('.');

		if (dot == -1)
		{
			return false;
		}

		String suffix = name.substring(dot + 1);
		return "xml".equalsIgnoreCase(suffix);
	}
}
