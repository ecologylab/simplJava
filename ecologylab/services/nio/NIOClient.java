/*
 * Created on May 12, 2006
 */
package ecologylab.services.nio;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.PortUnreachableException;
import java.net.SocketException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.services.ClientConstants;
import ecologylab.services.ServerConstants;
import ecologylab.services.ServicesClientBase;
import ecologylab.services.ServicesServer;
import ecologylab.services.messages.InitConnectionRequest;
import ecologylab.services.messages.InitConnectionResponse;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XMLTranslationException;

/**
 * Services Client using NIO; a major difference with the NIO version is state tracking. Since the sending methods do
 * not wait for the server to return.
 * 
 * This object will listen for incoming messages from the server, and will send any messages that it recieves on its
 * end.
 * 
 * Since the underlying implementation is TCP/IP, messages sent should be sent in order, and the responses should match
 * that order.
 * 
 * Another major difference between this and the non-NIO version of ServicesClient is that it is StartAndStoppable.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public class NIOClient extends ServicesClientBase implements Runnable, ServerConstants, ClientConstants
{
    protected Selector                    selector                     = null;

    protected boolean                     running                      = false;

    protected SocketChannel               channel                      = null;

    private Thread                        thread;

    protected SelectionKey                key                          = null;

    private final ByteBuffer              incomingRawBytes             = ByteBuffer.allocate(MAX_PACKET_SIZE);

    protected final CharBuffer            outgoingChars                = CharBuffer.allocate(MAX_PACKET_SIZE);

    private final StringBuilder           accumulator                  = new StringBuilder(MAX_PACKET_SIZE);

    private final StringBuilder           requestBuffer                = new StringBuilder(MAX_PACKET_SIZE);

    private static final CharsetDecoder   DECODER                      = Charset.forName(ServerConstants.CHARACTER_ENCODING).newDecoder();

    protected static final CharsetEncoder ENCODER                      = Charset.forName(ServerConstants.CHARACTER_ENCODING).newEncoder();

    private ResponseMessage               responseMessage              = null;

    protected Iterator<SelectionKey>      incoming;

    private volatile boolean              blockingRequestPending       = false;

    private final Queue<ResponseMessage>  blockingResponsesQueue       = new LinkedBlockingQueue<ResponseMessage>();

    protected final Queue<PreppedRequest> requestsQueue                = new LinkedBlockingQueue<PreppedRequest>();

    /**
     * A map that stores all the requests that have not yet gotten responses. Maps UID to RequestMessage.
     */
    protected Map<Long, PreppedRequest>   unfulfilledRequests          = new HashMap<Long, PreppedRequest>();

    /**
     * The number of times a call to reconnect() should attempt to contact the server before giving up and calling
     * stop().
     */
    protected int                         reconnectAttempts            = RECONNECT_ATTEMPTS;

    /**
     * The number of milliseconds to wait between reconnect attempts.
     */
    protected int                         waitBetweenReconnectAttempts = WAIT_BEWTEEN_RECONNECT_ATTEMPTS;

    private String                        sessionId                    = null;

    /**
     * selectInterval is passed to select() when it is called in the run loop. It is set to 0 indicating that the loop
     * should block until the selector picks up something interesting. However, if this class is subclassed, it is
     * possible to modify this value so that the select() will only block for the number of ms supplied by this field.
     * Thus, it is possible (by also subclassing the sendData() method) to have this send data on an interval, and then
     * select.
     */
    protected long                        selectInterval               = 0;

    protected boolean                     isSending                    = false;

    public NIOClient(String server, int port, TranslationSpace messageSpace, ObjectRegistry<?> objectRegistry)
    {
        super(server, port, messageSpace, objectRegistry);
    }

    /**
     * If this client is not already connected, connects to the specified server on the specified port, then calls
     * start() to begin listening for server responses and processing them, then sends handshake data and establishes
     * the session id.
     * 
     * @see ecologylab.services.ServicesClientBase#connect()
     */
    @Override public boolean connect()
    {
        debug("initializing connection...");
        if (super.connect())
        {

            debug("starting listener thread...");
            this.start();

            // now send first handshake message
            ResponseMessage initResponse = this.sendMessage(new InitConnectionRequest(this.sessionId));

            if (initResponse instanceof InitConnectionResponse)
            {
                if (this.sessionId == null)
                {
                    debug("new session...");
                    this.sessionId = ((InitConnectionResponse) initResponse).getSessionId();
                    debug(this.sessionId);
                }
                else if (this.sessionId == ((InitConnectionResponse) initResponse).getSessionId())
                {
                    debug("reconnected and restored previous connection: " + this.sessionId);
                }
                else
                {
                    String newId = ((InitConnectionResponse) initResponse).getSessionId();
                    debug("unable to restore previous session, " + this.sessionId + "; new session: " + newId);
                    this.unableToRestorePreviousConnection(this.sessionId, newId);
                    this.sessionId = newId;
                }
            }
        }

        debug("connected? " + this.connected());
        return connected();
    }

    /**
     * Sets the UID for request (if necessary), enqueues it then registers write interest for the NIOClient's selection
     * key and calls wakeup() on the selector.
     * 
     * @param request
     * @throws XMLTranslationException
     */
    protected PreppedRequest prepareAndEnqueueRequestForSending(RequestMessage request) throws XMLTranslationException
    {
        long uid = request.getUid();
        if (uid == 0)
        {
            uid = this.generateUid();
            request.setUid(uid);
        }

        // fill requestBuffer
        request.translateToXML(requestBuffer);

        PreppedRequest pReq = new PreppedRequest(requestBuffer.toString(), uid);

        requestBuffer.delete(0, requestBuffer.length());

        enqueueRequestForSending(pReq);

        return pReq;
    }

    protected void enqueueRequestForSending(PreppedRequest request)
    {
        synchronized (requestsQueue)
        {
            requestsQueue.add(request);
        }

        key.interestOps(key.interestOps() | (SelectionKey.OP_WRITE));

        selector.wakeup();
    }

    public void disconnect(boolean waitForResponses)
    {
        while (this.requestsRemaining() > 0 && this.connected() && waitForResponses)
        {
            debug("*******************Request queue not empty, finishing " + requestsRemaining()
                    + " messages before disconnecting...");
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

        debug("*******************disconnecting...");

        try
        {
            if (connected())
            {
                debug("*******************client is connected...");

                while (waitForResponses && connected() && !this.shutdownOK())
                {
                    debug("*******************" + this.unfulfilledRequests.size()
                            + " requests still pending response from server.");
                    debug("*******************connected: " + connected());

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

                debug("*******************shutting down output.");
                // shut down output
                channel.socket().shutdownOutput();

                debug("*******************closing down output.");
                // now that there's nothing coming back, shut down input
                channel.socket().shutdownInput();

                debug("*******************close down all.");
                // close it all out
                channel.close();
                channel.keyFor(selector).cancel();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            debug("null out");
            nullOut();

            stop();
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
        debug("null out");

        socket = null;
        channel = null;
        selector = null;
        key = null;
    }

    @Override public boolean connected()
    {
        return (channel != null) && !channel.isConnectionPending() && channel.isConnected() && socket.isConnected();
    }

    /**
     * Side effect of calling start().
     */
    @Override protected boolean createConnection()
    {
        try
        {
            // get the selector
            selector = Selector.open();

            // create the channel and connect it to the server
            channel = SocketChannel.open(new InetSocketAddress(server, port));

            // disable blocking
            channel.configureBlocking(false);

            // link in the socket from the channel
            socket = channel.socket();

            if (connected())
            {
                // register the channel for read operations, now that it is
                // connected
                channel.register(selector, SelectionKey.OP_READ);
                // channel.register(selector, SelectionKey.OP_WRITE);

                this.key = channel.keyFor(selector);
            }
        }
        catch (BindException e)
        {
            debug("Couldnt create socket connection to server '" + server + "': " + e);

            nullOut();
        }
        catch (PortUnreachableException e)
        {
            debug("Server is alive, but has no daemon on port " + port + ": " + e);

            nullOut();
        }
        catch (SocketException e)
        {
            debug("Server '" + server + "' unreachable: " + e);

            nullOut();
        }
        catch (IOException e)
        {
            debug("Bad response from server: " + e);

            nullOut();
        }

        return connected();
    }

    /**
     * Hook method to allow subclasses to deal with a failed restore after disconnect. This should be a rare occurance,
     * but some sublcasses may need to deal with this case specifically.
     * 
     * @param oldId -
     *            the previous session id.
     * @param newId -
     *            the new session id given by the server after reconnect.
     */
    protected void unableToRestorePreviousConnection(String oldId, String newId)
    {
    }

    /**
     * Sends request, but does not wait for the response. The response gets processed later in a non-stateful way by the
     * run method.
     * 
     * @param request
     *            the request to send to the server.
     * 
     * @return the UID of request.
     */
    public PreppedRequest nonBlockingSendMessage(RequestMessage request) throws IOException
    {
        if (connected())
        {
            try
            {
                return this.prepareAndEnqueueRequestForSending(request);
            }
            catch (XMLTranslationException e)
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
     * @see ecologylab.services.ServicesClientBase#sendMessage(ecologylab.services.messages.RequestMessage)
     */
    @Override public synchronized ResponseMessage sendMessage(RequestMessage request)
    {
        return this.sendMessage(request, -1);
    }

    /**
     * Blocking send with timeout. Sends the request and waits timeOutMillis milliseconds for the response, which it
     * returns. sendMessage(RequestMessage, int) will return null if no message was recieved in time.
     * 
     * @param request
     * @param timeOutMillis
     * @return
     */
    public synchronized ResponseMessage sendMessage(RequestMessage request, int timeOutMillis)
    {
        ResponseMessage returnValue = null;

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
        catch (XMLTranslationException e1)
        {
            error("error translating to XML; returning null");
            e1.printStackTrace();

            return null;
        }

        if (request instanceof InitConnectionRequest)
        {
            debug("init request: " + ((InitConnectionRequest) request).getSessionId());
        }

        // wait to be notified that the response has arrived
        while (blockingRequestPending && !blockingRequestFailed)
        {
            debug("waiting on blocking request");

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

            debug("waking");

            timeCounter += System.currentTimeMillis() - startTime;
            startTime = System.currentTimeMillis();

            while ((blockingRequestPending) && (!blockingResponsesQueue.isEmpty()))
            {
                returnValue = blockingResponsesQueue.poll();

                if (returnValue.getUid() == currentMessageUid)
                {
                    debug("got the right response");

                    blockingRequestPending = false;

                    blockingResponsesQueue.clear();

                    return returnValue;
                }
                else
                {
                    returnValue = null;
                }
            }

            if ((timeOutMillis > -1) && (timeCounter >= timeOutMillis) && (blockingRequestPending))
            {
                blockingRequestFailed = true;
            }
        }

        if (blockingRequestFailed)
        {
            debug("Request failed due to timeout!");
        }

        return returnValue;
    }

    protected void start()
    {
        if (connected())
        {
            running = true;

            if (thread == null)
            {
                thread = new Thread(this, "client network handler to " + this.server + ":" + this.port);
                thread.start();
            }
        }
    }

    protected void stop()
    {
        System.err.println("shutting down client listening thread.");

        running = false;

        // dispose of thread
        thread = null;
    }

    public final void run()
    {
        System.out.println("starting up run method.");
        while (running)
        {
            try
            {
                if (connected())
                {
                    if (selector.select() > 0)
                    {
                        // there is something to read; only register one
                        // channel, so...
                        incoming = selector.selectedKeys().iterator();

                        while (incoming.hasNext())
                        {
                            key = incoming.next();

                            incoming.remove();

                            if (key.isValid())
                            {
                                if (key.isReadable())
                                {
                                    readChannel();
                                }
                                else if (key.isWritable())
                                {
                                    // lock outgoing requests queue, send data from it, then switch out of write mode
                                    synchronized (requestsQueue)
                                    {
                                        while (this.requestsRemaining() > 0)
                                        {
                                            this.createPacketFromMessageAndSend(this.dequeueRequest(), key);
                                        }
                                    }

                                    key.interestOps(key.interestOps() & (~SelectionKey.OP_WRITE));
                                }
                            }
                            else
                            { // the key is invalid; server disconnected unexpectedly
                                debug("server disconnected...");
                                this.disconnect(false);
                                this.reconnect();
                            }
                        }
                    }
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        System.out.println("Thread shutting down.");

    }

    /**
     * Returns the next request in the request queue and removes it from that queue. Sublcasses that override the queue
     * functionality will need to override this method.
     * 
     * @return the next request in the request queue.
     */
    protected PreppedRequest dequeueRequest()
    {
        return this.requestsQueue.poll();
    }

    /**
     * Returns the number of requests remaining in the requests queue. Subclasses that override the queue functionality
     * will need to change this method accordingly.
     * 
     * @return the size of the request queue.
     */
    protected int requestsRemaining()
    {
        return this.requestsQueue.size();
    }

    protected void readChannel()
    {
        int bytesRead = 0;

        try
        {
            while ((bytesRead = channel.read(incomingRawBytes)) > 0)
            { // read until there's nothing left to read

                // decode bytes and store in accumulator
                incomingRawBytes.flip();
                accumulator.append(DECODER.decode(incomingRawBytes));
                incomingRawBytes.clear();

                // find the first header
                int endOfFirstHeader = accumulator.indexOf(HTTP_HEADER_TERMINATOR);

                if ((accumulator.length() > 0) && (endOfFirstHeader != -1))
                {
                    int contentLength = ServicesServer.parseHeader(accumulator.substring(0, endOfFirstHeader));

                    if (contentLength == -1)
                        break;

                    String firstMessage = null;

                    int totalSize = endOfFirstHeader + 4 + contentLength;

                    if (accumulator.length() >= totalSize)
                    {
                        firstMessage = accumulator.substring(endOfFirstHeader + 4, totalSize);
                        accumulator.delete(0, totalSize);
                    }
                    else
                    {
                        endOfFirstHeader = -1;
                        break;
                    }

                    if (firstMessage != null)
                    {
                        // we got a response
                        if (!this.blockingRequestPending)
                        {
                            processString(firstMessage);
                        }
                        else
                        {
                            blockingResponsesQueue.add(processString(firstMessage));
                            synchronized (this)
                            {
                                notify();
                            }
                        }
                    }

                    endOfFirstHeader = accumulator.indexOf(HTTP_HEADER_TERMINATOR);
                }
            }

            if (bytesRead == -1)
            {
                debug("Read returned -1; disconnecting...");

                disconnect(false);

                debug("attempting re-connect...");
            }
        }
        catch (CharacterCodingException e1)
        {
            e1.printStackTrace();
        }
        catch (IOException e1)
        {
            debug("IOException");

            if ("An existing connection was forcibly closed by the remote host".equals(e1.getMessage()))
            {
                debug("Server shut down; disconnecting.");

                disconnect(false);
            }
        }
    }

    /**
     * Attempts to reconnect this client if it has been disconnected. After reconnecting, re-queues all requests still
     * in the unfulfilledRequests map.
     * 
     * If the attempt to reconnect fails, reconnect() will attempt a number of times equal to reconnectAttempts, waiting
     * waitBetweenReconnectAttempts milliseconds between attempts. If all such attempts fail, calls stop() on this to
     * shut down the client. The client will then need to be re-started manually.
     * 
     */
    protected void reconnect()
    {
        debug("attempting to reconnect...");
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
                List<PreppedRequest> rerequests = new LinkedList<PreppedRequest>(this.unfulfilledRequests.values());

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
     * Stores the request in the unfulfilledRequests map according to its UID, converts it to XML, prepends the
     * HTTP-like header, then writes it out to the channel. Then re-registers key for reading.
     * 
     * @param pReq
     */
    private void createPacketFromMessageAndSend(PreppedRequest pReq, SelectionKey incomingKey)
    {
        String outgoingReq = pReq.getRequest();

        this.addUnfulfilledRequest(pReq);

        try
        {
            StringBuilder message = new StringBuilder(CONTENT_LENGTH_STRING + ":" + outgoingReq.length()
                    + HTTP_HEADER_TERMINATOR + outgoingReq);

            outgoingChars.clear();

            int capacity;

            while (message.length() > 0)
            {
                outgoingChars.clear();
                capacity = outgoingChars.capacity();

                if (message.length() > capacity)
                {
                    outgoingChars.put(message.toString(), 0, capacity);
                    message.delete(0, capacity);
                }
                else
                {
                    outgoingChars.put(message.toString());
                    message.delete(0, message.length());
                }

                outgoingChars.flip();

                channel.write(ENCODER.encode(outgoingChars));
            }
        }
        catch (ClosedChannelException e)
        {
            debug("connection severed; disconnecting and storing requests...");
            this.disconnect(false);

            this.reconnect();
        }
        catch (BufferOverflowException e)
        {
            debug("buffer overflow.");
            e.printStackTrace();
            System.out.println("capacity: " + outgoingChars.capacity());
            System.out.println("outgoing request: " + outgoingReq);
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

            this.reconnect();
        }

        incomingKey.interestOps(incomingKey.interestOps() & (~SelectionKey.OP_WRITE));
    }

    /**
     * Converts incomingMessage to a ResponseMessage, then processes the response and removes its UID from the
     * unfulfilledRequests map.
     * 
     * @param incomingMessage
     * @return
     */
    private ResponseMessage processString(String incomingMessage)
    {
        if (show(5))
            debug("incoming message: " + incomingMessage);

        try
        {
            responseMessage = translateXMLStringToResponseMessage(incomingMessage);
        }
        catch (XMLTranslationException e)
        {
            e.printStackTrace();
        }

        if (responseMessage == null)
        {
            debug("ERROR: translation failed: ");
        }
        else
        {
            // perform the service being requested
            processResponse(responseMessage);

            synchronized (unfulfilledRequests)
            {
                unfulfilledRequests.remove(responseMessage.getUid());
            }
        }

        // debug("just translated response: "+responseMessage.getUid());

        return responseMessage;
    }

    @Override public void disconnect()
    {
        disconnect(true);
    }

    /**
     * @param reconnectAttempts
     *            the reconnectAttempts to set
     */
    public void setReconnectAttempts(int reconnectAttempts)
    {
        this.reconnectAttempts = reconnectAttempts;
    }

    /**
     * @param waitBetweenReconnectAttempts
     *            the waitBetweenReconnectAttempts to set
     */
    public void setWaitBetweenReconnectAttempts(int waitBetweenReconnectAttempts)
    {
        this.waitBetweenReconnectAttempts = waitBetweenReconnectAttempts;
    }

    protected void clearSessionId()
    {
        this.sessionId = null;
    }
}
