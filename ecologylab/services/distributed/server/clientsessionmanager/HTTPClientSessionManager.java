package ecologylab.services.distributed.server.clientsessionmanager;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import ecologylab.collections.Scope;
import ecologylab.net.ParsedURL;
import ecologylab.services.distributed.impl.NIOServerIOThread;
import ecologylab.services.distributed.server.NIOServerProcessor;
import ecologylab.services.messages.BadSemanticContentResponse;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.services.messages.UpdateMessage;
import ecologylab.xml.TranslationScope;

public abstract class HTTPClientSessionManager extends AbstractClientSessionManager
{

	static final String	HTTP_VERSION							= "HTTP/1.1";

	static final String	HTTP_RESPONSE_HEADERS			= HTTP_VERSION + " 303 See Other" + "\r\n";

	static final String	HTTP_APPEND								= " " + HTTP_VERSION;

	static final int		HTTP_APPEND_LENGTH				= HTTP_APPEND.length();

	static final String	HTTP_CONTENT_TYPE					= "Content-Type: text/plain; charset=US-ASCII\r\n";

	protected boolean		ALLOW_HTTP_STYLE_REQUESTS	= true;

	public HTTPClientSessionManager(String sessionId, int maxPacketSize, NIOServerIOThread server,
			NIOServerProcessor frontend, SelectionKey socket, TranslationScope translationSpace,
			Scope<?> registry)
	{
		super(sessionId, maxPacketSize, server, frontend, socket, translationSpace, registry);
	}

	/**
	 * @see ecologylab.services.distributed.server.clientsessionmanager.ClientSessionManager#clearOutgoingMessageHeaderBuffer(java.lang.StringBuilder)
	 */
	@Override
	protected void clearOutgoingMessageHeaderBuffer(StringBuilder outgoingMessageHeaderBuf)
	{
		outgoingMessageHeaderBuf.delete(0, outgoingMessageHeaderBuf.length());
	}

	/**
	 * @see ecologylab.services.distributed.server.clientsessionmanager.ClientSessionManager#createHeader(java.lang.StringBuilder,
	 *      java.lang.StringBuilder, RequestMessage, ResponseMessage)
	 */
	@Override
	protected void createHeader(int messageSize, StringBuilder outgoingMessageHeaderBuf,
			RequestMessage incomingRequest, ResponseMessage outgoingResponse, long uid)
	{
		boolean isOK = outgoingResponse.isOK();
		ParsedURL responseUrl = isOK ? incomingRequest.okRedirectUrl(localScope) : incomingRequest
				.errorRedirectUrl(localScope);

		if (responseUrl != null)
		{
			debugA("responseUrl: " + responseUrl);

			if (responseUrl != null)
			{
				outgoingMessageHeaderBuf.append(HTTP_RESPONSE_HEADERS
						+ HTTP_CONTENT_TYPE
						+ "Location: "
						+ responseUrl.toString());
			}

			debugA("Server sending response!!!\n" + outgoingMessageHeaderBuf.toString());
		}
		else
			warning("isOK=" + isOK + " but responseUrl=null. Can't send redirect response.");
	}

	protected void makeUpdateHeader(int messageSize, StringBuilder headerBufOutgoing,
			UpdateMessage<?> update)
	{

	}

	/**
	 * @see ecologylab.services.distributed.server.clientsessionmanager.AbstractClientSessionManager#clearOutgoingMessageBuffer(java.lang.StringBuilder)
	 */
	@Override
	protected void clearOutgoingMessageBuffer(StringBuilder outgoingMessageBuf)
	{
	}

	/**
	 * @see ecologylab.services.distributed.server.clientsessionmanager.AbstractClientSessionManager#prepareBuffers(java.lang.StringBuilder,
	 *      java.lang.StringBuilder, java.lang.StringBuilder)
	 */
	@Override
	protected void prepareBuffers(StringBuilder incomingMessageBuf, StringBuilder outgoingMessageBuf,
			StringBuilder outgoingMessageHeaderBuf)
	{
	}

	@Override
	protected ResponseMessage performService(RequestMessage requestMessage)
	{
		requestMessage.setSender(((SocketChannel) this.socketKey.channel()).socket().getInetAddress());

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
