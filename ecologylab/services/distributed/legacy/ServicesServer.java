package ecologylab.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.ElementState;
import ecologylab.xml.NameSpace;

/**
 * Generic service server. Accepts TCP/IP messages and acknowleges with responses.
 * Extend this to provide concentrated services.
 * 
 * @author blake
 */
public abstract class ServicesServer extends Thread
{
	private int portNumber;
	private ServerSocket socketServer;
	
	public ServicesServer(int portNumber)
	{
		this.portNumber = portNumber;
	}
	
	abstract public ResponseMessage performService(RequestMessage requestMessage);
	abstract protected String getPackageName();
	abstract protected String getMessagePackageName();
	
	public void run()
	{
		while (true)
		{
			try 
			{
			   System.out.println("ServicesServer: Listening for a connection on port " + portNumber);
			   socketServer = new ServerSocket(portNumber);
			   while (true) 
			   {
				   ElementState.setDefaultPackageName(getPackageName());
				   
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
					   String str = reader.readLine();
					   
					   ElementState.setDefaultPackageName(getMessagePackageName());
					   
					   RequestMessage requestMessage
								= (RequestMessage) ElementState.translateFromXMLString(str);
					   
					   ElementState.setDefaultPackageName(getPackageName());
					   
					   //perform the service being requested
					   ResponseMessage responseMessage = performService(requestMessage);
					   
					   //send the response
					   out.println(responseMessage.translateToXML(false));
					   out.flush();
				   }
			   }
			}
			catch (Exception e)
			{
				//incomingSocket.close();
				
				System.out.println("ServicesServer error: couldn't bind to port " + portNumber);
				e.printStackTrace();
			}
		}
	}
}
