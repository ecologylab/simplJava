package ecologylab.oodss.logging;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import ecologylab.appframework.types.prefs.Pref;
import ecologylab.io.Files;
import ecologylab.net.NetTools;
import ecologylab.oodss.messages.ErrorResponse;
import ecologylab.oodss.messages.ResponseMessage;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.XMLTools;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.formatenums.StringFormat;

/**
 * Allows the application to send application-specific content to the log, at the beginning of a
 * session.
 * <p/>
 * NB: this class should *never* be extended in an application specific way, because the
 * LoggingServer should never need to know the TranslationSpace for such a super class. What you do
 * extend is the {@link Prologue Prologue} object.
 * 
 * @author andruid
 * @author eunyee
 */
@simpl_inherit
public final class SendPrologue extends LogueMessage
{
	@simpl_scalar
	protected String	date		= new Date(System.currentTimeMillis()).toString();

	@simpl_scalar
	protected String	ip			= NetTools.localHost();

	@simpl_scalar
	protected String	userID	= "0";

	@simpl_scalar
	protected String	studyName;

	@simpl_scalar
	protected boolean	performEpilogueNow;

	public SendPrologue(Logging logging, Prologue prologue)
	{
		super(logging);
		this.date = prologue.date;
		this.ip = prologue.ip;
		this.userID = prologue.userID;
		this.studyName = prologue.getStudyName();
		try
		{
			bufferToLog = SimplTypesScope.serialize(prologue, StringFormat.XML);//prologue.serialize((StringBuilder) null);
			bufferToLog.insert(0, beginLog());
			bufferToLog.append(Logging.OP_SEQUENCE_START);
		}
		catch (SIMPLTranslationException e)
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

	public static String constructFileName()
	{
		return getFileName(new Date(), Pref.lookupString(Prologue.STUDY_NAME));
	}

	public static String getFileName(Date date, String studyName)
	{
		String tempDate = date.toString().replace(' ', '_');
		tempDate = tempDate.replace(':', '_');

		String ip = NetTools.localHost();
		/**
		 * A session log file name of a user
		 */
		String sessionLogFile = // "/project/ecologylab/studyResults/CF_LOG/" +
		// "LogFiles/" +
		ip + "__" + tempDate + "_" + Pref.lookupString("uid", "No UID") + ".xml";

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
	 * First, configure OUTPUT_WRITER, then, in case we have some data, do super.performService(...)
	 * to write it out to the file.
	 */
	@Override
	public ResponseMessage performService(LoggingContextScope contextScope)
	{
		try
		{
			contextScope.setUpOutputStreamWriter(this.getFileName());

			if (this.performEpilogueNow)
			{
				SendEpilogue se = new SendEpilogue();
				se.bufferToLog = this.bufferToLog;
				// this will write the buffer and then close the file
				return se.performService(contextScope);
			}
			else
				return super.performService(contextScope);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();

			return new ErrorResponse(e.getMessage());
		}
		catch (IOException e)
		{
			e.printStackTrace();

			return new ErrorResponse(e.getMessage());
		}
	}
}
