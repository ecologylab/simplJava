/**
 * 
 */
package ecologylab.services.distributed.server.contextmanager;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.channels.SelectionKey;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.services.distributed.impl.NIOServerBackend;
import ecologylab.services.distributed.server.NIOServerFrontend;
import ecologylab.services.messages.HttpGetRequest;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XMLTranslationException;

/**
 * A ContextManager for handling HTTP Get requests. Can be used to respond to browser requests.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public class HTTPGetContextManager extends HTTPContextManager
{
	static final String	HTTP_PREPEND		= "GET /";
	static final int	HTTP_PREPEND_LENGTH	= HTTP_PREPEND.length();

	/**
	 * @param token
	 * @param server
	 * @param socketKey
	 * @param translationSpace
	 * @param registry
	 */
	public HTTPGetContextManager(Object token, int maxPacketSize, NIOServerBackend server, NIOServerFrontend frontend,
			SelectionKey socketKey, TranslationSpace translationSpace, ObjectRegistry<?> registry)
	{
		super(token, maxPacketSize, server, frontend, socketKey, translationSpace, registry);
	}

	/**
	 * This method only handles HttpGetRequest messages; it will report an error for any non-HttpGetRequest. Otherwise,
	 * it will not add anything to the msgBufOutgoing, as HttpGetRequests should only have a header and no contnents
	 * 
	 * @see ecologylab.services.distributed.server.contextmanager.ContextManager#translateResponseMessageToString(ecologylab.services.messages.RequestMessage,
	 *      ecologylab.services.messages.ResponseMessage)
	 */
	@Override protected void translateResponseMessageToStringBufferContents(RequestMessage requestMessage,
			ResponseMessage responseMessage, StringBuilder outgoingMessageBuf) throws XMLTranslationException
	{
		if (!(requestMessage instanceof HttpGetRequest))
		{
			debug("ERROR! HTTPGetContextManager only handles HttpGetRequests!");
		}
	}

	/**
	 * @throws UnsupportedEncodingException
	 * @see ecologylab.services.distributed.server.contextmanager.ContextManager#translateStringToRequestMessage(java.lang.String)
	 */
	@Override protected RequestMessage translateStringToRequestMessage(CharSequence messageSequence)
			throws XMLTranslationException, UnsupportedEncodingException
	{
		String messageString = messageSequence.toString();
		if (messageString.startsWith(HTTP_PREPEND))
		{
			int endIndex = messageString.lastIndexOf(HTTP_APPEND);
			messageString = messageString.substring(HTTP_PREPEND_LENGTH, endIndex);
			messageString = URLDecoder.decode(messageString, "UTF-8");
			debug("fixed message! " + messageString);
		}

		return super.translateStringToRequestMessage(messageString);
	}

	

}
