package ecologylab.services.logging;

import java.util.Date;

import ecologylab.xml.XmlTranslationException;
import ecologylab.xml.XmlTools;


/**
 * request message for the Logging server to open new log file
 * and write the header.
 * 
 * @author eunyee
 */
public class Prologue extends LogueMessage
{
	public String	date					= new Date(System.currentTimeMillis()).toString();
	
	public String	ip						= Logging.localHost();
	
	public String	logName;
	
	public int 		userID					= 0;
	
	/**
	 * Constructor for building from the Logging class.
	 * @param logging
	 */
	public Prologue(Logging logging)
	{
		super(logging);
	}
	/*
	 * Constructor for automatic translation;
	 */
	public Prologue()
	{
		super();
	}
	
	String getMessageString()
	{
		try
		{
			return (beginLog() + this.translateToXML(false) + Logging.OP_SEQUENCE_START);
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
					ip + "__" + tempDate + ".xml";
		return sessionLogFile;
	}
	
	public void setUserID(int id)
	{
		this.userID = id;
	}

 	public String beginLog()
 	{
 		return XmlTools.xmlHeader() + "\n<" + logName() + ">\n\n";
 	}
}