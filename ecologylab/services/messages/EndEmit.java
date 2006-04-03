package ecologylab.services.messages;

import ecologylab.services.Logging;

/**
 * 
 * Request message about letting the server to write closing block for xml logs 
 * and close the log file. 
 * 
 * @author eunyee
 */
public class EndEmit extends LogRequestMessage
{
	String getMessageString()
	{
		return Logging.LOG_CLOSING;
	}
}