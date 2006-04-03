package ecologylab.services.logging;

import java.io.IOException;
import java.net.BindException;

import ecologylab.generic.Debug;
import ecologylab.generic.ObjectRegistry;
import ecologylab.services.ServicesServer;
import ecologylab.xml.NameSpace;

/**
 * creating logging server and set the file for saving debug messages
 * @author eunyee
 */

public class LoggingServer extends ServicesServer
implements LoggingDef
{

	public LoggingServer(int portNumber, NameSpace nameSpace, ObjectRegistry objectRegistry) 
	throws BindException, IOException 
	{
		super(portNumber, nameSpace, objectRegistry);
		// Let server debug messages print to the file
		Debug.setLoggingFile(serverLogFile);
	}
	
}