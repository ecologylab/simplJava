package ecologylab.services.logging;

import java.net.InetAddress;


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
//	static final String loggingServer = "128.194.147.58";
	static final String loggingServer = Logging.localHost();
	static final int port = 10000;
	
	/**
	 * we set the maximum size of logging message as a 16kbyte
	 */
	static final int maxSize = 60000; 
	
	/**
	 * Logging file of the server's debug messages
	 */
	static String serverLogFile 	= // "/project/ecologylab/studyResults/CF_LOG/" + 
				"stdout.log";
	
	static String keyStringForFileObject = "key";
}