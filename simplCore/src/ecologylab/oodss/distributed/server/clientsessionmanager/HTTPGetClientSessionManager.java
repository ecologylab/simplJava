/**
 * 
 */
package ecologylab.oodss.distributed.server.clientsessionmanager;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.channels.SelectionKey;

import ecologylab.collections.Scope;
import ecologylab.oodss.distributed.impl.NIOServerIOThread;
import ecologylab.oodss.distributed.server.NIOServerProcessor;
import ecologylab.oodss.messages.HttpGetRequest;
import ecologylab.oodss.messages.RequestMessage;
import ecologylab.oodss.messages.ResponseMessage;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;

/**
 * A ContextManager for handling HTTP Get requests. Can be used to respond to browser requests.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 * 
 */
public class HTTPGetClientSessionManager<S extends Scope, PARENT extends Scope> extends HTTPClientSessionManager<S, PARENT>
{
	public static final String	HTTP_PREPEND				= "GET /";

	public static final int			HTTP_PREPEND_LENGTH	= HTTP_PREPEND.length();

	/**
	 * @param token
	 * @param server
	 * @param socketKey
	 * @param translationScope
	 * @param registry
	 */
	public HTTPGetClientSessionManager(String token, int maxPacketSize, NIOServerIOThread server,
			NIOServerProcessor frontend, SelectionKey socketKey, SimplTypesScope translationScope,
			PARENT registry)
	{
		super(token, maxPacketSize, server, frontend, socketKey, translationScope, registry);

		this.initialized = true;
	}

	/**
	 * This method only handles HttpGetRequest messages; it will report an error for any
	 * non-HttpGetRequest. Otherwise, it will not add anything to the msgBufOutgoing, as
	 * HttpGetRequests should only have a header and no contnents
	 * 
	 * @see ecologylab.oodss.distributed.server.clientsessionmanager.ClientSessionManager#translateResponseMessageToString(ecologylab.oodss.messages.RequestMessage,
	 *      ecologylab.oodss.messages.ResponseMessage)
	 */
	@Override
	protected void translateResponseMessageToStringBufferContents(RequestMessage requestMessage,
			ResponseMessage responseMessage, StringBuilder outgoingMessageBuf)
			throws SIMPLTranslationException
	{
		if (!(requestMessage instanceof HttpGetRequest))
		{
			debug("ERROR! HTTPGetContextManager only handles HttpGetRequests!");
		}
	}

	/**
	 * This client manager operates primarily on HTTP header data, as it is unlikely that a GET
	 * request will contain an actual RequestMessage.
	 * 
	 * @throws UnsupportedEncodingException
	 * @see ecologylab.oodss.distributed.server.clientsessionmanager.ClientSessionManager#translateStringToRequestMessage(java.lang.String)
	 */
	@Override
	protected RequestMessage translateStringToRequestMessage(CharSequence messageSequence)
			throws SIMPLTranslationException, UnsupportedEncodingException
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
