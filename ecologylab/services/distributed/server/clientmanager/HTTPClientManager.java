package ecologylab.services.distributed.server.clientmanager;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import ecologylab.collections.Scope;
import ecologylab.net.ParsedURL;
import ecologylab.services.distributed.impl.NIOServerIOThread;
import ecologylab.services.distributed.server.NIOServerFrontend;
import ecologylab.services.messages.BadSemanticContentResponse;
import ecologylab.services.messages.HttpRequest;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.TranslationSpace;

public abstract class HTTPClientManager extends AbstractClientManager
{

	static final String	HTTP_VERSION					= "HTTP/1.1";

	static final String	HTTP_RESPONSE_HEADERS		= HTTP_VERSION + " 303 See Other" + "\r\n";
																						//+ "Date: Fri, 17 Nov 2006 05:21:59 GMT\r\n";

	static final String				HTTP_APPEND						= " " + HTTP_VERSION;

	static final int					HTTP_APPEND_LENGTH			= HTTP_APPEND.length();

	static final String HTTP_CONTENT_TYPE 			= "Content-Type: text/plain; charset=US-ASCII\r\n";
	
	protected boolean					ALLOW_HTTP_STYLE_REQUESTS	= true;
	
	public HTTPClientManager(Object sessionId, int maxPacketSize,
			NIOServerIOThread server, NIOServerFrontend frontend,
			SelectionKey socket, TranslationSpace translationSpace,
			Scope<?> registry) {
		super(sessionId, maxPacketSize, server, frontend, socket, translationSpace,
				registry);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @see ecologylab.services.distributed.server.clientmanager.ClientManager#clearOutgoingMessageHeaderBuffer(java.lang.StringBuilder)
	 */
	@Override protected void clearOutgoingMessageHeaderBuffer(StringBuilder outgoingMessageHeaderBuf)
	{
		outgoingMessageHeaderBuf.delete(0, outgoingMessageHeaderBuf.length());
	}

	/**
	 * @see ecologylab.services.distributed.server.clientmanager.ClientManager#createHeader(java.lang.StringBuilder,
	 *      java.lang.StringBuilder, RequestMessage, ResponseMessage)
	 */
	@Override protected void createHeader(StringBuilder outgoingMessageBuf, StringBuilder outgoingMessageHeaderBuf,
			RequestMessage incomingRequest, ResponseMessage outgoingResponse, long uid)
	{
		if (incomingRequest instanceof HttpRequest)
		{
			HttpRequest httpRequest = (HttpRequest) incomingRequest;

			ParsedURL responseUrl = null;
			if (outgoingResponse.isOK())
				responseUrl = httpRequest.okResponseUrl();
			else
				responseUrl = httpRequest.errorResponseUrl();

			debugA("responseUrl: " + responseUrl);

			if (responseUrl != null)
				outgoingMessageHeaderBuf.append(HTTP_RESPONSE_HEADERS + HTTP_CONTENT_TYPE + "Location: " + responseUrl.toString() + "\r\n\r\n" + System.getProperty("line.separator"));

			debugA("Server sending response!!!\n" + outgoingMessageHeaderBuf.toString());
		}
	}
	
	/**
	 * @see ecologylab.services.distributed.server.clientmanager.AbstractClientManager#clearOutgoingMessageBuffer(java.lang.StringBuilder)
	 */
	@Override protected void clearOutgoingMessageBuffer(StringBuilder outgoingMessageBuf)
	{
	}

	/**
	 * @see ecologylab.services.distributed.server.clientmanager.AbstractClientManager#prepareBuffers(java.lang.StringBuilder,
	 *      java.lang.StringBuilder, java.lang.StringBuilder)
	 */
	@Override protected void prepareBuffers(StringBuilder incomingMessageBuf, StringBuilder outgoingMessageBuf,
			StringBuilder outgoingMessageHeaderBuf)
	{
	}
	
	@Override protected ResponseMessage performService(RequestMessage requestMessage)
	{
		requestMessage.setSender(((SocketChannel)this.socketKey.channel()).socket().getInetAddress());

		try
		{
			return requestMessage.performService(localScope);
		}
		catch (Exception e)
		{
			e.printStackTrace();

			return new BadSemanticContentResponse("The request, "
					+ requestMessage.toString()
					+ " caused an exception on the server.");
		}
	}
}
