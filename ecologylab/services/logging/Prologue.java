package ecologylab.services.logging;

import java.util.Date;


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

	String getMessageString()
	{
		return Logging.LOG_HEADER;
	}
	
}