package ecologylab.services;

import java.io.IOException;
import java.net.BindException;

import ecologylab.generic.Debug;
import ecologylab.generic.ObjectRegistry;
import ecologylab.xml.NameSpace;

/**
 * 
 * @author eunyee
 *
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