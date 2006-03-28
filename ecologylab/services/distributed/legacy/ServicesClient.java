package ecologylab.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

import ecologylab.generic.Debug;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.services.messages.ResponseTypes;
import ecologylab.xml.NameSpace;

/**
 * Client to connect to ServicesServer.
 * 
 * @author blake
 */
public class ServicesClient
extends Debug
implements ResponseTypes
{
	private Socket 		sock;
	BufferedReader 		reader;
	PrintStream 		output;
	
	private int 		port;
	private String		server;
	private NameSpace	messageSpace = null;
	
	/**
	 * Create a client that will connect on the provided port. Assume localhost
	 * 
	 * @param port The localhost port to connect.
	 */
	public ServicesClient(int port)
	{
		this(port, null);
	}
	
	
	public ServicesClient(int port, NameSpace messageSpace)
	{
		this("localhost", port, messageSpace);
	}
	
	public ServicesClient(String server, int port)
	{
		this("localhost", port, null);
	}
	
	public ServicesClient(String server, int port, NameSpace messageSpace)
	{
		this.port			= port;
		this.server 		= server;
		this.messageSpace 	= messageSpace;
	}
	
	private void init()
	{
		InetAddress address = null;
		try 
		{
			//address = InetAddress.getLocalHost();
			//get the address and connect
			address 	= InetAddress.getByName(server);
			sock 		= new Socket(address, port);
			reader 		= new BufferedReader(new InputStreamReader(sock.getInputStream()));
			output		= new PrintStream(sock.getOutputStream()); 
		}
		catch (Exception e)
		{
			//System.err.println("No server: " + server);
			//e.printStackTrace();
			//System.exit(2);
			sock = null;
		}
	}
	
	/**
	 * Determine if we are connected.
	 * 
	 * @return True if connected, false if not.
	 */
	public boolean connected()
	{
		return (sock != null);
	}
	
	/**
	 * Connect to the server (if not already connected). Return connection status.
	 * 
	 * @return True if connected, false if not.
	 */
	public boolean connect()
	{
		if (connected())
			return true;
		
		init();
		if (connected())
			return true;
		
		return false;
	}
	
	public void sendMessage(RequestMessage requestMessage)
	{
		if (!connected())
			init();
		
		boolean transactionComplete = false;
		while (!transactionComplete)
		{
			String message		= null;
			try
			{
				message = requestMessage.translateToXML(false);
				output.println(message);
			
				System.out.println("Services Client: just sent message: " + message);
				String response;
				
				System.out.println("Services Client: awaiting a response");
				response = reader.readLine();
				
				//ConsoleUtils.obtrusiveConsoleOutput("RESPONSE: " + response);
				
				ResponseMessage responseMessage;
				
				if (messageSpace != null)
					responseMessage = (ResponseMessage)
						ResponseMessage.translateFromXMLString(response, messageSpace);
				else
					responseMessage = (ResponseMessage)
						ResponseMessage.translateFromXMLString(response);
				
				transactionComplete = true;
				if (responseMessage.response.equals(BAD))
				{
					Debug.println("Received BAD response to message: " + message);
				}
			}
			catch (Exception e)
			{
				System.out.println("Failed sending request message, resending...");
			}
		}
	}
}
