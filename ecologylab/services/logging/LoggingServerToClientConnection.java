package ecologylab.services.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

import ecologylab.services.ServerToClientConnection;
import ecologylab.services.ServicesServer;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.XmlTranslationException;

/**
 * 
 * @author eunyee
 *
 */
public class LoggingServerToClientConnection extends ServerToClientConnection
{
	FileOutputStream outputStream;
	
	public LoggingServerToClientConnection(Socket incomingSocket, ServicesServer servicesServer) 
	throws IOException 
	{
		super(incomingSocket, servicesServer);
	}
	
	/**
	 * Perform the service specified by the request method.
	 * The default implementation, here, simply passes the message to the servicesServer,
	 * which is keeping an objectRegistry context, and does the perform.
	 * <p/>
	 * This routine is abstracted out here, so that customized Servers can do thread/connection
	 * specific custom processing in this method, as needed, by overriding the definition.
	 * 
	 * @param requestMessage
	 * @return
	 */
	protected ResponseMessage performService(RequestMessage requestMessage)
	{
		if( requestMessage instanceof SendPrologue )
		{
			String name = ((SendPrologue)requestMessage).getFileName();
			getFile(name);
//			servicesServer.getObjectRegistry().registerObject(LoggingDef.keyStringForFileObject, getFile(name) );
		}
		else if( outputStream == null )
		{
			debug("Prologue has not been received OR File has not been created!! " + requestMessage);
		}
		
		if( (outputStream != null) && ( requestMessage instanceof LogRequestMessage) )
		{
			((LogRequestMessage)requestMessage).setOutputStream(outputStream);
		}
		ResponseMessage responseMessage = servicesServer.performService(requestMessage);
		return responseMessage;
	}
	
	FileOutputStream getFile(String fileName)
	{
		if( outputStream == null )
		{
			try {
				File file	= new File(fileName);
				String dirPath	= file.getParent();
				File dir	= new File(dirPath);
				if (!dir.exists())
					dir.mkdirs();
				//TODO what if (file.exists()) ???
				outputStream = new FileOutputStream(file, true);
				return outputStream;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return outputStream;
	}
	
	/**
	 * Use the ServicesServer and its ObjectRegistry to do the translation.
	 * Put a copy of the xmlString into the LoggingRequestMessage.
	 * Do not doRecursiveDescent when translating the message, since we're logging,
	 * so all we need is to copy the String to the appropriate log file.
	 * 
	 * @param messageString
	 * @return
	 * @throws XmlTranslationException
	 */
	protected RequestMessage translateXMLStringToRequestMessage(String messageString)
	throws XmlTranslationException 
	{
		// translate with recursive descent!
		RequestMessage requestMessage = servicesServer.translateXMLStringToRequestMessage(messageString, false);
		if (requestMessage instanceof LogRequestMessage)
		{
			LogRequestMessage lrm = (LogRequestMessage) requestMessage;
			lrm.setXmlString(messageString);
		}
		else
			throw new XmlTranslationException("LoggingServer received non logging message: " + requestMessage);
		return requestMessage;
	}

}