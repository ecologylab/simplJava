package ecologylab.services.logging;


/**
 * request message for the Logging server to open new log file
 * and write the header.
 * 
 * @author eunyee
 */
public class BeginEmit extends LogRequestMessage
{

	String getMessageString()
	{
		return Logging.LOG_HEADER;
	}
	
}