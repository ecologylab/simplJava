package ecologylab.services.logging;

import java.io.IOException;
import java.net.BindException;
import java.net.Socket;

import ecologylab.generic.Debug;
import ecologylab.generic.ObjectRegistry;
import ecologylab.services.ServerToClientConnection;
import ecologylab.services.ServicesServer;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.NameSpace;

/**
 * creating logging server and set the file for saving debug messages
 * @author eunyee
 */

public class LoggingServer extends ServicesServer
implements LoggingDef
{

	boolean end = false;
	
	public LoggingServer(int portNumber, NameSpace nameSpace, ObjectRegistry objectRegistry) 
	throws BindException, IOException 
	{
		super(portNumber, nameSpace, objectRegistry);
		// Let server debug messages print to the file
//		Debug.setLoggingFile(serverLogFile);
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
}