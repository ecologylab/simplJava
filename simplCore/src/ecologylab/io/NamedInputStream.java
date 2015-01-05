package ecologylab.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Bundle a nice printable string for error messages with an InputStream.
 * 
 * @author andruid
 */
public class NamedInputStream 
{
	final String			name;
	final InputStream	inputStream;

	public NamedInputStream(String name, InputStream stream)
	{
		this.name					= name;
		this.inputStream	= stream;
	}
	public NamedInputStream(File file) throws FileNotFoundException
	{
		this.name					= file.getName();
		this.inputStream	= new FileInputStream(file);
	}
	public String getName()
	{
		return name;
	}

	public InputStream getInputStream()
	{
		return inputStream;
	}
}
