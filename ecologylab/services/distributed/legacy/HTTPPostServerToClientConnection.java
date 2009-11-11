package ecologylab.services.distributed.legacy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import ecologylab.services.messages.IgnoreRequest;
import ecologylab.services.messages.RequestMessage;
import ecologylab.xml.XMLTranslationException;

/**
 * parses HTTP Post request message 
 * It ignore messages by sending IgnoreRequest when it gets the header strings. 
 * It only translates strings of body messages to XML RequestMessage.  
 * 
 * @author eunyee
 *
 */
@Deprecated public class HTTPPostServerToClientConnection extends ServerToClientConnection
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
     * @throws XMLTranslationException
     */
    protected RequestMessage translateXMLStringToRequestMessage(
            String messageString) throws XMLTranslationException, UnsupportedEncodingException
    {
        int messageLineLength = messageString.getBytes().length;
        if (HTTP_HEADER_END)
        {
            RequestMessage requestMessage = servicesServer
                    .translateXMLStringToRequestMessage(messageString, true);
            debug("THIS REQUEST MESSAGE : "
                    + requestMessage.translateToXML());

            return requestMessage;
        }
        if ((messageLineLength == 2) && "\n\r".equals(messageString))
        {
            HTTP_HEADER_END = true;
        }

        return IgnoreRequest.get();

    }
}