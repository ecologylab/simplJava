/**
 * 
 */
package ecologylab.services.nio.contextmanager;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.generic.Debug;
import ecologylab.services.ServerConstants;
import ecologylab.services.ServicesServer;
import ecologylab.services.exceptions.BadClientException;
import ecologylab.services.messages.BadSemanticContentResponse;
import ecologylab.services.messages.InitConnectionRequest;
import ecologylab.services.messages.InitConnectionResponse;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.services.nio.NIOServerBackend;
import ecologylab.services.nio.servers.NIOServerFrontend;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XmlTranslationException;

/**
 * @author Zach
 * 
 */
public abstract class AbstractContextManager extends Debug implements ServerConstants
{

    protected static final String         CONTENT_LENGTH_STRING        = "content-length:";

    protected static final String         HTTP_HEADER_TERMINATOR       = "\r\n\r\n";

    protected static final int            CONTENT_LENGTH_STRING_LENGTH = CONTENT_LENGTH_STRING.length();

    /**
     * The encoder to translate from Strings to bytes.
     */
    private static final CharsetEncoder   encoder                      = Charset.forName(CHARACTER_ENCODING)
                                                                               .newEncoder();

    /**
     * Stores incoming character data until it can be parsed into an XML message and turned into a Java object.
     */
    protected final StringBuilder incomingMessageBuffer = new StringBuilder(MAX_PACKET_SIZE);

    /**
     * Stores outgoing character data for ResponseMessages.
     */
    protected final StringBuilder         outgoingMessageBuffer        = new StringBuilder(MAX_PACKET_SIZE);

    /**
     * Stores outgoing header character data.
     */
    protected final StringBuilder         outgoingMessageHeaderBuffer  = new StringBuilder(MAX_PACKET_SIZE);

    /**
     * Indicates whether or not one or more messages are queued for execution by this ContextManager.
     */
    protected boolean                     messageWaiting               = false;

    /**
     * A queue of the requests to be performed by this ContextManager. Subclasses may override functionality and not use
     * requestQueue.
     */
    protected final Queue<RequestMessage> requestQueue                 = new LinkedBlockingQueue<RequestMessage>();

    /**
     * The ObjectRegistry that is used by the processRequest method of each incoming RequestMessage.
     */
    protected ObjectRegistry              registry;

    /**
     * The network communicator that will handle all the reading and writing for the socket associated with this
     * ContextManager
     */
    protected NIOServerBackend            server;

    /**
     * The frontend for the server that is running the ContextManager. This is needed in case the client attempts to
     * restore a session, in which case the frontend must be queried for the old ContextManager.
     */
    protected NIOServerFrontend           frontend                     = null;

    protected SocketChannel               socket;

    /**
     * sessionId uniquely identifies this ContextManager. It is used to restore the state of a lost connection.
     */
    protected Object                      sessionId                    = null;

    protected int                         maxPacketSize;

    /**
     * Used to translate incoming message XML strings into RequestMessages.
     */
    protected TranslationSpace            translationSpace;

    /**
     * A buffer for data that will be sent back to the client.
     */
    private CharBuffer                    outgoingChars                = CharBuffer.allocate(MAX_PACKET_SIZE);

    /**
     * Tracks the number of bad transmissions from the client; used for determining if a client is bad.
     */
    private int                           badTransmissionCount;

    private int                           endOfFirstHeader             = -1;

    private long                          lastActivity                 = System.currentTimeMillis();

    /**
     * Counts how many characters still need to be extracted from the incomingMessageBuffer before they can be turned
     * into a message (based upon the HTTP header). A value of -1 means that there is not yet a complete header, so no
     * length has been determined (yet).
     */
    private int                           contentLengthRemaining       = -1;

    /**
     * Stores the first XML message from the incomingMessageBuffer, or parts of it (if it is being read over several
     * invocations).
     */
    private final StringBuilder           firstMessageBuffer           = new StringBuilder();

    /**
     * Indicates whether the first request message has been received. The first request may be an InitConnection, which
     * has special properties.
     */
    private boolean                       firstRequestReceived         = false;

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
    public AbstractContextManager(Object sessionId, int maxPacketSize, NIOServerBackend server, NIOServerFrontend frontend,
            SocketChannel socket, TranslationSpace translationSpace, ObjectRegistry registry)
    {
        this.frontend = frontend;
        this.socket = socket;
        this.server = server;
        // this.key = key;

        // channel = (SocketChannel) key.channel();
        //
        this.registry = registry;
        this.translationSpace = translationSpace;

        // set up session id
        this.sessionId = sessionId;

        this.maxPacketSize = maxPacketSize;

        this.prepareBuffers(incomingMessageBuffer, outgoingMessageBuffer, outgoingMessageHeaderBuffer);
    }

    /**
     * Converts the given bytes into chars, then extracts any messages from the chars and enqueues them. Message is
     * copied into the local buffer before any operations are performed.
     * 
     * @param message
     *            the CharBuffer containing one or more messages, or pieces of messages.
     */
    public final void enqueueStringMessage(CharBuffer message) throws CharacterCodingException, BadClientException
    {
        synchronized (incomingMessageBuffer)
        {
            incomingMessageBuffer.append(message);

            // look for HTTP header
            while (incomingMessageBuffer.length() > 0)
            {
                if (endOfFirstHeader == -1)
                    endOfFirstHeader = incomingMessageBuffer.indexOf("\r\n\r\n");

                if (endOfFirstHeader == -1)
                { /*
                     * no header yet; if it's too large, bad client; if it's not too large yet, just exit, it'll get
                     * checked again when more data comes down the pipe
                     */
                    if (incomingMessageBuffer.length() > ServerConstants.MAX_HTTP_HEADER_LENGTH)
                    {
                        // clear the buffer
                        BadClientException e = new BadClientException(this.socket.socket().getInetAddress()
                                .getHostAddress(), "Maximum HTTP header length exceeded. Read "
                                + incomingMessageBuffer.length() + "/" + MAX_HTTP_HEADER_LENGTH);

                        incomingMessageBuffer.delete(0, incomingMessageBuffer.length());

                        throw e;
                    }

                    break;
                }
                /*
                 * we have the end of the first header; either just now or from a prior invocation of this method. If we
                 * have it, but don't have the remaining content length, then we first need to extract that from the
                 * header.
                 */
                if (contentLengthRemaining == -1)
                {
                    try
                    {
                        contentLengthRemaining = ServicesServer.parseHeader(incomingMessageBuffer.substring(0,
                                endOfFirstHeader));

                        /*
                         * if we still don't have the remaining length, then there was a problem
                         */
                        if (contentLengthRemaining == -1)
                        {
                            break;
                        }
                        else if (contentLengthRemaining > maxPacketSize)
                        {
                            throw new BadClientException(this.socket.socket().getInetAddress().getHostAddress(),
                                    "Specified content length too large: " + contentLengthRemaining);
                        }

                        /*
                         * if we got here, endOfFirstHeader is not -1, so we need to add 4 to it to ensure we're just
                         * after all the header (including the four termination markers)
                         */
                        endOfFirstHeader += 4;

                        // done with the header; delete it
                        incomingMessageBuffer.delete(0, endOfFirstHeader);
                    }
                    catch (IllegalStateException e)
                    {
                        throw new BadClientException(this.socket.socket().getInetAddress().getHostAddress(),
                                "Malformed header.");
                    }
                }

                try
                {
                    // see if the incoming buffer has enough characters to
                    // include the specified content length
                    if (incomingMessageBuffer.length() >= contentLengthRemaining)
                    {
                        firstMessageBuffer.append(incomingMessageBuffer.substring(0, contentLengthRemaining));

                        incomingMessageBuffer.delete(0, contentLengthRemaining);

                        // reset to do a new read on the next invocation
                        contentLengthRemaining = -1;
                        endOfFirstHeader = -1;
                    }
                    else
                    {
                        String charsRead = incomingMessageBuffer.toString();
                        firstMessageBuffer.append(charsRead);

                        // indicate that we need to get more from the buffer in
                        // the next invocation
                        contentLengthRemaining -= charsRead.length();
                        endOfFirstHeader = -2;

                        incomingMessageBuffer.delete(0, charsRead.length());
                    }
                }
                catch (NullPointerException e)
                {
                    e.printStackTrace();
                }

                if ((firstMessageBuffer != null) && (firstMessageBuffer.length() > 0) && (contentLengthRemaining == -1))
                { /*
                     * if we've read a complete message, then contentLengthRemaining will be reset to -1
                     */
                    processString(firstMessageBuffer.toString());
                    firstMessageBuffer.delete(0, firstMessageBuffer.length());
                }
            }
        }
    }

    /**
     * Indicates the last System timestamp was when the ContextManager had any activity.
     * 
     * @return the last System timestamp indicating when the ContextManager had any activity.
     */
    public final long getLastActivity()
    {
        return lastActivity;
    }

    /**
     * @return the socket
     */
    public SocketChannel getSocket()
    {
        return socket;
    }

    /**
     * Indicates whether there are any messages queued up to be processed.
     * 
     * isMessageWaiting() should be overridden if getNextRequest() is overridden so that it properly reflects the way
     * that getNextRequest() works; it may also be important to override enqueueRequest().
     * 
     * @return true if getNextRequest() can return a value, false if it cannot.
     */
    public boolean isMessageWaiting()
    {
        return messageWaiting;
    }

    /**
     * Calls processRequest(RequestMessage) on each queued message as they are acquired through getNextRequest() and
     * finishing when isMessageWaiting() returns false.
     * 
     * The functionality of processAllMessagesAndSendResponses() may be overridden by overridding the following methods:
     * isMessageWaiting(), processRequest(RequestMessage), getNextRequest().
     * 
     * @throws BadClientException
     */
    public final void processAllMessagesAndSendResponses() throws BadClientException
    {
        while (isMessageWaiting())
        {
            this.processNextMessageAndSendResponse();
        }
    }

    /**
     * @param socket
     *            the socket to set
     */
    public void setSocket(SocketChannel socket)
    {
        this.socket = socket;
    }

    /**
     * Hook method for having shutdown behavior.
     * 
     * This method is called whenever the client terminates their connection or when the server is shutting down.
     */
    public void shutdown()
    {

    }

    protected abstract void clearOutgoingMessageBuffer(StringBuilder outgoingMessageBuf);

    protected abstract void clearOutgoingMessageHeaderBuffer(StringBuilder outgoingMessageHeaderBuf);

    protected abstract void createHeader(StringBuilder outgoingMessageBuf, StringBuilder outgoingMessageHeaderBuf,
            RequestMessage incomingRequest, ResponseMessage outgoingResponse);

    /**
     * Adds the given request to this's request queue.
     * 
     * enqueueRequest(RequestMessage) is a hook method for ContextManagers that need to implement other functionality,
     * such as prioritizing messages.
     * 
     * If enqueueRequest(RequestMessage) is overridden, the following methods should also be overridden:
     * isMessageWaiting(), getNextRequest().
     * 
     * @param request
     */
    protected void enqueueRequest(RequestMessage request)
    {
        if (requestQueue.offer(request))
        {
            messageWaiting = true;
        }
    }

    /**
     * Returns the next message in the request queue.
     * 
     * getNextRequest() may be overridden to provide specific functionality, such as a priority queue. In this case, it
     * is important to override the following methods: isMessageWaiting(), enqueueRequest().
     * 
     * @return the next message in the requestQueue.
     */
    protected RequestMessage getNextRequest()
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
     * Appends the sender's IP address to the incoming message and calls performService on the given RequestMessage
     * using the local ObjectRegistry.
     * 
     * performService(RequestMessage) may be overridden by subclasses to provide more specialized functionality.
     * Generally, overrides should then call super.performService(RequestMessage) so that the IP address is appended to
     * the message.
     * 
     * @param requestMessage
     * @return
     */
    protected ResponseMessage performService(RequestMessage requestMessage)
    {
        requestMessage.setSender(this.socket.socket().getInetAddress());

        try
        {
            return requestMessage.performService(registry);
        }
        catch (Exception e)
        {
            e.printStackTrace();

            return new BadSemanticContentResponse("The request, " + requestMessage.toString()
                    + " caused an exception on the server.");
        }
    }

    protected abstract void prepareBuffers(StringBuilder incomingMessageBuf, StringBuilder outgoingMessageBuf,
            StringBuilder outgoingMessageHeaderBuf);

    protected abstract void translateResponseMessageToStringBufferContents(RequestMessage requestMessage,
            ResponseMessage responseMessage, StringBuilder messageBuffer) throws XmlTranslationException;

    /**
     * Translates the given XML String into a RequestMessage object.
     * 
     * translateStringToRequestMessage(String) may be overridden to provide specific functionality, such as a
     * ContextManager that does not use XML Strings.
     * 
     * @param messageString -
     *            an XML String representing a RequestMessage object.
     * @return the RequestMessage created by translating messageString into an object.
     * @throws XmlTranslationException
     *             if an error occurs when translating from XML into a RequestMessage.
     * @throws UnsupportedEncodingException
     *             if the String is not encoded properly.
     */
    protected RequestMessage translateStringToRequestMessage(String messageString) throws XmlTranslationException,
            UnsupportedEncodingException
    {
        return (RequestMessage) ElementState.translateFromXMLString(messageString, translationSpace);
    }

    /**
     * Calls processRequest(RequestMessage) on the result of getNextRequest().
     * 
     * In order to override functionality processRequest(RequestMessage) and/or getNextRequest() should be overridden.
     * 
     */
    private final void processNextMessageAndSendResponse()
    {
        this.processRequest(this.getNextRequest());
    }

    /**
     * Calls performService(requestMessage), then converts the resulting ResponseMessage into a String, adds the
     * HTTP-like headers, and passes the completed String to the server backend for sending to the client.
     * 
     * @param request -
     *            the request message to process.
     */
    private final void processRequest(RequestMessage request)
    {
        this.lastActivity = System.currentTimeMillis();

        ResponseMessage response = null;

        if (request == null)
        {
            debug("No request.");
        }
        else
        {
            if (!firstRequestReceived)
            {
                // special processing for InitConnectionRequest
                if (request instanceof InitConnectionRequest)
                {
                    String incomingSessionId = ((InitConnectionRequest) request).getSessionId();

                    if (incomingSessionId == null)
                    { // client is not expecting an old ContextManager
                        response = new InitConnectionResponse((String) this.sessionId);
                    }
                    else
                    { // client is expecting an old ContextManager
                        if (frontend.restoreContextManagerFromSessionId(incomingSessionId, this))
                        {
                            response = new InitConnectionResponse(incomingSessionId);
                        }
                        else
                        {
                            response = new InitConnectionResponse((String) this.sessionId);
                        }
                    }
                }

                firstRequestReceived = true;
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
                    response.setUid(request.getUid());

                    // setup outgoingMessageBuffer
                    this.translateResponseMessageToStringBufferContents(request, response, outgoingMessageBuffer);

                    // setup outgoingMessageHeaderBuffer
                    this.createHeader(outgoingMessageBuffer, outgoingMessageHeaderBuffer, request, response);

                    outgoingChars.clear();
                    outgoingChars.put(outgoingMessageHeaderBuffer.toString());
                    outgoingChars.put(outgoingMessageBuffer.toString());
                    outgoingChars.flip();

                    this.clearOutgoingMessageBuffer(outgoingMessageBuffer);
                    this.clearOutgoingMessageHeaderBuffer(outgoingMessageHeaderBuffer);

                    ByteBuffer outgoingBuffer = encoder.encode(outgoingChars);

                    server.send(this.socket, outgoingBuffer);
                }
                catch (XmlTranslationException e)
                {
                    e.printStackTrace();
                }
                catch (CharacterCodingException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Takes an incoming message in the form of an XML String and converts it into a RequestMessage using
     * translateStringToRequestMessage(String). Then places the RequestMessage on the requestQueue using
     * enqueueRequest().
     * 
     * @param incomingMessage
     * @throws BadClientException
     */
    private final void processString(String incomingMessage) throws BadClientException
    {
        if (show(5))
        {
            debug("processing: " + incomingMessage);
            debug("translationSpace: " + translationSpace.toString());
        }

        RequestMessage request = null;
        try
        {
            request = this.translateStringToRequestMessage(incomingMessage);
        }
        catch (XmlTranslationException e)
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
                debug("ERROR; incoming message could not be translated: " + incomingMessage.substring(0, 50) + "..."
                        + incomingMessage.substring(incomingMessage.length() - 50));
            }
            else
            {
                debug("ERROR; incoming message could not be translated: " + incomingMessage);
            }
            if (++badTransmissionCount >= MAXIMUM_TRANSMISSION_ERRORS)
            {
                throw new BadClientException(this.socket.socket().getInetAddress().getHostAddress(),
                        "Too many Bad Transmissions: " + badTransmissionCount);
            }
            // else
            error("translation failed: badTransmissionCount=" + badTransmissionCount);
        }
        else
        {
            badTransmissionCount = 0;

            synchronized (requestQueue)
            {
                this.enqueueRequest(request);
            }
        }
    }

}