/**
 * 
 */
package ecologylab.services.distributed.server.clientmanager;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.channels.SelectionKey;

import ecologylab.appframework.Scope;
import ecologylab.services.distributed.impl.NIOServerBackend;
import ecologylab.services.distributed.server.NIOServerFrontend;
import ecologylab.services.messages.HttpGetRequest;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XMLTranslationException;

/**
 * A ContextManager for handling HTTP Get requests. Can be used to respond to
 * browser requests.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public class HTTPGetClientManager extends HTTPClientManager
{
	static final String	HTTP_PREPEND			= "GET /";

	static final int		HTTP_PREPEND_LENGTH	= HTTP_PREPEND.length();

	/**
	 * @param token
	 * @param server
	 * @param socketKey
	 * @param translationSpace
	 * @param registry
	 */
	public HTTPGetClientManager(Object token, int maxPacketSize,
			NIOServerBackend server, NIOServerFrontend frontend,
			SelectionKey socketKey, TranslationSpace translationSpace,
			Scope<?> registry)
	{
		super(token, maxPacketSize, server, frontend, socketKey,
				translationSpace, registry);
		
		this.initialized = true;
	}

	/**
	 * This method only handles HttpGetRequest messages; it will report an error
	 * for any non-HttpGetRequest. Otherwise, it will not add anything to the
	 * msgBufOutgoing, as HttpGetRequests should only have a header and no
	 * contnents
	 * 
	 * @see ecologylab.services.distributed.server.clientmanager.ClientManager#translateResponseMessageToString(ecologylab.services.messages.RequestMessage,
	 *      ecologylab.services.messages.ResponseMessage)
	 */
	@Override protected void translateResponseMessageToStringBufferContents(
			RequestMessage requestMessage, ResponseMessage responseMessage,
			StringBuilder outgoingMessageBuf) throws XMLTranslationException
	{
		if (!(requestMessage instanceof HttpGetRequest))
		{
			debug("ERROR! HTTPGetContextManager only handles HttpGetRequests!");
		}
	}

	/**
	 * This client manager operates primarily on HTTP header data, as it is
	 * unlikely that a GET request will contain an actual RequestMessage.
	 * 
	 * @throws UnsupportedEncodingException
	 * @see ecologylab.services.distributed.server.clientmanager.ClientManager#translateStringToRequestMessage(java.lang.String)
	 */
	@Override protected RequestMessage translateStringToRequestMessage(
			CharSequence messageSequence) throws XMLTranslationException,
			UnsupportedEncodingException
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
