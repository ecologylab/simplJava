/**
 * 
 */
package ecologylab.services.distributed.server.contextmanager;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.channels.SocketChannel;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.net.ParsedURL;
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
public class HTTPGetContextManager extends AbstractContextManager
{
	static final String				HTTP_PREPEND					= "GET /";

	static final int					HTTP_PREPEND_LENGTH			= HTTP_PREPEND.length();

	private static final String	HTTP_VERSION					= "HTTP/1.1";

	private static final String	HTTP_RESPONSE_HEADERS		= HTTP_VERSION + " 307 Temporary Redirect" + "\r\n"
																						+ "Date: Fri, 17 Nov 2006 05:21:59 GMT\r\n";

	static final String				HTTP_APPEND						= " " + HTTP_VERSION;

	static final int					HTTP_APPEND_LENGTH			= HTTP_APPEND.length();

	protected boolean					ALLOW_HTTP_STYLE_REQUESTS	= true;

	/**
	 * @param token
	 * @param server
	 * @param socket
	 * @param translationSpace
	 * @param registry
	 */
	public HTTPGetContextManager(Object token, int maxPacketSize, NIOServerBackend server, NIOServerFrontend frontend,
			SocketChannel socket, TranslationSpace translationSpace, ObjectRegistry<?> registry)
	{
		super(token, maxPacketSize, server, frontend, socket, translationSpace, registry);
	}

	/**
	 * This method only handles HttpGetRequest messages; it will report an error for any non-HttpGetRequest. Otherwise,
	 * it will not add anything to the outgoingMessageBuf, as HttpGetRequests should only have a header and no contnents
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
	@Override protected RequestMessage translateStringToRequestMessage(String messageString)
			throws XMLTranslationException, UnsupportedEncodingException
	{
		if (messageString.startsWith(HTTP_PREPEND))
		{
			int endIndex = messageString.lastIndexOf(HTTP_APPEND);
			messageString = messageString.substring(HTTP_PREPEND_LENGTH, endIndex);
			messageString = URLDecoder.decode(messageString, "UTF-8");
			debug("fixed message! " + messageString);
		}

		return super.translateStringToRequestMessage(messageString);
	}

	/**
	 * @see ecologylab.services.distributed.server.contextmanager.ContextManager#clearOutgoingMessageHeaderBuffer(java.lang.StringBuilder)
	 */
	@Override protected void clearOutgoingMessageHeaderBuffer(StringBuilder outgoingMessageHeaderBuf)
	{
		outgoingMessageHeaderBuf.delete(0, outgoingMessageHeaderBuf.length());
	}

	/**
	 * @see ecologylab.services.distributed.server.contextmanager.ContextManager#createHeader(java.lang.StringBuilder,
	 *      java.lang.StringBuilder, RequestMessage, ResponseMessage)
	 */
	@Override protected void createHeader(StringBuilder outgoingMessageBuf, StringBuilder outgoingMessageHeaderBuf,
			RequestMessage incomingRequest, ResponseMessage outgoingResponse)
	{
		if (incomingRequest instanceof HttpGetRequest)
		{
			HttpGetRequest httpRequest = (HttpGetRequest) incomingRequest;

			ParsedURL responseUrl = null;
			if (outgoingResponse.isOK())
				responseUrl = httpRequest.okResponseUrl();
			else
				responseUrl = httpRequest.errorResponseUrl();

			debugA("responseUrl: " + responseUrl);

			if (responseUrl != null)
				outgoingMessageHeaderBuf.append(HTTP_RESPONSE_HEADERS + "Location: " + responseUrl.toString() + "\r\n\r\n");

			debugA("Server sending response!!!\n" + outgoingMessageHeaderBuf.toString());
		}
	}

	/**
	 * @see ecologylab.services.distributed.server.contextmanager.AbstractContextManager#clearOutgoingMessageBuffer(java.lang.StringBuilder)
	 */
	@Override protected void clearOutgoingMessageBuffer(StringBuilder outgoingMessageBuf)
	{
	}

	/**
	 * @see ecologylab.services.distributed.server.contextmanager.AbstractContextManager#prepareBuffers(java.lang.StringBuilder,
	 *      java.lang.StringBuilder, java.lang.StringBuilder)
	 */
	@Override protected void prepareBuffers(StringBuilder incomingMessageBuf, StringBuilder outgoingMessageBuf,
			StringBuilder outgoingMessageHeaderBuf)
	{
	}

}
