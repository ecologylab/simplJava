package ecologylab.services;

import java.io.IOException;
import java.net.BindException;
import java.net.Socket;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.xml.TranslationSpace;

/**
 * 
 * @author eunyee
 * 
 */
public class HTTPPostServer extends ServicesServer
{
	private static int postServerPort = 10200;
	
	public HTTPPostServer(int portNumber, TranslationSpace requestTranslationSpace, ObjectRegistry objectRegistry) 
		throws IOException, BindException 
	{
		super(portNumber, ServicesServer.HTTP_POST_SERVER, requestTranslationSpace, objectRegistry);
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
		return new HTTPPostServerToClientConnection(incomingSocket, this);
	}

	/**
	 * Construct an instance of the TestDataServer.
	 * Handle and report on exceptions that may occur in the process.
	 * 
	 * @return	The TestDataServer instance, or null if exceptions are thrown.
	 */
	protected static HTTPPostServer get(int portNumber, TranslationSpace requestTranslationSpace, ObjectRegistry objectRegistry)
	{
		HTTPPostServer httpPostServer	= null;
		try
		{
			httpPostServer = new HTTPPostServer(portNumber, requestTranslationSpace, objectRegistry);
		} catch (BindException e)
		{
			println("LoggingServer ERROR binding to port during initialization: " + e);
			e.printStackTrace();
		} catch (IOException e)
		{
			println("LoggingServer ERROR during initialization: " + e);
			e.printStackTrace();
		}
		return httpPostServer;
	}
	
	public static void main(String args[])
	{
		HTTPPostServer validateionTestServer	= get(postServerPort, TranslationSpace.get("validateMessage", "testServer"), null);
		
		if (validateionTestServer != null)
		{			
			validateionTestServer.start();
		}
	}

}