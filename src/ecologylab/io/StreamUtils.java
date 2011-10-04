package ecologylab.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utility methods for operating on streams.
 * 
 * @author blake
 *
 */
public class StreamUtils
{
	public static final void copyFile(File in, File out)
	throws IOException
	{
		copyInputStream(new FileInputStream(in), new FileOutputStream(out));
	}
	/**
	 * Tiny inner class to handle buffer I/O
	 * 
	 * @param in	The inputstream
	 * @param out	The outputstream
	 * @throws IOException	Throws IOException on invalid in or out stream.
	 */
	public static final void copyInputStream(InputStream in, OutputStream out)
	throws IOException
	{
	  //TODO use a buffer pool!
	  byte[] buffer = new byte[1024];
	  int len;
	
	  while((len = in.read(buffer)) >= 0)
	    out.write(buffer, 0, len);
	
	  in.close();
	  out.close();
	}


}
