/**
 * 
 */
package ecologylab.services.distributed.server.contextmanager;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.generic.Debug;
import ecologylab.generic.StringTools;
import ecologylab.services.distributed.common.ServerConstants;
import ecologylab.services.distributed.impl.MessageWithMetadata;
import ecologylab.services.distributed.impl.MessageWithMetadataPool;
import ecologylab.services.distributed.impl.NIOServerBackend;
import ecologylab.services.distributed.server.NIOServerFrontend;
import ecologylab.services.exceptions.BadClientException;
import ecologylab.services.messages.BadSemanticContentResponse;
import ecologylab.services.messages.InitConnectionRequest;
import ecologylab.services.messages.InitConnectionResponse;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XMLTranslationException;

/**
 * The base class for all ContextManagers, objects which track the state and
 * respond to clients on a server. There is a one-to-one correspondence between
 * connected clients and ContextManager instances.
 * 
 * AbstractContextManager handles all encoding and decoding of messages, as well
 * as translating them. Hook methods provide places where subclasses may modify
 * behavior for specific purposes.
 * 
 * Typical usage is to have the context manager's request queue be filled by a
 * network thread, while it is emptied by a working thread.
 * 
 * The normal cycle for filling the queue is to call
 * acquireIncomingSequenceBuf() to clear and get the incomingCharBuffer, then
 * fill it externally (normally passing it as an argument to a
 * CharsetDecoder.decode call), then calling processIncomingSequenceBufToQueue()
 * to release it and let the ContextManager store the characters, converting
 * messages into objects as they become available.
 * 
 * For a complete, basic implementation (which is suitable for most uses), see
 * {@link ecologylab.services.distributed.server.contextmanager.ClientManager ContextManager}.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public abstract class AbstractClientManager extends Debug implements
		ServerConstants
{
	/**
	 * Stores the key-value pairings from a parsed HTTP-like header on an
	 * incoming message.
	 */
	protected final HashMap<String, String>							headerMap						= new HashMap<String, String>();

	protected int																startReadIndex					= 0;

	/**
	 * Stores incoming character data until it can be parsed into an XML message
	 * and turned into a Java object.
	 */
	protected final StringBuilder											msgBufIncoming					= new StringBuilder(
																																MAX_PACKET_SIZE_CHARACTERS);

	/** Stores outgoing character data for ResponseMessages. */
	protected final StringBuilder											msgBufOutgoing					= new StringBuilder(
																																MAX_PACKET_SIZE_CHARACTERS);

	/** Stores outgoing header character data. */
	protected final StringBuilder											headerBufOutgoing				= new StringBuilder(
																																MAX_HTTP_HEADER_LENGTH);

	/**
	 * Indicates whether or not one or more messages are queued for execution by
	 * this ContextManager.
	 */
	protected boolean															messageWaiting					= false;

	/**
	 * A queue of the requests to be performed by this ContextManager. Subclasses
	 * may override functionality and not use requestQueue.
	 */
	protected final Queue<MessageWithMetadata<RequestMessage>>	requestQueue					= new LinkedBlockingQueue<MessageWithMetadata<RequestMessage>>();

	/**
	 * The ObjectRegistry that is used by the processRequest method of each
	 * incoming RequestMessage.
	 */
	protected ObjectRegistry<?>											registry;

	/**
	 * The network communicator that will handle all the reading and writing for
	 * the socket associated with this ContextManager
	 */
	protected NIOServerBackend												server;

	/**
	 * The frontend for the server that is running the ContextManager. This is
	 * needed in case the client attempts to restore a session, in which case the
	 * frontend must be queried for the old ContextManager.
	 */
	protected NIOServerFrontend											frontend							= null;

	/**
	 * The selection key for this context manager.
	 */
	protected SelectionKey													socketKey;

	/**
	 * sessionId uniquely identifies this ContextManager. It is used to restore
	 * the state of a lost connection.
	 */
	protected Object															sessionId						= null;

	protected int																maxPacketSize;

	/** Used to translate incoming message XML strings into RequestMessages. */
	protected TranslationSpace												translationSpace;

	/**
	 * stores the sequence of characters read from the header of an incoming
	 * message, may need to persist across read calls, as the entire header may
	 * not be sent at once.
	 */
	private final StringBuilder											currentHeaderSequence		= new StringBuilder();

	/**
	 * stores the sequence of characters read from the header of an incoming
	 * message and identified as being a key for a header entry; may need to
	 * persist across read calls.
	 */
	private final StringBuilder											currentKeyHeaderSequence	= new StringBuilder();

	/** A buffer for data that will be sent back to the client. */
	private CharBuffer														outgoingChars					= CharBuffer
																																.allocate(MAX_PACKET_SIZE_CHARACTERS);

	/**
	 * Tracks the number of bad transmissions from the client; used for
	 * determining if a client is bad.
	 */
	private int																	badTransmissionCount;

	private int																	endOfFirstHeader				= -1;

	private long																lastActivity					= System
																																.currentTimeMillis();

	/**
	 * Counts how many characters still need to be extracted from the
	 * incomingMessageBuffer before they can be turned into a message (based upon
	 * the HTTP header). A value of -1 means that there is not yet a complete
	 * header, so no length has been determined (yet).
	 */
	private int																	contentLengthRemaining		= -1;

	/**
	 * Stores the first XML message from the incomingMessageBuffer, or parts of
	 * it (if it is being read over several invocations).
	 */
	private final StringBuilder											firstMessageBuffer			= new StringBuilder();

	/**
	 * Indicates whether the first request message has been received. The first
	 * request may be an InitConnection, which has special properties.
	 */
	protected boolean															initialized						= false;

	private final MessageWithMetadataPool<RequestMessage>			reqPool							= new MessageWithMetadataPool<RequestMessage>(
																																2,
																																4);

	private long																contentUid						= -1;

	/**
	 * Creates a new ContextManager.
	 * 
	 * @param sessionId
	 * @param maxPacketSize
	 * @param server
	 * @param frontend
	 * @param socket
	 * @param translationSpace
	 * @param registry
	 */
	public AbstractClientManager(Object sessionId, int maxPacketSize,
			NIOServerBackend server, NIOServerFrontend frontend,
			SelectionKey socket, TranslationSpace translationSpace,
			ObjectRegistry<?> registry)
	{
		this.frontend = frontend;
		this.socketKey = socket;
		this.server = server;
		this.registry = registry;
		this.translationSpace = translationSpace;

		// set up session id
		this.sessionId = sessionId;

		this.maxPacketSize = maxPacketSize;

		this.prepareBuffers(msgBufIncoming, msgBufOutgoing, headerBufOutgoing);
	}

	/**
	 * Extracts messages from the given CharBuffer, using HTTP-like headers,
	 * converting them into RequestMessage instances, then enqueues those
	 * instances.
	 * 
	 * enqueueStringMessage will normally be called repeatedly, as new data comes
	 * in from a client. It will automatically parse messages that are split up
	 * over multiple reads, and will handle multiple messages in one read, if
	 * necessary.
	 * 
	 * @param message
	 *           the CharBuffer containing one or more messages, or pieces of
	 *           messages.
	 */
	public final void processIncomingSequenceBufToQueue(
			CharBuffer incomingSequenceBuf) throws CharacterCodingException,
			BadClientException
	{
		synchronized (msgBufIncoming)
		{
			msgBufIncoming.append(incomingSequenceBuf);

			// look for HTTP header
			while (msgBufIncoming.length() > 0)
			{
				if (endOfFirstHeader == -1)
				{
					endOfFirstHeader = this.parseHeader(startReadIndex,
							msgBufIncoming);
				}

				if (endOfFirstHeader == -1)
				{ /*
					 * no header yet; if it's too large, bad client; if it's not too
					 * large yet, just exit, it'll get checked again when more data
					 * comes down the pipe
					 */
					if (msgBufIncoming.length() > ServerConstants.MAX_HTTP_HEADER_LENGTH)
					{
						// clear the buffer
						BadClientException e = new BadClientException(
								((SocketChannel) this.socketKey.channel()).socket()
										.getInetAddress().getHostAddress(),
								"Maximum HTTP header length exceeded. Read "
										+ msgBufIncoming.length() + "/"
										+ MAX_HTTP_HEADER_LENGTH);

						msgBufIncoming.setLength(0);

						throw e;
					}

					// next time around, start reading from where we left off this
					// time
					startReadIndex = msgBufIncoming.length();

					break;
				}
				else
				{ // we've read all of the header, and have it loaded into the map;
					// now we can use it
					if (contentLengthRemaining == -1)
					{
						try
						{
							// handle all header information here; delete it when done
							// here
							String contentLengthString = this.headerMap
									.get(CONTENT_LENGTH_STRING);
							contentLengthRemaining = (contentLengthString != null) ? Integer
									.parseInt(contentLengthString)
									: 0;

							String uidString = this.headerMap
									.get(UNIQUE_IDENTIFIER_STRING);
							contentUid = (uidString != null) ? Long
									.parseLong(uidString) : 0;

							// done with the header; delete it
							msgBufIncoming.delete(0, endOfFirstHeader);
							this.headerMap.clear();
						}
						catch (NumberFormatException e)
						{
							e.printStackTrace();
							contentLengthRemaining = -1;
						}
						// next time we read the header (the next message), we need to
						// start from the beginning
						startReadIndex = 0;
					}
				}

				/*
				 * we have the end of the header (otherwise we would have broken out
				 * earlier). If we don't have the content length, something bad
				 * happened, because it should have been read.
				 */
				if (contentLengthRemaining == -1)
				{
					/*
					 * if we still don't have the remaining length, then there was a
					 * problem
					 */
					break;
				}
				else if (contentLengthRemaining > maxPacketSize)
				{
					throw new BadClientException(((SocketChannel) this.socketKey
							.channel()).socket().getInetAddress().getHostAddress(),
							"Specified content length too large: "
									+ contentLengthRemaining);
				}

				try
				{
					// see if the incoming buffer has enough characters to
					// include the specified content length
					if (msgBufIncoming.length() >= contentLengthRemaining)
					{
						firstMessageBuffer.append(msgBufIncoming.substring(0,
								contentLengthRemaining));

						msgBufIncoming.delete(0, contentLengthRemaining);

						// reset to do a new read on the next invocation
						contentLengthRemaining = -1;
						endOfFirstHeader = -1;
					}
					else
					{
						firstMessageBuffer.append(msgBufIncoming);

						// indicate that we need to get more from the buffer in
						// the next invocation
						contentLengthRemaining -= msgBufIncoming.length();

						msgBufIncoming.setLength(0);
					}
				}
				catch (NullPointerException e)
				{
					e.printStackTrace();
				}

				if ((contentLengthRemaining == -1))
				{ /*
					 * if we've read a complete message, then contentLengthRemaining
					 * will be reset to -1
					 */
					processString(firstMessageBuffer, contentUid);
					firstMessageBuffer.setLength(0);
				}
			}
		}
	}

	/**
	 * Indicates the last System timestamp was when the ContextManager had any
	 * activity.
	 * 
	 * @return the last System timestamp indicating when the ContextManager had
	 *         any activity.
	 */
	public final long getLastActivity()
	{
		return lastActivity;
	}

	/**
	 * @return the socket
	 */
	public SelectionKey getSocketKey()
	{
		return socketKey;
	}

	/**
	 * Indicates whether there are any messages queued up to be processed.
	 * 
	 * isMessageWaiting() should be overridden if getNextRequest() is overridden
	 * so that it properly reflects the way that getNextRequest() works; it may
	 * also be important to override enqueueRequest().
	 * 
	 * @return true if getNextRequest() can return a value, false if it cannot.
	 */
	public boolean isMessageWaiting()
	{
		return messageWaiting;
	}

	/**
	 * Calls processRequest(RequestMessage) on each queued message as they are
	 * acquired through getNextRequest() and finishing when isMessageWaiting()
	 * returns false.
	 * 
	 * The functionality of processAllMessagesAndSendResponses() may be
	 * overridden by overridding the following methods: isMessageWaiting(),
	 * processRequest(RequestMessage), getNextRequest().
	 * 
	 * @throws BadClientException
	 */
	public final void processAllMessagesAndSendResponses()
			throws BadClientException
	{
		while (isMessageWaiting())
		{
			this.processNextMessageAndSendResponse();
		}
	}

	/**
	 * Sets the SelectionKey, and sets the new SelectionKey to have the same
	 * attachment (session id) as the old one.
	 * 
	 * @param socket
	 *           the socket to set
	 */
	public void setSocket(SelectionKey socket)
	{
		String sessionId = (String) this.socketKey.attachment();

		this.socketKey = socket;

		this.socketKey.attach(sessionId);
	}

	/**
	 * Hook method for having shutdown behavior.
	 * 
	 * This method is called whenever the client terminates their connection or
	 * when the server is shutting down.
	 */
	public void shutdown()
	{

	}

	protected abstract void clearOutgoingMessageBuffer(
			StringBuilder outgoingMessageBuf);

	protected abstract void clearOutgoingMessageHeaderBuffer(
			StringBuilder outgoingMessageHeaderBuf);

	protected abstract void createHeader(StringBuilder outgoingMessageBuf,
			StringBuilder outgoingMessageHeaderBuf,
			RequestMessage incomingRequest, ResponseMessage outgoingResponse,
			long uid);

	/**
	 * Adds the given request to this's request queue.
	 * 
	 * enqueueRequest(RequestMessage) is a hook method for ContextManagers that
	 * need to implement other functionality, such as prioritizing messages.
	 * 
	 * If enqueueRequest(RequestMessage) is overridden, the following methods
	 * should also be overridden: isMessageWaiting(), getNextRequest().
	 * 
	 * @param request
	 */
	protected void enqueueRequest(MessageWithMetadata<RequestMessage> request)
	{
		messageWaiting = this.requestQueue.offer(request);
	}

	/**
	 * Returns the next message in the request queue.
	 * 
	 * getNextRequest() may be overridden to provide specific functionality, such
	 * as a priority queue. In this case, it is important to override the
	 * following methods: isMessageWaiting(), enqueueRequest().
	 * 
	 * @return the next message in the requestQueue.
	 */
	protected MessageWithMetadata<RequestMessage> getNextRequest()
	{
		synchronized (requestQueue)
		{
			int queueSize = requestQueue.size();

			if (queueSize == 1)
			{
				messageWaiting = false;
			}

			// return null if none left, or the next Request otherwise
			return requestQueue.poll();
		}
	}

	/**
	 * Parses the header of an incoming set of characters (i.e. a message from a
	 * client to a server), loading all of the HTTP-like headers into the given
	 * headerMap.
	 * 
	 * If headerMap is null, this method will throw a null pointer exception.
	 * 
	 * @param allIncomingChars -
	 *           the characters read from an incoming stream.
	 * @param headerMap -
	 *           the map into which all of the parsed headers will be placed.
	 * @return the length of the parsed header, or -1 if it was not yet found.
	 */
	protected int parseHeader(int startChar, StringBuilder allIncomingChars)
	{
		// indicates that we might be at the end of the header
		boolean maybeEndSequence = false;
		char currentChar;

		synchronized (currentHeaderSequence)
		{
			StringTools.clear(currentHeaderSequence);
			StringTools.clear(currentKeyHeaderSequence);

			int length = allIncomingChars.length();

			for (int i = 0; i < length; i++)
			{
				currentChar = allIncomingChars.charAt(i);

				switch (currentChar)
				{
				case (':'):
					/*
					 * we have the end of a key; move the currentHeaderSequence into
					 * the currentKeyHeaderSequence and clear it
					 */
					currentKeyHeaderSequence.append(currentHeaderSequence);

					StringTools.clear(currentHeaderSequence);

					break;
				case ('\r'):
					/*
					 * we have the end of a line; if there's a CRLF, then we have the
					 * end of the value sequence or the end of the header.
					 */
					if (allIncomingChars.charAt(i + 1) == '\n')
					{
						if (!maybeEndSequence)
						{// load the key/value pair
							headerMap.put(currentKeyHeaderSequence.toString()
									.toLowerCase(), currentHeaderSequence.toString().trim());

							StringTools.clear(currentKeyHeaderSequence);
							StringTools.clear(currentHeaderSequence);

							i++; // so we don't re-read that last character
						}
						else
						{ // end of the header
							return i + 2;
						}

						maybeEndSequence = true;
					}
					break;
				default:
					currentHeaderSequence.append(currentChar);
					maybeEndSequence = false;
					break;
				}
			}

			// if we got here, we didn't finish the header
			return -1;
		}
	}

	/**
	 * Appends the sender's IP address to the incoming message and calls
	 * performService on the given RequestMessage using the local ObjectRegistry.
	 * 
	 * performService(RequestMessage) may be overridden by subclasses to provide
	 * more specialized functionality. Generally, overrides should then call
	 * super.performService(RequestMessage) so that the IP address is appended to
	 * the message.
	 * 
	 * @param requestMessage
	 * @return
	 */
	protected ResponseMessage performService(RequestMessage requestMessage)
	{
		requestMessage.setSender(((SocketChannel) this.socketKey.channel())
				.socket().getInetAddress());

		try
		{
			return requestMessage
					.performService(registry, (String) this.sessionId);
		}
		catch (Exception e)
		{
			e.printStackTrace();

			return new BadSemanticContentResponse("The request, "
					+ requestMessage.toString()
					+ " caused an exception on the server.");
		}
	}

	protected abstract void prepareBuffers(StringBuilder incomingMessageBuf,
			StringBuilder outgoingMessageBuf,
			StringBuilder outgoingMessageHeaderBuf);

	protected abstract void translateResponseMessageToStringBufferContents(
			RequestMessage requestMessage, ResponseMessage responseMessage,
			StringBuilder messageBuffer) throws XMLTranslationException;

	/**
	 * Translates the given XML String into a RequestMessage object.
	 * 
	 * translateStringToRequestMessage(String) may be overridden to provide
	 * specific functionality, such as a ContextManager that does not use XML
	 * Strings.
	 * 
	 * @param messageString -
	 *           an XML String representing a RequestMessage object.
	 * @return the RequestMessage created by translating messageString into an
	 *         object.
	 * @throws XMLTranslationException
	 *            if an error occurs when translating from XML into a
	 *            RequestMessage.
	 * @throws UnsupportedEncodingException
	 *            if the String is not encoded properly.
	 */
	protected RequestMessage translateStringToRequestMessage(
			CharSequence messageString) throws XMLTranslationException,
			UnsupportedEncodingException
	{
		return (RequestMessage) ElementState.translateFromXMLCharSequence(
				messageString, translationSpace);
	}

	/**
	 * Calls processRequest(RequestMessage) on the result of getNextRequest().
	 * 
	 * In order to override functionality processRequest(RequestMessage) and/or
	 * getNextRequest() should be overridden.
	 * 
	 */
	private final void processNextMessageAndSendResponse()
	{
		this.processRequest(this.getNextRequest());
	}

	/**
	 * Calls performService(requestMessage), then converts the resulting
	 * ResponseMessage into a String, adds the HTTP-like headers, and passes the
	 * completed String to the server backend for sending to the client.
	 * 
	 * @param request -
	 *           the request message to process.
	 * @param uid
	 *           TODO
	 */
	private final void processRequest(
			MessageWithMetadata<RequestMessage> requestWithMetadata)
	{
		this.lastActivity = System.currentTimeMillis();

		ResponseMessage response = null;
		RequestMessage request = requestWithMetadata.getMessage();

		if (request == null)
		{
			debug("No request.");
		}
		else
		{
			if (!isInitialized())
			{
				// special processing for InitConnectionRequest
				if (request instanceof InitConnectionRequest)
				{
					String incomingSessionId = ((InitConnectionRequest) request)
							.getSessionId();

					if (incomingSessionId == null)
					{ // client is not expecting an old ContextManager
						response = new InitConnectionResponse((String) this.sessionId);
					}
					else
					{ // client is expecting an old ContextManager
						if (frontend.restoreContextManagerFromSessionId(
								incomingSessionId, this))
						{
							response = new InitConnectionResponse(incomingSessionId);
						}
						else
						{
							response = new InitConnectionResponse(
									(String) this.sessionId);
						}
					}
				}

				initialized = true;
			}
			else
			{
				// perform the service being requested
				response = performService(request);
			}

			if (response != null)
			{ // if the response is null, then we do
				// nothing else
				try
				{
					// setup outgoingMessageBuffer
					this.translateResponseMessageToStringBufferContents(request,
							response, msgBufOutgoing);

					// setup outgoingMessageHeaderBuffer
					this.createHeader(msgBufOutgoing, headerBufOutgoing, request,
							response, requestWithMetadata.getUid());

					// move the characters from the outgoing buffers into
					// outgoingChars using bulk get and put methods
					outgoingChars.clear();

					headerBufOutgoing.getChars(0, headerBufOutgoing.length(),
							outgoingChars.array(), 0);
					msgBufOutgoing.getChars(0, msgBufOutgoing.length(),
							outgoingChars.array(), headerBufOutgoing.length());

					outgoingChars.limit(headerBufOutgoing.length()
							+ msgBufOutgoing.length());

					this.clearOutgoingMessageBuffer(msgBufOutgoing);
					this.clearOutgoingMessageHeaderBuffer(headerBufOutgoing);

					ByteBuffer outgoingBuffer = this.server
							.acquireByteBufferFromPool();

					synchronized (ENCODER)
					{
						ENCODER.reset();

						ENCODER.encode(outgoingChars, outgoingBuffer, true);

						ENCODER.flush(outgoingBuffer);
					}

					server.enqueueBytesForWriting(this.socketKey, outgoingBuffer);
				}
				catch (XMLTranslationException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				debug("context manager did not produce as response message.");
			}
		}

		requestWithMetadata = reqPool.release(requestWithMetadata);
	}

	/**
	 * Takes an incoming message in the form of an XML String and converts it
	 * into a RequestMessage using translateStringToRequestMessage(String). Then
	 * places the RequestMessage on the requestQueue using enqueueRequest().
	 * 
	 * @param incomingMessage
	 * @throws BadClientException
	 */
	private final void processString(CharSequence incomingMessage,
			long incomingUid) throws BadClientException
	{
		RequestMessage request = null;
		try
		{
			request = this.translateStringToRequestMessage(incomingMessage);
		}
		catch (XMLTranslationException e)
		{
			// drop down to request == null, below
		}
		catch (UnsupportedEncodingException e)
		{
			// drop down to request == null, below
		}

		if (request == null)
		{
			if (incomingMessage.length() > 100)
			{
				debug("ERROR; incoming message could not be translated: "
						+ incomingMessage.subSequence(0, 50)
						+ "..."
						+ incomingMessage.subSequence(incomingMessage.length() - 50,
								incomingMessage.length()));
			}
			else
			{
				debug("ERROR; incoming message could not be translated: "
						+ incomingMessage);
			}
			if (++badTransmissionCount >= MAXIMUM_TRANSMISSION_ERRORS)
			{
				throw new BadClientException(((SocketChannel) this.socketKey
						.channel()).socket().getInetAddress().getHostAddress(),
						"Too many Bad Transmissions: " + badTransmissionCount);
			}
			// else
			error("translation failed: badTransmissionCount="
					+ badTransmissionCount);
		}
		else
		{
			badTransmissionCount = 0;

			MessageWithMetadata<RequestMessage> pReq = this.reqPool.acquire();

			pReq.setMessage(request);
			pReq.setUid(incomingUid);

			synchronized (requestQueue)
			{
				this.enqueueRequest(pReq);
			}
		}
	}

	/**
	 * Indicates whether or not this context manager has been initialized.
	 * Normally, this means that it has shared a session id with the client.
	 * 
	 * @return
	 */
	public boolean isInitialized()
	{
		return initialized;
	}
}