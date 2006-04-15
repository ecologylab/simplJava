package ecologylab.services.logging;


/**
 * 
 * Request message about letting the server to write closing block for xml logs 
 * and close the log file. 
 * 
 * @author eunyee
 */
public class Epilogue extends LogRequestMessage
{
	protected String getMessageString()
	{
		return Logging.LOG_CLOSING;
	}
}