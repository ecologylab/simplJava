package ecologylab.services.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Date;

import ecologylab.collections.Scope;
import ecologylab.io.Files;
import ecologylab.net.NetTools;
import ecologylab.services.distributed.common.ServerConstants;
import ecologylab.services.messages.ErrorResponse;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.XMLTools;
import ecologylab.xml.XMLTranslationException;
import ecologylab.xml.xml_inherit;

/**
 * Allows the application to send application-specific content to the log, at
 * the beginning of a session. <p/> NB: this class should *never* be extended in
 * an application specific way, because the LoggingServer should never need to
 * know the TranslationSpace for such a super class. What you do extend is the
 * {@link Prologue Prologue} object.
 * 
 * @author andruid
 * @author eunyee
 */
@xml_inherit public class SendPrologue extends LogueMessage
{
	@xml_attribute protected String	date		= new Date(System
																	.currentTimeMillis())
																	.toString();

	@xml_attribute protected String	ip			= NetTools.localHost();

	@xml_attribute protected String	userID	= "0";

	@xml_attribute protected String	studyName;

	public SendPrologue(Logging logging, Prologue prologue)
	{
		super(logging);
		this.date = prologue.date;
		this.ip = prologue.ip;
		this.userID = prologue.userID;
		this.studyName = prologue.getStudyName();
		try
		{
			bufferToLog = prologue.translateToXML((StringBuilder) null);
			bufferToLog.insert(0, beginLog());
			bufferToLog.append(Logging.OP_SEQUENCE_START);
		}
		catch (XMLTranslationException e)
		{
			e.printStackTrace();
		}
	}

	public SendPrologue()
	{
		super();
	}

	public String getFileName()
	{
		String tempDate = date.replace(' ', '_');
		tempDate = tempDate.replace(':', '_');
		/**
		 * A session log file name of a user
		 */
		String sessionLogFile = // "/project/ecologylab/studyResults/CF_LOG/" +
		// "LogFiles/" +
		ip + "__" + tempDate + "_" + userID + ".xml";
		String studyName = getStudyName();
		if (studyName != null)
			sessionLogFile = studyName + Files.sep + sessionLogFile;
		return sessionLogFile;
	}

	public String beginLog()
	{
		return XMLTools.xmlHeader() + "\n<" + logName() + ">\n\n";
	}

	public String getStudyName()
	{
		return studyName;
	}

	/**
	 * First, configure OUTPUT_WRITER, then, in case we have some data, do
	 * super.performService(...) to write it out to the file.
	 */
	@Override public ResponseMessage performService(Scope clientSessionScope)
	{
		String logFilesPath = (String) clientSessionScope
				.get(NIOLoggingServer.LOG_FILES_PATH);

		String fileName = logFilesPath + getFileName();

		try
		{
			File file = new File(fileName);
			String dirPath = file.getParent();
			if (dirPath != null)
			{
				File dir = new File(dirPath);
				if (!dir.exists())
					dir.mkdirs();
			}

			// rename file until we are not overwriting an existing file
			if (file.exists())
			{ // a little weird to do it this way, but the if is cheaper than
				// potentially reallocating the String over and over
				String filename = file.getName();
				int dotIndex = filename.lastIndexOf('.');

				int i = 1;

				while (file.exists())
				{
					String newFilename = (dotIndex > -1 ? filename.substring(0,
							dotIndex)
							+ i + filename.substring(dotIndex) : filename + i);
					
					i++;
					
					// we already took care of the parent directories
					// just need to make a new file w/ a new name
					file = new File(newFilename);
				}
			}
			
			if (!file.createNewFile())
			{
				throw new IOException("Could not create the logging file.");
			}
			
			debug("logging to file at: " + file.getAbsolutePath());

			FileOutputStream fos = new FileOutputStream(file, true);
			CharsetEncoder encoder = Charset.forName(
					ServerConstants.CHARACTER_ENCODING).newEncoder();

			clientSessionScope.put(OUTPUT_STREAM, new OutputStreamWriter(fos,
					encoder));

			return super.performService(clientSessionScope);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();

			return new ErrorResponse(e.getMessage());
		}
		catch (IOException e)
		{
			return new ErrorResponse(e.getMessage());
		}
	}
}
