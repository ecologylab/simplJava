package ecologylab.services.distributed.server.clientsessionmanager;

import java.io.UnsupportedEncodingException;
import java.nio.channels.SelectionKey;

import ecologylab.collections.Scope;
import ecologylab.services.distributed.impl.NIOServerIOThread;
import ecologylab.services.distributed.server.NIOServerProcessor;
import ecologylab.services.messages.HttpRequest;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.TranslationScope;
import ecologylab.xml.SIMPLTranslationException;

public class HTTPPostClientSessionManager extends HTTPClientSessionManager
{

	static final String	HTTP_PREPEND				= "POST /";

	static final int		HTTP_PREPEND_LENGTH	= HTTP_PREPEND.length();

	/**
	 * @param token
	 * @param server
	 * @param socketKey
	 * @param translationScope
	 * @param registry
	 */
	public HTTPPostClientSessionManager(String token, int maxPacketSize, NIOServerIOThread server,
			NIOServerProcessor frontend, SelectionKey socketKey, TranslationScope translationScope,
			Scope<?> registry)
	{
		super(token, maxPacketSize, server, frontend, socketKey, translationScope, registry);
		this.initialized = true;
	}

	/**
	 * This method only handles HttpGetRequest messages; it will report an error for any
	 * non-HttpGetRequest. Otherwise, it will not add anything to the msgBufOutgoing, as
	 * HttpGetRequests should only have a header and no contnents
	 * 
	 * @see ecologylab.services.distributed.server.clientsessionmanager.ClientSessionManager#translateResponseMessageToString(ecologylab.services.messages.RequestMessage,
	 *      ecologylab.services.messages.ResponseMessage)
	 */
	@Override
	protected void translateResponseMessageToStringBufferContents(RequestMessage requestMessage,
			ResponseMessage responseMessage, StringBuilder outgoingMessageBuf)
			throws SIMPLTranslationException
	{
		if (!(requestMessage instanceof HttpRequest))
		{
			debug("ERROR! HTTPPostContextManager only handles HttpRequests!");
		}
	}

	/**
	 * @throws UnsupportedEncodingException
	 * @see ecologylab.services.distributed.server.clientsessionmanager.ClientSessionManager#translateStringToRequestMessage(java.lang.String)
	 */
	@Override
	protected RequestMessage translateStringToRequestMessage(CharSequence incomingMessage)
			throws SIMPLTranslationException, UnsupportedEncodingException
	{
		String messageString = incomingMessage.toString();
		// messageString = URLDecoder.decode(messageString, "UTF-8");
		if (!messageString.startsWith("<"))
			messageString = messageString.substring(messageString.indexOf('=') + 1);

		return super.translateStringToRequestMessage(messageString);
	}

}
