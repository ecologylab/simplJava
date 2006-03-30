package ecologylab.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import ecologylab.generic.Debug;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.XmlTranslationException;

/**
 * Interface Ecology Lab Distributed Computing Services framework<p/>
 * 
 * Runs the connection from the server to a client.
 * 
 * @author andruid
 * @author zach
 * @author eunyee
 */
public class ServerToClientConnection extends Debug
implements Runnable
{
	BufferedReader		inputStreamReader;
	PrintStream			outputStreamWriter;
	
	ServicesServer		servicesServer;
	Socket				incomingSocket;
	
	boolean				running	= true;
	
	public ServerToClientConnection(Socket incomingSocket, ServicesServer servicesServer)
	throws IOException
	{
		this.incomingSocket	= incomingSocket;
		
		inputStreamReader	= new BufferedReader(new InputStreamReader(incomingSocket.getInputStream()));
		
		outputStreamWriter	= new PrintStream(incomingSocket.getOutputStream());
		
		this.servicesServer	= servicesServer;
	}
	public String toString()
	{
		return super.getClassName() + "[" + incomingSocket + "]";
	}
	/**
	 * Service the client connection.
	 */
	public void run()
	{
		while (running) 
		{
			//debug("waiting for packet");
			//get the packet message
			String messageString	= "";
			try
			{
				//TODO -- change to nio
				messageString = inputStreamReader.readLine();
				
				//debugA("got raw message: " + messageString);
				
				RequestMessage requestMessage = servicesServer.translateXMLStringToRequestMessage(messageString);
				
				if (requestMessage == null)
					debug("ERROR: translation failed: " + messageString);
				else
				{
					//perform the service being requested
					ResponseMessage responseMessage = servicesServer.performService(requestMessage);
					
					//send the response
					outputStreamWriter.println(responseMessage.translateToXML(false));
					outputStreamWriter.flush();
				}
			} catch (java.net.SocketException e)
			{
				// this seems to mean the connection went away
				if (outputStreamWriter != null) // dont need the message if we're already shutting down
					debug("STOPPING:  It seems we are no longer connected to the client.");
				break;
			} catch (IOException e)
			{
				// TODO count streak of errors and break;
				debug("IO ERROR: " + e.getMessage());
				e.printStackTrace();
			}
			catch (XmlTranslationException e)
			{
				// report error on XML passed through the socket
				debug("Bogus Message ERROR: " + messageString);
				e.printStackTrace();
			}
		}
		synchronized (this)
		{
			if (running)
				stop();
		}
	}
	public synchronized void stop()
	{
		running	= false;
		debug("stopping.");
		try
		{
			if (outputStreamWriter != null)
			{
				outputStreamWriter.close();
				//debug("writer is closed.");
				outputStreamWriter	= null;
			}
			if (inputStreamReader != null)
			{
				inputStreamReader.close();
				//debug("reader is closed.");
				inputStreamReader	= null;
			}
		} catch (IOException e)
		{
			debug("while closing reader & writer: " + e.getMessage());
		}
		servicesServer.connectionTerminated(this);
	}
}
