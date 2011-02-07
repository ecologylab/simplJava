package ecologylab.io;

import java.io.File;
import java.io.FileFilter;

public class XMLFileFilter implements FileFilter
{
	static XMLFileFilter singleton;

	static XMLFileFilter get()
	{
		XMLFileFilter result = singleton;
		if (result == null)
		{
			result	= new XMLFileFilter();
			singleton	= result;
		}
		return result;
	}
	public XMLFileFilter()
	{

	}
	public boolean accept(File file) 
	{
		if (file.isDirectory()) return true;
		String name = file.getName().toLowerCase();
		return name.endsWith(".xml");
	}
}
