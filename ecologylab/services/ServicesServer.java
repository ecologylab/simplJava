package ecologylab.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import ecologylab.generic.Debug;
import ecologylab.generic.Generic;
import ecologylab.generic.ObjectRegistry;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.ElementState;
import ecologylab.xml.NameSpace;
import ecologylab.xml.XmlTranslationException;

/**
 * Generic service server. Accepts TCP/IP messages and acknowleges with responses.
 * Extend this to provide concentrated services.
 * 
 * @author blake
 * @author andruid
 */
public class ServicesServer extends Debug
implements Runnable
{
	private int portNumber;
	private ServerSocket socketServer;
	
	boolean			finished;
	
	Thread			thread;
	
	/**
	 * Space that defines mappings between xml names, and Java class names,
	 * for request messages.
	 */
	NameSpace		requestTranslationSpace;
	
	/**
	 * Provides a context for request processing.
	 */
	ObjectRegistry	objectRegistry;

	/**
	 * Create a services server, that listens on the specified port, and
	 * uses the specified TranslationSpaces for operating on messages.
	 * 
	 * @param portNumber
	 * @param requestTranslationSpace
	 * @param objectRegistry Provides a context for request processing.
	 */
	public ServicesServer(int portNumber,
						  NameSpace requestTranslationSpace, ObjectRegistry objectRegistry)
	{
		this.portNumber 				= portNumber;
		this.requestTranslationSpace	= requestTranslationSpace;
		if (objectRegistry == null)
			objectRegistry				= new ObjectRegistry();
		this.objectRegistry				= objectRegistry;
	}

	/**
	 * Perform the service associated with a RequestMessage, by calling the
	 * performService() method on that message.
	 * 
	 * @param requestMessage	Message to perform.
	 * @return					Response to the message.
	 */
	public ResponseMessage performService(RequestMessage requestMessage) 
	{
		return requestMessage.performService(objectRegistry);
	}
	
	public void run()
	{
		while (!finished)
		{
			String messageString	= "";
			try 
			{
			   System.out.println("ServicesServer: Listening for a connection on port " + portNumber);
			   socketServer = new ServerSocket(portNumber);
			   while (true) 
			   {
				   Socket incomingSocket = socketServer.accept();
				   BufferedReader reader =
			   	  		new BufferedReader(new InputStreamReader(incomingSocket.getInputStream()));
				
				   PrintStream out =
			   	  		new PrintStream(incomingSocket.getOutputStream());
				
				   //boolean done = false;
				   while (true) 
				   {
						//System.out.println("waiting for actual packet");
					   //get the packet message
					   messageString = reader.readLine();
					   
					   //println("ServicesServer got raw message: " + messageString);
					   
					   RequestMessage requestMessage
								= (RequestMessage) ElementState.translateFromXMLString(messageString, requestTranslationSpace);
					   
					   //perform the service being requested
					   ResponseMessage responseMessage = performService(requestMessage);
					   
					   //send the response
					   out.println(responseMessage.translateToXML(false));
					   out.flush();
				   }
			   }
			}
			catch (java.net.BindException be)
			{
				Debug.println("ServicesServer ERROR: can't bind to port " + portNumber
						+" cause its already in use. Quitting!");
				return;
			}
			catch (XmlTranslationException te)
			{
				Debug.println("ERROR translating from XML: " + messageString);
				te.printStackTrace();
			}
			catch (Exception e)
			{
				//incomingSocket.close();
				
				System.out.println("ServicesServer error while processing requests. ");
				e.printStackTrace();
				Generic.sleep(1000);
			}
		}
	}
	/**
	 * Start the ServicesServer, at the specified priority.
	 * @param priority
	 */
	public synchronized void start(int priority)
	{
		if (thread == null)
		{
			Thread t	= new Thread(this, "Services Server");
			t.setPriority(priority);
			thread		= t;
			t.start();
		}
	}
	public synchronized void stop()
	{
		if (thread != null)
		{
			finished	= true;
			thread		= null;
		}
	}
	public void start()
	{
		start(Thread.NORM_PRIORITY);
	}
}
