package ecologylab.services.logging;

import java.util.Date;

import ecologylab.xml.XmlTools;
import ecologylab.xml.XmlTranslationException;

/**
 * This class should *never* be extended in an application specific way, because the LoggingServer should never
 * need to know the TranslationSpace for such a super class.
 * 
 * @author andruid
 * @author eunyee
 */
public class SendPrologue extends LogueMessage
{
	public Prologue		prologue;
	
	public String	date					= new Date(System.currentTimeMillis()).toString();
	
	public String	ip						= Logging.localHost();
	
	public int 		userID					= 0;

	public SendPrologue(Logging logging, Prologue prologue)
	{
		super(logging);
		this.prologue	= prologue;
		this.date		= prologue.date;
		this.ip			= prologue.ip;
		this.userID		= prologue.userID;
	}

	public SendPrologue()
	{
		super();
	}

	/**
	 * Called only in the context of writing directly to a file, not from the
	 * LoggingService, because in the latter case, the XML for Prologue will be
	 * peeled out of the SendPrologue message using substring().
	 */
	protected String getMessageString()
	{
		try
		{
			return (beginLog() + super.getMessageString() + Logging.OP_SEQUENCE_START);
		} catch (XmlTranslationException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	public String getFileName()
	{
		String tempDate = date.replace(' ','_');
		tempDate = tempDate.replace(':', '_');
		/**
		 * A session log file name of a user
		 */
		String sessionLogFile	=	// "/project/ecologylab/studyResults/CF_LOG/" + 
					"LogFiles/" + 
					ip + "__" + tempDate + "_" + userID + ".xml";
		return sessionLogFile;
	}
 	public String beginLog()
 	{
 		return XmlTools.xmlHeader() + "\n<" + logName() + ">\n\n";
 	}
	
}
