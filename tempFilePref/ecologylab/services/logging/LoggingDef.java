package ecologylab.services.logging;



/**
 * Logging server and port definitions
 * 
 * @author eunyee
 *
 */
public interface LoggingDef
{
	/**
	 * We use loggin server hosted on csdll.cs.tamu.edu
	 */
/**
	 * Logging file of the server's debug messages
	 */
	static String serverLogFile 	= // "/project/ecologylab/studyResults/CF_LOG/" + 
				"stdout.log";
	
	static String keyStringForFileObject = "key";
}