package ecologylab.services.logging;

import java.util.Date;

import ecologylab.xml.XmlTranslationException;


/**
 * request message for the Logging server to open new log file
 * and write the header.
 * 
 * @author eunyee
 */
public class Prologue extends LogRequestMessage
{
	public String	date					= new Date(System.currentTimeMillis()).toString();
	
	public String	ip						= Logging.localHost();
	
	public int 		userID					= 0;
	
	String getMessageString()
	{
		try {
			return (Logging.BEGIN_EMIT + this.translateToXML(false) + Logging.OP_SEQUENCE_START);
		} catch (XmlTranslationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
}