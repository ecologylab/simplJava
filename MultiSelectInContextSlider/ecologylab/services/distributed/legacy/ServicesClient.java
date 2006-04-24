package ecologylab.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.BindException;
import java.net.InetAddress;
import java.net.PortUnreachableException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import ecologylab.generic.Debug;
import ecologylab.generic.Generic;
import ecologylab.generic.ObjectRegistry;
import ecologylab.services.logging.LoggingDef;
import ecologylab.services.logging.LoggingServerToClientConnection;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.NameSpace;

/**
 * Interface Ecology Lab Distributed Computing Services framework<p/>
 * 
 * Client to connect to ServicesServer.
 * 
 * @author blake
 * @author andruid
 */
public class ServicesClient
extends Debug
{
	private final static int CONNECTION_RETRY_SLEEP_INTERVAL	=	250;
	
	private Socket 		sock;
	BufferedReader 		reader;
	PrintStream 		output;
	
	private int 		port;
	private String		server;
	private NameSpace	translationSpace = null;
	
	protected ObjectRegistry	objectRegistry;
	
	private String 		toString;
	
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
		this(server, port, messageSpace, null);
	}
	public ServicesClient(String server, int port, NameSpace messageSpace, ObjectRegistry objectRegistry)
	{
		this.port			= port;
		this.server 		= server;
		this.translationSpace 	= messageSpace;
		
		if (objectRegistry == null)
			objectRegistry	= new ObjectRegistry();
		this.objectRegistry	= objectRegistry;
	}
	
	private boolean createConnection()
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
		} catch (BindException e)
		{
			debug("Couldnt create socket connection to server '" + server + "': " + e);
			//e.printStackTrace();
			sock = null;
		} catch (PortUnreachableException e)
		{
			debug("Server is alive, but has no daemon on port " + port + ": " + e);
			//e.printStackTrace();
			sock = null;
		} catch (SocketException e)
		{
			debug("Server '" + server + "' unreachable: " + e);
		}
		catch (IOException e)
		{
			debug("Bad response from server: " + e);
			//e.printStackTrace();
			sock = null;
		}
		return sock != null;
	}
	
	public String toString()
	{
		String toString	= this.toString;
		if (toString == null)
		{
			toString		= this.getClassName() + "[" + server + ": " + port + "]";
			this.toString	= toString;
		}
		return toString;
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
		
		return createConnection();
	}
    
    /**
     * Disconnect from the server (if connected).
     */
    public void disconnect()
    {
        if (connected())
        {
            try
            {
                sock.close();
                System.out.println("Closed the connection.");
                sock = null;
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        } else
        {
            System.out.println("Could not close connection: not connected!");
        }
    }
    
	/**
	 * Send a message to the ServicesServer to get a service performed,
	 * 
	 * @param requestMessage
	 * @return	The ResponseMessage from the server.
	 * 			This could be null, which means that communication with the server failed.
	 * 			Reasons for failure include:
	 * 			1) IOException: the socket connection broke somehow.
	 * 			2) XmlTranslationException: The message was malformed or 
	 * 			   translation failed strangely.
	 */
	public ResponseMessage sendMessage(RequestMessage requestMessage)
	{
		if (!connected())
			createConnection();
		
		ResponseMessage responseMessage	= null;
		boolean transactionComplete = false;
		int badTransmissionCount = 0;
		while (!transactionComplete)
		{
			String requestMessageXML		= null;
			try
			{
				requestMessageXML = requestMessage.translateToXML(false);
				
				if( requestMessageXML.getBytes().length > ServerToClientConnection.MAX_PACKET_SIZE )
				{
					debug("requestMessage is Bigger than accetable server size \n CANNOT SEND : " + requestMessageXML);
					break;
				}

				output.println(requestMessageXML);
			
				if (show(5))
					debug("Services Client: just sent message: " + requestMessageXML);
				String response;
				
				if (show(5))
					debug("Services Client: awaiting a response");
				response = reader.readLine();
				
				responseMessage = (ResponseMessage)
						ResponseMessage.translateFromXMLString(response, translationSpace);
				
//				if (responseMessage.response.equals(BADTransmission))
				if (responseMessage instanceof ServerToClientConnection.BadTransmissionResponse)
				{
					badTransmissionCount++;
					if( badTransmissionCount == 3 )
					{
						debug("ERROR: Quitting sending to the server because of the network condition after " +
								badTransmissionCount + " times try ");
						break;
					}
					else
						debug("ERROR: BADTransmission of: " + requestMessageXML + "\n\t Resending.");
						
				}
				else
				{
					if (show(5))
						debug("received response: " + response);
					processResponse(responseMessage);
					transactionComplete = true;
				}
			}
			catch (Exception e)
			{
				debug("ERROR: Failed sending " + requestMessage + ": " + e);
				transactionComplete	= true;
			}
		}
		return responseMessage;
	}

	protected void processResponse(ResponseMessage responseMessage)
	{
		responseMessage.processResponse(objectRegistry);
	}
	
	/**
	 * Try and connect to the server. If we fail, wait CONNECTION_RETRY_SLEEP_INTERVAL
	 * and try again. Repeat ad nauseum.
	 */
	public void waitForConnect()
	{
		System.out.println("Waiting for a server on port " + port);
		while (!connect())
		{
			//try again soon
			Generic.sleep(CONNECTION_RETRY_SLEEP_INTERVAL);
		}
	}
	
	public boolean isServerRunning()
	{
		boolean serverIsRunning = createConnection();
		//we're just checking, don't keep the connection
		disconnect();
		return serverIsRunning;
	}


    /**
     * @return Returns the server.
     */
    public String getServer()
    {
        return server;
    }


    /**
     * @param server The server to set.
     */
    public void setServer(String server)
    {
        this.server = server;
    }
}
