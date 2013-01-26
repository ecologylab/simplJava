/*
 * Created on May 12, 2006
 */
package ecologylab.oodss.distributed.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.PortUnreachableException;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import simpl.core.SimplTypesScope;
import simpl.exceptions.SIMPLTranslationException;
import simpl.formats.enums.Format;
import simpl.formats.enums.StringFormat;

import ecologylab.collections.Scope;
import ecologylab.generic.Debug;
import ecologylab.generic.Generic;
import ecologylab.generic.StringTools;
import ecologylab.oodss.distributed.common.ClientConstants;
import ecologylab.oodss.distributed.common.LimitedInputStream;
import ecologylab.oodss.distributed.exception.MessageTooLargeException;
import ecologylab.oodss.distributed.impl.MessageWithMetadata;
import ecologylab.oodss.distributed.impl.MessageWithMetadataPool;
import ecologylab.oodss.distributed.impl.PreppedRequest;
import ecologylab.oodss.distributed.impl.PreppedRequestPool;
import ecologylab.oodss.exceptions.BadClientException;
import ecologylab.oodss.messages.DisconnectRequest;
import ecologylab.oodss.messages.InitConnectionRequest;
import ecologylab.oodss.messages.InitConnectionResponse;
import ecologylab.oodss.messages.RequestMessage;
import ecologylab.oodss.messages.ResponseMessage;
import ecologylab.oodss.messages.SendableRequest;
import ecologylab.oodss.messages.ServiceMessage;
import ecologylab.oodss.messages.UpdateMessage;

/**
 * Services Client using NIO; a major difference with the NIO version is state tracking. Since the
 * sending methods do not wait for the server to return.
 * 
 * This object will listen for incoming messages from the server, and will send any messages that it
 * receives on its end.
 * 
 * Since the underlying implementation is TCP/IP, messages sent should be sent in order, and the
 * responses should match that order.
 * 
 * Another major difference between this and the non-NIO version of ServicesClient is that it is
 * StartAndStoppable.
 * 
 * @author Zachary O. Dugas Toups (zach@ecologylab.net)
 */
public class NIOClient<S extends Scope> extends Debug implements ClientConstants
{
	protected String																									serverAddress;

	/**
	 * stores the sequence of characters read from the header of an incoming message, may need to
	 * persist across read calls, as the entire header may not be sent at once.
	 */
	private final StringBuilder																				currentHeaderSequence					= new StringBuilder();

	/**
	 * stores the sequence of characters read from the header of an incoming message and identified as
	 * being a key for a header entry; may need to persist across read calls.
	 */
	private final StringBuilder																				currentKeyHeaderSequence			= new StringBuilder();

	
	private volatile boolean																					blockingRequestPending				= false;

	private final Queue<MessageWithMetadata<ServiceMessage, Object>>	blockingResponsesQueue				= new LinkedBlockingQueue<MessageWithMetadata<ServiceMessage, Object>>();

	protected final LinkedBlockingQueue<PreppedRequest>								requestsQueue									= new LinkedBlockingQueue<PreppedRequest>();

	/**
	 * A map that stores all the requests that have not yet gotten responses. Maps UID to
	 * RequestMessage.
	 */
	protected final Map<Long, PreppedRequest>													unfulfilledRequests						= new HashMap<Long, PreppedRequest>();

	/**
	 * The number of times a call to reconnect() should attempt to contact the server before giving up
	 * and calling stop().
	 */
	protected int																											reconnectAttempts							= RECONNECT_ATTEMPTS;

	/** The number of milliseconds to wait between reconnect attempts. */
	protected int																											waitBetweenReconnectAttempts	= WAIT_BEWTEEN_RECONNECT_ATTEMPTS;

	private String																										sessionId											= null;

	protected ReconnectBlocker																				blocker												= null;

	/**
	 * selectInterval is passed to select() when it is called in the run loop. It is set to 0
	 * indicating that the loop should block until the selector picks up something interesting.
	 * However, if this class is subclassed, it is possible to modify this value so that the select()
	 * will only block for the number of ms supplied by this field. Thus, it is possible (by also
	 * subclassing the sendData() method) to have this send data on an interval, and then select.
	 */
	protected long																										selectInterval								= 0;

	protected boolean																									isSending											= false;

	/**
	 * Contains the unique identifier for the next message that the client will send.
	 */
	private long																											uidIndex											= 1;

	private int																												endOfFirstHeader							= -1;

	protected boolean																									maybeEndSequence							= false;

	private int																												uidOfCurrentMessage						= -1;

	/**
	 * Counts how many characters still need to be extracted from the incomingMessageBuffer before
	 * they can be turned into a message (based upon the HTTP header). A value of -1 means that there
	 * is not yet a complete header, so no length has been determined (yet).
	 */
	private int																												contentLengthRemaining				= -1;

	/**
	 * Whether or not to allow server to reply using compression
	 */
	private boolean																										allowCompression							= false;

	/**
	 * Whether or not to send compressed requests.
	 */
	private boolean																										sendCompressed								= false;

	/**
	 * Stores the key-value pairings from a parsed HTTP-like header on an incoming message.
	 */
	protected final HashMap<String, String>														headerMap											= new HashMap<String, String>();

	protected Socket																									thisSocket										= null;

	protected final PreppedRequestPool																pRequestPool;

	protected final MessageWithMetadataPool<ServiceMessage, Object>		responsePool									= new MessageWithMetadataPool<ServiceMessage, Object>(
																																																			2,
																																																			4);
	
	private String																										contentEncoding;

	private List<ClientStatusListener>																clientStatusListeners					= null;

	private byte[]																										readBuffer										= new byte[1024];
	
	private ByteBuffer																								firstByteReadBuffer 					= ByteBuffer.wrap(readBuffer, 0, 1);
	
	private CharBuffer																								firstCharBuffer								= CharBuffer
																																																			.allocate(1);

	private OutputStream																							socketOutputStream;

	private OutputStreamWriter																				socketWriter;

	private boolean																										maybeLineEnd									= false;

	private int																												portNumber;

	private InputStream																								socketInputStream;

	private boolean																										running												= false;

	private Thread																										reader;

	private Thread																										writer;

	protected S																													objectRegistry;

	private SimplTypesScope																					translationScope;

	protected CharsetDecoder																					decoder												= CHARSET
																																																			.newDecoder();

	public NIOClient(String serverAddress, int portNumber, SimplTypesScope messageSpace,
			S objectRegistry) throws IOException
	{
		super();

		this.portNumber = portNumber;
		this.objectRegistry = objectRegistry;

		this.translationScope = messageSpace;

		pRequestPool = new PreppedRequestPool(2, 4, 1024);

		this.serverAddress = serverAddress;
	}
	
	
	@Deprecated
	public NIOClient(String serverAddress, int portNumber, SimplTypesScope messageSpace,
			S objectRegistry, int maxMessageLengthChars) throws IOException
	{
		this(serverAddress, portNumber, messageSpace, objectRegistry);
	}

	/**
	 * If this client is not already connected, connects to the specified serverAddress on the
	 * specified portNumber, then calls start() to begin listening for server responses and processing
	 * them, then sends handshake data and establishes the session id.
	 * 
	 * @see ecologylab.oodss.distributed.legacy.ServicesClientBase#connect()
	 */
	public boolean connect(int timeoutMilli)
	{
		debug(5, "initializing connection...");
		if (this.connectImpl())
		{
			debug(5, "starting listener thread...");

			this.start();

			// now send first handshake message
			ResponseMessage initResponse = null;
			try
			{
				initResponse = this.sendMessage(new InitConnectionRequest(this.sessionId), timeoutMilli);
			}
			catch (MessageTooLargeException e)
			{
				// this shouldn't be able to happen
				e.printStackTrace();
			}

			if (initResponse instanceof InitConnectionResponse)
			{
				if (this.sessionId == null)
				{
					this.sessionId = ((InitConnectionResponse) initResponse).getSessionId();

					debug(3, "new session: " + this.sessionId);
				}
				else if (this.sessionId == ((InitConnectionResponse) initResponse).getSessionId())
				{
					debug(3, "reconnected and restored previous connection: " + this.sessionId);
				}
				else
				{
					String newId = ((InitConnectionResponse) initResponse).getSessionId();
					debug("unable to restore previous session, " + this.sessionId + "; new session: " + newId);
					this.unableToRestorePreviousConnection(this.sessionId, newId);
					this.sessionId = newId;
				}

				// this.thisSocket.keyFor(this.selector).attach(this.sessionId);
			}
		}

		this.notifyOfStatusChange(this.connected());

		debug(5, "connected? " + this.connected());
		return connected();
	}
	
	public boolean connect()
	{
	  return connect(-1);
	}

	/**
	 * Connect to the server (if not already connected). Return connection status.
	 * 
	 * @return True if connected, false if not.
	 */
	private boolean connectImpl()
	{
		return connected() ? true : createConnection();
	}

	/**
	 * Sets the UID for request (if necessary), enqueues it then registers write interest for the
	 * NIOClient's selection key and calls wakeup() on the selector.
	 * 
	 * @param request
	 * @throws SIMPLTranslationException
	 */
	protected PreppedRequest prepareAndEnqueueRequestForSending(SendableRequest request)
			throws SIMPLTranslationException, MessageTooLargeException
	{
		long uid = this.generateUid();

		PreppedRequest pReq = null;

		pReq = this.pRequestPool.acquire();

		// fill requestBuffer
		SimplTypesScope.serialize(request, pReq.getRequest(), StringFormat.XML);

		pReq.setUid(uid);
		pReq.setDisposable(request.isDisposable());

		if (pReq != null)
			enqueueRequestForSending(pReq);

		return pReq;
	}

	protected void enqueueRequestForSending(PreppedRequest request)
	{
		synchronized (requestsQueue)
		{
			requestsQueue.add(request);
		}
	}

	public void disconnect(boolean waitForResponses)
	{
		int attemptsCounter = 0;
		while (this.requestsRemaining() > 0 && this.connected() && waitForResponses
				&& attemptsCounter++ < 10)
		{
			debug(5, "******************* Request queue not empty, finishing " + requestsRemaining()
					+ " messages before disconnecting (attempt " + attemptsCounter + ")...");
			synchronized (this)
			{
				try
				{
					wait(100);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}

		debug(5, "* starting disconnect process...");

		try
		{
			if (connected())
			{
				debug(5, "** currently connected...");

				while (waitForResponses && connected() && !this.shutdownOK() && attemptsCounter-- > 0)
				{
					debug(5, "*** " + this.unfulfilledRequests.size()
							+ " requests still pending response from server (attempt " + attemptsCounter + ").");
					debug(5, "*** connected: " + connected());

					synchronized (this)
					{
						try
						{
							wait(100);
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}
				}

				// disconnect properly...
				if (this.sessionId != null)
				{
					debug(5, "**** session still active; handling disconnecting messages...");
					this.handleDisconnectingMessages();
					this.sessionId = null;
				}
			}
		}
		finally
		{
			stop();
			nullOut();
		}
	}

	/**
	 * Hook method for subclasses to provide specific disconnect messages. For example, authenticating
	 * clients will want to log out.
	 */
	protected void handleDisconnectingMessages()
	{
		debug(5, "************** sending disconnect request");
		try
		{
			this.sendMessage(DisconnectRequest.REUSABLE_INSTANCE, 10000);
		}
		catch (MessageTooLargeException e)
		{
			// this shouldn't be able to happen, unless the maximum request size
			// gets set below the size of a DisconnectRequest
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 */
	protected boolean shutdownOK()
	{
		return !(this.unfulfilledRequests.size() > 0);
	}

	protected void nullOut()
	{
		if (thisSocket != null)
		{
			synchronized (thisSocket)
			{
				thisSocket = null;
			}
		}
	}

	public boolean connected()
	{
		return (thisSocket != null) && thisSocket.isConnected();
	}

	/**
	 * Side effect of calling start().
	 */
	protected boolean createConnection()
	{
		if(!connected())
		{
			try
			{
				// create the channel and connect it to the server
				debug(5, "creating socket!");
				thisSocket = new Socket();
				
				
				thisSocket.connect(new InetSocketAddress(serverAddress, portNumber));
	
				socketOutputStream = new BufferedOutputStream(thisSocket.getOutputStream());
				socketWriter = new OutputStreamWriter(socketOutputStream, CHARSET);
	
				socketInputStream = new BufferedInputStream(thisSocket.getInputStream());
			}
			catch (BindException e)
			{
				debug(5, "Couldnt create socket connection to server - " + serverAddress + ":" + portNumber
						+ " - " + e);
	
				nullOut();
			}
			catch (PortUnreachableException e)
			{
				debug(5, "Server is alive, but has no daemon on portNumber " + portNumber + ": " + e);
	
				nullOut();
			}
			catch (SocketException e)
			{
				debug(5, "Server '" + serverAddress + "' unreachable: " + e);
	
				nullOut();
			}
			catch (IOException e)
			{
				debug(5, "Bad response from server: " + e);
	
				nullOut();
			}
		}
		return connected();
	}

	/**
	 * Hook method to allow subclasses to deal with a failed restore after disconnect. This should be
	 * a rare occurance, but some sublcasses may need to deal with this case specifically.
	 * 
	 * @param oldId
	 *          - the previous session id.
	 * @param newId
	 *          - the new session id given by the server after reconnect.
	 */
	protected void unableToRestorePreviousConnection(String oldId, String newId)
	{
	}

	/**
	 * Sends request, but does not wait for the response. The response gets processed later in a
	 * non-stateful way by the run method.
	 * 
	 * @param request
	 *          the request to send to the server.
	 * 
	 * @return the UID of request.
	 */
	public PreppedRequest nonBlockingSendMessage(RequestMessage request) throws IOException,
			MessageTooLargeException
	{
		if (connected())
		{
			try
			{
				return this.prepareAndEnqueueRequestForSending(request);
			}
			catch (SIMPLTranslationException e)
			{
				error("error translating message; returning null");
				e.printStackTrace();

				return null;
			}
		}
		else
		{
			throw new IOException("Not connected to server.");
		}
	}

	/**
	 * Blocking send. Sends the request and waits infinitely for the response, which it returns.
	 * 
	 * @throws MessageTooLargeException
	 * 
	 * @see ecologylab.oodss.distributed.legacy.ServicesClientBase#sendMessage(ecologylab.oodss.messages.RequestMessage)
	 */
	public synchronized ResponseMessage sendMessage(RequestMessage request)
			throws MessageTooLargeException
	{
		return this.sendMessage(request, -1);
	}

	/**
	 * Blocking send with timeout. Sends the request and waits timeOutMillis milliseconds for the
	 * response, which it returns. sendMessage(RequestMessage, int) will return null if no message was
	 * received in time.
	 * 
	 * @param request
	 * @param timeOutMillis
	 * @return
	 * @throws MessageTooLargeException
	 */
	public synchronized ResponseMessage sendMessage(SendableRequest request, int timeOutMillis)
			throws MessageTooLargeException
	{
		MessageWithMetadata<ServiceMessage, Object> responseMessage = null;

		// notify the connection thread that we are waiting on a response
		blockingRequestPending = true;

		long currentMessageUid;

		boolean blockingRequestFailed = false;
		long startTime = System.currentTimeMillis();
		int timeCounter = 0;

		try
		{
			currentMessageUid = this.prepareAndEnqueueRequestForSending(request).getUid();
		}
		catch (SIMPLTranslationException e1)
		{
			error("error translating to XML; returning null");
			e1.printStackTrace();

			return null;
		}
		catch (MessageTooLargeException e)
		{
			blockingRequestPending = false;
			error("message too large to send");
			e.printStackTrace();
			throw e;
		}

		// wait to be notified that the response has arrived
		while (blockingRequestPending && !blockingRequestFailed)
		{
			if (timeOutMillis <= -1)
			{
				debug(5, "waiting on blocking request");
			}

			try
			{
				if (timeOutMillis > -1)
				{
					wait(timeOutMillis);
				}
				else
				{
					wait();
				}
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
				Thread.interrupted();
			}

			debug(5, "waking");

			timeCounter += System.currentTimeMillis() - startTime;
			startTime = System.currentTimeMillis();

			while ((blockingRequestPending) && (!blockingResponsesQueue.isEmpty()))
			{
				responseMessage = blockingResponsesQueue.poll();

				if (responseMessage.getUid() == currentMessageUid)
				{
					debug(5, "got the right response: " + currentMessageUid);

					blockingRequestPending = false;

					blockingResponsesQueue.clear();

					ResponseMessage respMsg = (ResponseMessage) responseMessage.getMessage();

					// try
					// {
					// debug("response: " + respMsg.serialize().toString());
					// }
					// catch (SIMPLTranslationException e)
					// {
					// e.printStackTrace();
					// }

					responseMessage = responsePool.release(responseMessage);

					return respMsg;
				}

				responseMessage = responsePool.release(responseMessage);
			}

			if ((timeOutMillis > -1) && (timeCounter >= timeOutMillis) && (blockingRequestPending))
			{
				blockingRequestFailed = true;
			}
		}

		if (blockingRequestFailed)
		{
			debug(5, "Request failed due to timeout!");
		}

		return null;
	}

	private class SocketReader implements Runnable
	{

		@Override
		public void run()
		{
			while (running)
			{
				try
				{
					processReadData(socketInputStream);
				}
				catch (BadClientException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	private class SocketWriter implements Runnable
	{

		@Override
		public void run()
		{
			while (running)
			{
				try
				{
					createPacketFromMessageAndSend(requestsQueue.take());
				}
				catch (InterruptedException e)
				{
					if (!running)
					{
						break;
					}
				}

			}

		}

	}

	public void start()
	{
		if (connected())
		{
			running = true;
			reader = new Thread(new SocketReader());
			reader.start();

			writer = new Thread(new SocketWriter());
			writer.start();
		}
	}

	public void stop()
	{
		debug(5, "shutting down client listening thread.");
		running = false;

		try
		{
			if(thisSocket != null)
				thisSocket.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		reader.interrupt();
		writer.interrupt();
	}

	/**
	 * Returns the next request in the request queue and removes it from that queue. Sublcasses that
	 * override the queue functionality will need to override this method.
	 * 
	 * @return the next request in the request queue.
	 */
	protected PreppedRequest dequeueRequest()
	{
		return this.requestsQueue.poll();
	}

	/**
	 * Returns the number of requests remaining in the requests queue. Subclasses that override the
	 * queue functionality will need to change this method accordingly.
	 * 
	 * @return the size of the request queue.
	 */
	protected int requestsRemaining()
	{
		return this.requestsQueue.size();
	}

	/**
	 * Attempts to reconnect this client if it has been disconnected. After reconnecting, re-queues
	 * all requests still in the unfulfilledRequests map.
	 * 
	 * If the attempt to reconnect fails, reconnect() will attempt a number of times equal to
	 * reconnectAttempts, waiting waitBetweenReconnectAttempts milliseconds between attempts. If all
	 * such attempts fail, calls stop() on this to shut down the client. The client will then need to
	 * be re-started manually.
	 * 
	 */
	protected void reconnect()
	{
		debug(5, "attempting to reconnect...");
		int reconnectsRemaining = this.reconnectAttempts;
		if (reconnectsRemaining < 0)
		{
			reconnectsRemaining = 1;
		}

		while (!connected() && reconnectsRemaining > 0)
		{
			this.nullOut();

			// attempt to connect, if failed, wait
			if (!this.connect() && --reconnectsRemaining > 0)
			{
				try
				{
					this.wait(this.waitBetweenReconnectAttempts);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}

		if (connected())
		{
			synchronized (unfulfilledRequests)
			{
				List<PreppedRequest> rerequests = new LinkedList<PreppedRequest>(
						this.unfulfilledRequests.values());

				Collections.sort(rerequests);

				for (PreppedRequest req : rerequests)
				{
					this.enqueueRequestForSending(req);
				}
			}
		}
		else
		{
			this.stop();
		}
	}

	/**
	 * Hook method to allow subclasses to deal with unfulfilled requests in their own way.
	 * 
	 * Adds req to the unfulfilled requests map.
	 * 
	 * @param req
	 */
	protected void addUnfulfilledRequest(PreppedRequest req)
	{
		synchronized (unfulfilledRequests)
		{
			this.unfulfilledRequests.put(req.getUid(), req);
		}
	}

	/**
	 * Stores the request in the unfulfilledRequests map according to its UID, converts it to XML,
	 * prepends the HTTP-like header, then writes it out to the channel. Then re-registers key for
	 * reading.
	 * 
	 * @param pReq
	 */
	private void createPacketFromMessageAndSend(PreppedRequest pReq)
	{
		StringBuilder outgoingReq = pReq.getRequest();

		this.addUnfulfilledRequest(pReq);

		byte[] messageBytes = null;

		try
		{
			if (this.sendCompressed)
			{

				messageBytes = this.compress(outgoingReq);
			}
			else
			{
				messageBytes = this.encode(outgoingReq);
			}

			socketWriter.append(CONTENT_LENGTH_STRING);
			socketWriter.append(':');
			socketWriter.append("" + messageBytes.length);

			socketWriter.append(HTTP_HEADER_LINE_DELIMITER);

			socketWriter.append(UNIQUE_IDENTIFIER_STRING);
			socketWriter.append(':');
			socketWriter.append("" + pReq.getUid());

			if (allowCompression)
			{
				socketWriter.append(HTTP_HEADER_LINE_DELIMITER);
				socketWriter.append(HTTP_ACCEPTED_ENCODINGS);
			}
			if (this.sendCompressed)
			{
				socketWriter.append(HTTP_HEADER_LINE_DELIMITER);
				socketWriter.append(HTTP_CONTENT_CODING);
				socketWriter.append(":");
				socketWriter.append(HTTP_DEFLATE_ENCODING);
			}

			socketWriter.append(HTTP_HEADER_TERMINATOR);

			socketWriter.flush();
			
			socketOutputStream.write(messageBytes);
			socketOutputStream.flush();
		}
		catch (ClosedChannelException e)
		{
			debug(5, "connection severed; disconnecting and storing requests...");
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
			System.out.println("recovering.");
		}
		catch (CharacterCodingException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			debug("connection severed; disconnecting...");
			this.disconnect(false);
		}
	}

	private byte[] compress(StringBuilder src) throws IOException
	{
		ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream(1024);
		DeflaterOutputStream zipStream = new DeflaterOutputStream(byteArrayStream);
		OutputStreamWriter encodingStream = new OutputStreamWriter(zipStream, CHARSET);

		encodingStream.append(src);
		encodingStream.flush();
		zipStream.flush();
		zipStream.finish();
		byteArrayStream.flush();

		return byteArrayStream.toByteArray();
	}

	private byte[] encode(StringBuilder src) throws IOException
	{
		ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream(1024);
		OutputStreamWriter encodingStream = new OutputStreamWriter(byteArrayStream, CHARSET);

		encodingStream.append(src);
		encodingStream.flush();
		byteArrayStream.flush();

		return byteArrayStream.toByteArray();
	}

	private void processUpdate(UpdateMessage message)
	{
		message.processUpdate(objectRegistry);
	}

	/**
	 * Converts incomingMessage to a ResponseMessage, then processes the response and removes its UID
	 * from the unfulfilledRequests map.
	 * 
	 * @param incomingMessage
	 * @return
	 * @throws SIMPLTranslationException 
	 */
	private MessageWithMetadata<ServiceMessage, Object> processString(
			InputStream incomingMessageStream, int incomingUid) throws SIMPLTranslationException
	{

		/*
		 * if (show(5)) debug("incoming message: " + incomingMessage);
		 */

		MessageWithMetadata<ServiceMessage, Object> response = translateXMLStringToServiceMessage(
				incomingMessageStream, incomingUid);		

		if (response == null)
		{
			debug("ERROR: translation failed: ");
		}
		else
		{
			if (response.getMessage() instanceof ResponseMessage)
			{
				// perform the service being requested
				processResponse((ResponseMessage) response.getMessage());

				synchronized (unfulfilledRequests)
				{
					PreppedRequest finishedReq = unfulfilledRequests.remove(response.getUid());

					if (finishedReq != null)
					{ // subclasses might choose not to use unfulfilledRequests; this
						// avoids problems with releasing resources;
						// NOTE -- it may be necessary to release elsewhere in this case.
						finishedReq = this.pRequestPool.release(finishedReq);
					}
				}
			}
			else if (response.getMessage() instanceof UpdateMessage)
			{
				processUpdate((UpdateMessage) response.getMessage());
			}
		}

		return response;
	}

	public void disconnect()
	{
		disconnect(true);
	}

	/**
	 * @param reconnectAttempts
	 *          the reconnectAttempts to set
	 */
	public void setReconnectAttempts(int reconnectAttempts)
	{
		this.reconnectAttempts = reconnectAttempts;
	}

	/**
	 * @param waitBetweenReconnectAttempts
	 *          the waitBetweenReconnectAttempts to set
	 */
	public void setWaitBetweenReconnectAttempts(int waitBetweenReconnectAttempts)
	{
		this.waitBetweenReconnectAttempts = waitBetweenReconnectAttempts;
	}

	protected void clearSessionId()
	{
		this.sessionId = null;
	}

	/**
	 * @see ecologylab.oodss.distributed.impl.NIONetworking#processReadData(java.lang.Object,
	 *      java.nio.channels.SocketChannel, byte[], int)
	 */
	protected void processReadData(InputStream inputStream) throws BadClientException
	{

		try
		{
			// look for HTTP header

			if (endOfFirstHeader == -1)
			{				
				endOfFirstHeader = this.parseHeader(inputStream);
			}

			if (endOfFirstHeader == -1)
			{
				/*
				 * no header yet; if it's too large, bad client; if it's not too large yet, just exit, it'll
				 * get checked again when more data comes down the pipe
				 */
				return;
			}
			else
			{ // we've read all of the header, and have it loaded into the
				// map; now we can use it
				if (contentLengthRemaining == -1)
				{
					try
					{
						// handle all header information here; delete it when
						// done here
						contentLengthRemaining = Integer.parseInt(this.headerMap.get(CONTENT_LENGTH_STRING));

						if (headerMap.containsKey(UNIQUE_IDENTIFIER_STRING))
						{
							uidOfCurrentMessage = Integer.parseInt(this.headerMap.get(UNIQUE_IDENTIFIER_STRING));
						}
						contentEncoding = this.headerMap.get(HTTP_CONTENT_CODING);

						this.headerMap.clear();
					}
					catch (NumberFormatException e)
					{
						e.printStackTrace();
						contentLengthRemaining = -1;
					}
				}
			}

			/*
			 * we have the end of the first header (otherwise we would have broken out earlier). If we
			 * don't have the content length, something bad happened, because it should have been read.
			 */
			if (contentLengthRemaining == -1)
			{
				/*
				 * if we still don't have the remaining length, then there was a problem
				 */
				return;
			}

			try
			{
				// buffer all incoming bytes from stream

				if (contentLengthRemaining > 0)
				{
					LimitedInputStream limitStream = new LimitedInputStream(inputStream, contentLengthRemaining);
					
					
					InflaterInputStream inflateStream = null;

					InputStream messageStream = null;

					if (contentEncoding != null && contentEncoding.equals(HTTP_DEFLATE_ENCODING))
					{
						inflateStream = new InflaterInputStream(limitStream);
						messageStream = inflateStream;
					}
					else
					{
						messageStream = limitStream;
					}

					try
					{
						
						if (!this.blockingRequestPending)
						{
							// we process the read data into a response message, let it
							// perform its response, then dispose of
							// the
							// resulting MessageWithMetadata object
							this.responsePool.release(processString(messageStream, uidOfCurrentMessage));
						}
						else
						{
							blockingResponsesQueue.add(processString(messageStream, uidOfCurrentMessage));
							synchronized (this)
							{
								notify();
							}
						}
						
					}
					catch (SIMPLTranslationException e)
					{
						e.printStackTrace();
						
					}
					finally
					{
						while(limitStream.available() > 0)
						{							
							limitStream.read(readBuffer);
						}
						
						contentLengthRemaining = -1; 
						endOfFirstHeader = -1;
					}

				}

			}
			catch (NullPointerException e)
			{
				e.printStackTrace();
			}

		}
		catch (CharacterCodingException e1)
		{
			e1.printStackTrace();
		}
		catch (SocketException se)
		{
			this.stop();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * Increments the internal tracker of the next UID, and returns the current one.
	 * 
	 * @return the current uidIndex.
	 */
	public synchronized long generateUid()
	{
		// return the current value of uidIndex, then increment.
		return uidIndex++;
	}

	/**
	 * Use the ServicesClient and its NameSpace to do the translation. Can be overridden to provide
	 * special functionalities
	 * 
	 * @param messageString
	 * @return
	 * @throws XMLTranslationException
	 */
	protected MessageWithMetadata<ServiceMessage, Object> translateXMLStringToServiceMessage(
			InputStream inputStream, int incomingUid) throws SIMPLTranslationException
	{
		ServiceMessage resp = (ServiceMessage) this.translationScope.deserialize(inputStream,
				Format.XML, CHARSET);

		if (resp == null)
		{
			return null;
		}

		MessageWithMetadata<ServiceMessage, Object> retVal = this.responsePool.acquire();

		retVal.setMessage(resp);
		retVal.setUid(incomingUid);

		return retVal;
	}

	/**
	 * Process a ResponseMessage received from the server in response to a previously-sent
	 * RequestMessage.
	 * 
	 * @param responseMessageToProcess
	 */
	protected void processResponse(ResponseMessage responseMessageToProcess)
	{
		responseMessageToProcess.processResponse(objectRegistry);
	}

	public String getServer()
	{
		return this.serverAddress;
	}

	public void setServer(String serverAddress)
	{
		this.serverAddress = serverAddress;
	}

	/**
	 * Returns the most recently used UID.
	 * 
	 * @return the current uidIndex.
	 */
	public long getUidNoIncrement()
	{
		// return the current value of uidIndex
		return uidIndex;
	}

	/**
	 * Check to see if the server is running.
	 * 
	 * @return true if the server is running, false otherwise.
	 */
	public boolean isServerRunning()
	{
		debug(5, "checking availability of server");
		boolean serverIsRunning = createConnection();

		// we're just checking, don't keep the connection
		if (connected())
		{
			debug(5, "server is running; disconnecting");
			disconnect();
		}

		return serverIsRunning;
	}

	/**
	 * Try and connect to the server. If we fail, wait CONNECTION_RETRY_SLEEP_INTERVAL and try again.
	 * Repeat ad nauseum.
	 */
	public void waitForConnect()
	{
		System.out.println("Waiting for a server on port " + portNumber);
		while (!connect())
		{
			// try again soon
			Generic.sleep(WAIT_BEWTEEN_RECONNECT_ATTEMPTS);
		}
	}

	/**
	 * Parses the header of an incoming set of characters (i.e. a message from a client to a server),
	 * loading all of the HTTP-like headers into the given headerMap.
	 * 
	 * If headerMap is null, this method will throw a null pointer exception.
	 * 
	 * @param allIncomingChars
	 *          - the characters read from an incoming stream.
	 * @param headerMap
	 *          - the map into which all of the parsed headers will be placed.
	 * @return the length of the parsed header, or -1 if it was not yet found.
	 * @throws IOException
	 */
	protected int parseHeader(InputStream incomingStream) throws IOException
	{
		// indicates that we might be at the end of the header

		char currentChar;
		
		synchronized (currentHeaderSequence)
		{
			StringTools.clear(currentHeaderSequence);
			StringTools.clear(currentKeyHeaderSequence);

			while (true)
			{
				int ret = incomingStream.read(readBuffer, 0, 1);
				
				if(ret != 1)
				{
					return -1;
				}
				
				firstByteReadBuffer.position(0);
				firstByteReadBuffer.limit(1);
								
				firstCharBuffer.clear();
				decoder.decode(firstByteReadBuffer, firstCharBuffer, true);

				firstCharBuffer.flip();
				
				currentChar = firstCharBuffer.charAt(0);
				
				switch (currentChar)
				{
				case (':'):
					/*
					 * we have the end of a key; move the currentHeaderSequence into the
					 * currentKeyHeaderSequence and clear it
					 */
					currentKeyHeaderSequence.append(currentHeaderSequence);

					StringTools.clear(currentHeaderSequence);
					maybeLineEnd = false;

					break;
				case ('\r'):
					/*
					 * we have the end of a line; if there's a CRLF, then we have the end of the value
					 * sequence or the end of the header.
					 */
					maybeLineEnd = true;
					break;
				case ('\n'):
					if (maybeLineEnd)
					{
						if (!maybeEndSequence)
						{// load the key/value pair
							headerMap.put(currentKeyHeaderSequence.toString().toLowerCase(),
									currentHeaderSequence.toString());

							StringTools.clear(currentKeyHeaderSequence);
							StringTools.clear(currentHeaderSequence);
						}
						else
						{ // end of the header
							maybeEndSequence = false;
							return 0;
						}

						maybeEndSequence = true;
					}
					maybeLineEnd = false;
					
					break;
				default:
					currentHeaderSequence.append(currentChar);
					maybeEndSequence = false;
					maybeLineEnd = false;
					break;
				}
				
			}
		}
	}

	public void setReconnectBlocker(ReconnectBlocker blocker)
	{
		this.blocker = blocker;
	}

	public class Reconnecter implements Runnable
	{
		@Override
		public void run()
		{
			if (blocker != null)
				blocker.reconnectBlock();
			debug("Reconnecting in reconnector thread!");
			reconnect();
		}
	}

	public boolean allowsCompression()
	{
		return this.allowCompression;
	}

	public void allowCompression(boolean useCompression)
	{
		this.allowCompression = useCompression;
	}

	/**
	 * Specifies whether or not to use request compression, most web servers don't allow receiving
	 * compressed requests (i.e. Apache)
	 * 
	 * @param useRequestCompression
	 *          whether or not to use compression when sending requests
	 */
	public void useRequestCompression(boolean useRequestCompression)
	{
		this.sendCompressed = useRequestCompression;
	}

	public boolean usesRequestCompression()
	{
		return this.sendCompressed;
	}

	public void addClientStatusListener(ClientStatusListener csl)
	{
		this.clientStatusListeners().add(csl);
	}

	public void removeClientStatusListener(ClientStatusListener csl)
	{
		this.clientStatusListeners().remove(csl);
	}

	private void notifyOfStatusChange(boolean newStatus)
	{
		if (this.clientStatusListeners != null)
		{
			for (ClientStatusListener csl : this.clientStatusListeners())
			{
				csl.clientConnectionStatusChanged(newStatus);
			}
		}
	}

	private List<ClientStatusListener> clientStatusListeners()
	{
		if (this.clientStatusListeners == null)
		{
			synchronized (this)
			{
				if (this.clientStatusListeners == null)
				{
					this.clientStatusListeners = new LinkedList<ClientStatusListener>();
				}
			}
		}

		return this.clientStatusListeners;
	}
	
	public void setPriority(int priority)
	{
		if (reader != null)
		{
			reader.setPriority(priority);
		}
		if (writer != null)
		{
			writer.setPriority(priority);
		}
	}
	
	public S getScope()
	{
		return objectRegistry;
	}
}
