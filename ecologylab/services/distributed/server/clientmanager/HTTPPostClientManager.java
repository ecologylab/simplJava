package ecologylab.services.distributed.server.clientmanager;

import java.io.UnsupportedEncodingException;
import java.nio.channels.SelectionKey;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.services.distributed.impl.NIOServerBackend;
import ecologylab.services.distributed.server.NIOServerFrontend;
import ecologylab.services.messages.HttpRequest;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XMLTranslationException;

public class HTTPPostClientManager extends HTTPClientManager
{				

	static final String 	HTTP_PREPEND 		= "POST /";
	static final int 		HTTP_PREPEND_LENGTH	= HTTP_PREPEND.length();

	/**
	 * @param token
	 * @param server
	 * @param socketKey
	 * @param translationSpace
	 * @param registry
	 */
	public HTTPPostClientManager(Object token, int maxPacketSize, NIOServerBackend server, NIOServerFrontend frontend,
			SelectionKey socketKey, TranslationSpace translationSpace, ObjectRegistry<?> registry)
	{	
		super(token, maxPacketSize, server, frontend, socketKey, translationSpace, registry);
		this.initialized = true;
	}

	/**
	 * This method only handles HttpGetRequest messages; it will report an error for any non-HttpGetRequest. Otherwise,
	 * it will not add anything to the msgBufOutgoing, as HttpGetRequests should only have a header and no contnents
	 * 
	 * @see ecologylab.services.distributed.server.clientmanager.ClientManager#translateResponseMessageToString(ecologylab.services.messages.RequestMessage,
	 *      ecologylab.services.messages.ResponseMessage)
	 */
	@Override protected void translateResponseMessageToStringBufferContents(RequestMessage requestMessage,
			ResponseMessage responseMessage, StringBuilder outgoingMessageBuf) throws XMLTranslationException
	{
		if (!(requestMessage instanceof HttpRequest))
		{
			debug("ERROR! HTTPPostContextManager only handles HttpRequests!");
		}
	}

	/**
	 * @throws UnsupportedEncodingException
	 * @see ecologylab.services.distributed.server.clientmanager.ClientManager#translateStringToRequestMessage(java.lang.String)
	 */
	@Override protected RequestMessage translateStringToRequestMessage(
			CharSequence incomingMessage) throws XMLTranslationException,
			UnsupportedEncodingException
	{
		String messageString 	= incomingMessage.toString();
		//messageString			= URLDecoder.decode(messageString, "UTF-8");
		if (!messageString.startsWith("<"))
			messageString = messageString.substring(messageString.indexOf('=')+1);
		
		return super.translateStringToRequestMessage(messageString);
	}

}
