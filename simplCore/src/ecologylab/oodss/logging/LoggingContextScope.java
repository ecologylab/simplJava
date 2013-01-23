/**
 * 
 */
package ecologylab.oodss.logging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Map;

import ecologylab.collections.Scope;
import ecologylab.generic.Debug;
import ecologylab.oodss.distributed.common.NetworkingConstants;

/**
 * @author Zachary O. Toups (zach@ecologylab.net)
 * 
 */
public class LoggingContextScope<T> extends Scope<T>
{
	protected Writer	outputStreamWriter;

	public LoggingContextScope(Map<String, T> parent)
	{
		super(parent);
	}

	public synchronized void setUpOutputStreamWriter(String logFileName) throws IOException
	{
		String logFilesPath = (String) this.get(NIOLoggingServer.LOG_FILES_PATH);

		String fileName = logFilesPath + logFileName;

		File file = new File(fileName);
		String dirPath = file.getParent();
		if (dirPath != null)
		{
			Debug.println("dirPath = " + dirPath);

			File dir = new File(dirPath);
			if (!dir.exists())
			{
				Debug.println("dirPath did not exist. create with mkdirs()");
				dir.mkdirs();
			}
		}

		Debug.println("attempting to use filename: " + fileName);
		// rename file until we are not overwriting an existing file
		if (file.exists())
		{ // a little weird to do it this way, but the if is cheaper than
			// potentially reallocating the String over and over
			String filename = file.getName();
			int dotIndex = filename.lastIndexOf('.');
			int i = 1;

			do
			{
				Debug.println(filename + " already exists.");

				String newFilename = (dotIndex > -1 ? filename.substring(0, dotIndex)
						+ i
						+ filename.substring(dotIndex) : filename + i);
				Debug.println("trying new filename = " + newFilename);
				i++;

				// we already took care of the parent directories
				// just need to make a new file w/ a new name
				file = new File(newFilename);
			}
			while (file.exists());
		}

		if (!file.createNewFile())
		{
			throw new IOException("Could not create the logging file.");
		}

		Debug.println("Logging to file at: " + file.getAbsolutePath());

		FileOutputStream fos = new FileOutputStream(file, true);
		CharsetEncoder encoder = Charset.forName(NetworkingConstants.CHARACTER_ENCODING).newEncoder();

		outputStreamWriter = new OutputStreamWriter(fos, encoder);
	}

	/**
	 * @return the outputStreamWriter
	 */
	public Writer getOutputStreamWriter()
	{
		return outputStreamWriter;
	}

	/**
	 * Sets outputStreamWriter to null so that logging server can try to shut down.
	 * 
	 * TODO Should handle semantics of shutting down output stream.
	 */
	public synchronized void shutdown()
	{
		this.outputStreamWriter = null;
	}
}
