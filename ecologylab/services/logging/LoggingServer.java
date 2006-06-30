package ecologylab.services.logging;

import java.io.IOException;
import java.net.BindException;
import java.net.Socket;

import ecologylab.generic.ObjectRegistry;
import ecologylab.services.ServerToClientConnection;
import ecologylab.services.ServicesHostsAndPorts;
import ecologylab.services.ServicesServer;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.TranslationSpace;

/**
 * creating logging server and set the file for saving debug messages
 * @author eunyee
 */

public class LoggingServer extends ServicesServer
implements LoggingDef
{

	boolean end = false;
	
	public LoggingServer(int portNumber, TranslationSpace nameSpace, ObjectRegistry objectRegistry) 
	throws BindException, IOException 
	{
		super(portNumber, nameSpace, objectRegistry);
		// Let server debug messages print to the file
//		Debug.setLoggingFile(serverLogFile);
	}
	public LoggingServer()
	throws BindException, IOException 
	{
		this(ServicesHostsAndPorts.LOGGING_PORT, TranslationSpace.get("ecologylab.services.logging", "ecologylab.services.logging"),
				null);
	}
	/**
	 * Create a ServerToClientConnection, the object that handles the connection to
	 * each incoming client.
	 * To extend the functionality of the client, you can override this method in your subclass of this,
	 * to return a subclass of ServerToClientConnection.
	 * 
	 * @param incomingSocket
	 * @return
	 * @throws IOException
	 */
	protected ServerToClientConnection getConnection(Socket incomingSocket)
	throws IOException
	{
		return new LoggingServerToClientConnection(incomingSocket, this);
	}
	
	/**
	 * Perform the service associated with a RequestMessage, by calling the
	 * performService() method on that message.
	 * Override the performServices method to do error handling. 
	 * @param requestMessage	Message to perform.
	 * @return					Response to the message.
	 */
	public ResponseMessage performService(RequestMessage requestMessage) 
	{
		if( requestMessage instanceof SendEpilogue)
			end = true;
		
		return requestMessage.performService(objectRegistry);
	}
	
	protected void terminationAction()
	{
		if( !end )
		{
			(new SendEpilogue()).performService(objectRegistry);
		}
	}
	/**
	 * Construct an instance of the LoggingServer.
	 * Handle and report on exceptions that may occur in the process.
	 * 
	 * @return	The LoggingServer instance, or null if exceptions are thrown.
	 */
	public static LoggingServer get()
	{
		LoggingServer loggingServer	= null;
		try
		{
			loggingServer = new LoggingServer();
		} catch (BindException e)
		{
			println("LoggingServer ERROR binding to port during initialization: " + e);
			e.printStackTrace();
		} catch (IOException e)
		{
			println("LoggingServer ERROR during initialization: " + e);
			e.printStackTrace();
		}
		return loggingServer;
	}
	public static void main(String args[])
	{
		LoggingServer loggingServer	= get();
		if (loggingServer != null)
			loggingServer.start();
	}
}