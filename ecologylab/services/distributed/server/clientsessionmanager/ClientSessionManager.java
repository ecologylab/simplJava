package ecologylab.services.distributed.server.clientsessionmanager;

import java.nio.channels.SelectionKey;

import ecologylab.collections.Scope;
import ecologylab.services.distributed.common.ServerConstants;
import ecologylab.services.distributed.impl.NIOServerIOThread;
import ecologylab.services.distributed.server.NIOServerProcessor;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.services.messages.UpdateMessage;
import ecologylab.xml.TranslationScope;
import ecologylab.xml.SIMPLTranslationException;

/**
 * Stores information about the connection context for the client on the server. Should be extended
 * for more specific implementations. Handles accumulating incoming messages and translating them
 * into RequestMessage objects, as well as the ability to perform the messages' services and send
 * their responses.
 * 
 * Generally, this class can be driven by one or more threads, depending on the desired
 * functionality.
 * 
 * On a server, there will be one ContextManager for each client connection.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public class ClientSessionManager<S extends Scope> extends TCPClientSessionManager<S> implements ServerConstants
{
	/**
	 * Creates a new ContextManager.
	 * 
	 * @param sessionId
	 * @param maxPacketSize
	 * @param server
	 * @param frontend
	 * @param socketKey
	 * @param translationScope
	 * @param registry
	 */
	public ClientSessionManager(String sessionId, int maxPacketSize, NIOServerIOThread server,
			NIOServerProcessor frontend, SelectionKey socketKey, TranslationScope translationScope,
			Scope<?> registry)
	{
		super(sessionId, maxPacketSize, server, frontend, socketKey, translationScope, registry);
	}

	/**
	 * Prepares the internal buffer objects, which are passed in as parameters.
	 * 
	 * This is a hook method, custom subclasses may wish to override it with different (or, in some
	 * cases, no) functionality.
	 * 
	 * This implementation adds "content-length: " to the outgoingMessageHeaderBuf for performance
	 * purposes. Subclasses that do not use this optimization should override this method with one
	 * that does nothing.
	 */
	@Override
	protected void prepareBuffers(StringBuilder incomingMessageBuf, StringBuilder outgoingMessageBuf,
			StringBuilder outgoingMessageHeaderBuf)
	{
		outgoingMessageHeaderBuf.append(CONTENT_LENGTH_STRING + ":");
	}

	/**
	 * Clears the contents of the outgoingMessageHeaderBuffer so that it is prepared for the next
	 * outgoing message.
	 * 
	 * This implementation leaves "content-length:" so that it can be reused for the next message.
	 * 
	 * @param outgoingMessageHeaderBuf
	 */
	@Override
	protected void clearOutgoingMessageHeaderBuffer(StringBuilder outgoingMessageHeaderBuf)
	{
		outgoingMessageHeaderBuf.delete(CONTENT_LENGTH_STRING_LENGTH + 1, outgoingMessageHeaderBuf
				.length());
	}

	/**
	 * Clears the contents of the outgoingMessageBuffer so that it is empty and ready to recieve a new
	 * outgoing message.
	 * 
	 * @param msgBufOutgoing
	 */
	@Override
	protected void clearOutgoingMessageBuffer(StringBuilder outgoingMessageBuf)
	{
		outgoingMessageBuf.delete(0, outgoingMessageBuf.length());
	}

	/**
	 * Generates the outgoing message header. This implementation assumes that the
	 * outgoingMessageHeaderBuf contains "content-length: " and will add the content length, based on
	 * the contents of msgBufOutgoing, however, custom implementations for more specific purposes may
	 * be constructed.
	 * 
	 * @param msgBufOutgoing
	 * @param outgoingMessageHeaderBuf
	 * @param incomingRequest
	 *          TODO
	 * @param outgoingResponse
	 *          TODO
	 */
	@Override
	protected void createHeader(int messageSize, StringBuilder outgoingMessageHeaderBuf,
			RequestMessage incomingRequest, ResponseMessage outgoingResponse, long uid)
	{
		outgoingMessageHeaderBuf.append(messageSize);
		outgoingMessageHeaderBuf.append(HTTP_HEADER_LINE_DELIMITER);

		outgoingMessageHeaderBuf.append(UNIQUE_IDENTIFIER_STRING);
		outgoingMessageHeaderBuf.append(':');
		outgoingMessageHeaderBuf.append(uid);
	}

	/**
	 * Generates the outgoing message header (for updates). This implementation
	 * assumes that the outgoingMessageHeaderBuf contains "content-length: " and
	 * will add the content length, based on the contents of msgBufOutgoing,
	 * however, custom implementations for more specific purposes may be
	 * constructed.
	 * 
	 * @param messageSize
	 *           size of outgoing buffer
	 * @param outgoingMessageHeaderBuf
	 *           buffer to put header parts in
	 * @param update
	 *           update message going out
	 */
	@Override
	protected void makeUpdateHeader(int messageSize,
			StringBuilder outgoingMessageHeaderBuf, UpdateMessage<?> update)
	{
		outgoingMessageHeaderBuf.append(messageSize);
	}
	
	/**
	 * Translates response into an XML string and adds an HTTP-like header, then returns the result.
	 * 
	 * translateResponseMessageToString(RequestMessage, ResponseMessage) may be overridden to provide
	 * more specific functionality; for example, for servers that use customized messages instead of
	 * XML.
	 * 
	 * @param requestMessage
	 *          - the current request.
	 * @param responseMessage
	 *          - the ResponseMessage generated by processing requestMessage.
	 * @param messageBuffer
	 *          - buffer must be passed in to this method -- it will contain the results of the
	 *          translation and will be used by the calling method to send data back to the client.
	 * @return a String that constitutes a complete response message in XML with HTTP-like headers.
	 */
	@Override
	protected void translateResponseMessageToStringBufferContents(RequestMessage requestMessage,
			ResponseMessage responseMessage, StringBuilder messageBuffer) throws SIMPLTranslationException
	{
		responseMessage.serialize(messageBuffer);
	}
}
