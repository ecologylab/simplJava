package ecologylab.services.logging;

import ecologylab.xml.XmlTools;
import ecologylab.xml.XmlTranslationException;


/**
 * 
 * Request message about letting the server to write closing block for xml logs 
 * and close the log file. 
 * 
 * @author eunyee
 */
public class Epilogue extends LogueMessage
{
	/**
	 * Constructor for building from the Logging class.
	 * @param logging
	 */
	public Epilogue(Logging logging)
	{
		logName			= XmlTools.getXmlTagName(logging.getClass(), "State", false);
	}
	/*
	 * Constructor for automatic translation;
	 */
	public Epilogue()
	{
		super();
	}
	/*
	String getMessageString()
	{
		try
		{
			//TODO Eunyee
			return (Logging.OP_SEQUENCE_END + super.getMessageString() + endLog());
		} catch (XmlTranslationException e) 
		{
			e.printStackTrace();
			return null;
		}
	}*/
	
	public String endLog()
 	{
 		return Logging.OP_SEQUENCE_END + "</" + logName() + ">";
 	}

}
