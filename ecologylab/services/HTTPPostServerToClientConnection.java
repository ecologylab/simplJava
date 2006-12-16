package ecologylab.services;

import java.io.IOException;
import java.net.Socket;

import ecologylab.services.messages.IgnoreRequest;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.XmlTranslationException;

/**
 * parses HTTP Post request message 
 * It ignore messages by sending IgnoreRequest when it gets the header strings. 
 * It only translates strings of body messages to XML RequestMessage.  
 * 
 * @author eunyee
 *
 */
public class HTTPPostServerToClientConnection extends ServerToClientConnection
{

	public HTTPPostServerToClientConnection(Socket incomingSocket, ServicesServer servicesServer) 
		throws IOException 
	{
		super(incomingSocket, servicesServer);
	}
	
	boolean HTTP_HEADER_END = false;
    /**
     * Use the ServicesServer and its ObjectRegistry to do the translation. 
     * 
     * @param messageString
     * @return
     * @throws XmlTranslationException
     */
    protected RequestMessage translateXMLStringToRequestMessage(
            String messageString) throws XmlTranslationException
    {
    	int messageLineLength = messageString.getBytes().length;
    	if( HTTP_HEADER_END )
    	{
	        RequestMessage requestMessage = servicesServer.translateXMLStringToRequestMessage(messageString, true);
	    debug("THIS REQUEST MESSAGE : " + requestMessage.translateToXML(false));  
	    
	        return requestMessage;
    	}
		if ((messageLineLength == 2) && "\n\r".equals(messageString))
		{
			HTTP_HEADER_END = true;
		} 

		return IgnoreRequest.get();

    }
}