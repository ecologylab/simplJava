package ecologylab.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ecologylab.generic.Debug;

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
	
	/**
	 * this method uses the <i>local file header signature</i> to detect if an input stream is a zip
	 * stream. for zip files this header signature is 50 4B 03 04. note that this is not 100%
	 * accuracy: the stream may just happen to start with this signature, or it may be corrupted.
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static boolean isZipStream(InputStream in) throws IOException
	{
		if (!in.markSupported())
		{
			Debug.error(StreamUtils.class,
					"isZipStream(): mark/reset not supported for this input stream: cannot detect if this is a zip stream!");
			return false;
		}
		in.mark(4);
		byte[] sig = new byte[4];
		in.read(sig);
		in.reset();
		if (sig[0] == 0x50 && sig[1] == 0x4b && sig[2] == 0x03 && sig[3] == 0x04)
			return true;
		return false;
	}

}
