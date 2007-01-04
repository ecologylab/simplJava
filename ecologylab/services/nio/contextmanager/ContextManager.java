package ecologylab.services.nio;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.concurrent.LinkedBlockingQueue;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.generic.Debug;
import ecologylab.services.BadClientException;
import ecologylab.services.ServerConstants;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XmlTranslationException;

/**
 * Stores information about the connection context for the client on the server.
 * Should be extended for more specific implementations. Handles accumulating
 * incoming messages and translating them into RequestMessage objects, as well
 * as the ability to perform the messages' services and send their responses.
 * 
 * Generally, this class can be driven by one or more threads, depending on the
 * desired functionality.
 * 
 * @author Zach Toups
 */
public class ContextManager extends Debug implements ServerConstants
{
    private StringBuilder                         accumulator      = new StringBuilder(
                                                                           MAX_PACKET_SIZE);

    protected boolean                             messageWaiting   = false;

    private RequestMessage                        request;

    protected LinkedBlockingQueue<RequestMessage> requestQueue     = new LinkedBlockingQueue<RequestMessage>();

    private Object                                token            = null;

    private CharBuffer                            outgoingChars    = CharBuffer
                                                                           .allocate(MAX_PACKET_SIZE);

    private static CharsetEncoder                 encoder          = Charset
                                                                           .forName(
                                                                                   CHARACTER_ENCODING)
                                                                           .newEncoder();

    protected long                                initialTimeStamp = System
                                                                           .currentTimeMillis();

    protected boolean                             receivedAValidMsg;

    protected ObjectRegistry                      registry;

    private int                                   badTransmissionCount;

    NIOServerBackend                              server;

    protected SocketChannel                                 socket;

    /**
     * Used to translate incoming message XML strings into RequestMessages.
     */
    private TranslationSpace                      translationSpace;

    public ContextManager(Object token, /* SelectionKey key, */
            NIOServerBackend server, SocketChannel socket,
            TranslationSpace translationSpace, ObjectRegistry registry)
    {
        this.token = token;

        this.socket = socket;
        this.server = server;
        // this.key = key;

        // channel = (SocketChannel) key.channel();
        //
        this.registry = registry;
        this.translationSpace = translationSpace;

        System.out.println("my server is: " + this.server);
    }

    /**
     * @return the next message in the requestQueue.
     */
    protected RequestMessage getNextMessage()
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
     * Calls performService on the given RequestMessage using the local
     * ObjectRegistry. Can be overridden by subclasses to provide more
     * specialized functionality.
     * 
     * @param requestMessage
     * @return
     */
    protected ResponseMessage performService(RequestMessage requestMessage)
    {
        requestMessage.setSender(this.socket.socket().getInetAddress());

        return requestMessage.performService(registry);
    }

    private void processRequest(RequestMessage request)
    {
        ResponseMessage response = null;

        // if (show(5))
        // {
        try
        {
            debug("processing: " + request.translateToXML(false));
        }
        catch (XmlTranslationException e1)
        {
            e1.printStackTrace();
        }
        // }

        if (request == null)
        {
            debug("No request.");
        }
        else
        {
            // perform the service being requested
            response = performService(request);

            if (response != null)
            { // if the response is null, then we do nothing else
                try
                {
                    response.setUid(request.getUid());

                    // if (show(5))
                    debug("response: " + response.translateToXML(false));
                    // translate the response and store it, then
                    // encode it and write it
                    outgoingChars.clear();
                    outgoingChars.put(response.translateToXML(false)).put('\n');
                    outgoingChars.flip();

                    ByteBuffer temp = encoder.encode(outgoingChars);

                    System.out.println(outgoingChars.toString());
                    System.out.println("sent: " + temp);

                    server.send(this.socket, temp);

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

    public void processNextMessageAndSendResponse()
    {
        this.processRequest(this.getNextMessage());
    }

    public void processAllMessagesAndSendResponses() throws BadClientException
    {
        timeoutBeforeValidMsg();
        while (isMessageWaiting())
        {
            this.processNextMessageAndSendResponse();
            timeoutBeforeValidMsg();
        }
    }

    /**
     * Checks the time since the last valid message. If the time has been too
     * long (MAX_TIME_BEFORE_VALID_MSG), throws a BadClientException, which the
     * server will deal with.
     * 
     * If the time has not been too long, updates the time stamp.
     * 
     * @throws BadClientException
     */
    void timeoutBeforeValidMsg() throws BadClientException
    {
        long now = System.currentTimeMillis();
        long elapsedTime = now - this.initialTimeStamp;
        if (elapsedTime >= MAX_TIME_BEFORE_VALID_MSG)
        {
            throw new BadClientException(
                    "Too long before valid response: elapsedTime="
                            + elapsedTime + ".");
        }
        else
        {
            this.initialTimeStamp = now;
        }
    }

    /**
     * @return Returns the token.
     */
    public Object getToken()
    {
        return token;
    }

    /**
     * @return Returns the messageWaiting.
     */
    public boolean isMessageWaiting()
    {
        return messageWaiting;
    }

    /**
     * Takes an incoming message in the form of an XML String and converts it
     * into a RequestMessage. Then places the RequestMessage on the
     * requestQueue.
     * 
     * @param incomingMessage
     * @throws BadClientException
     */
    private void processString(String incomingMessage)
            throws BadClientException
    {
        if (show(5))
        {
            debug("processing: " + incomingMessage);
            debug("translationSpace: " + translationSpace.toString());
        }

        request = null;
        try
        {
            request = (RequestMessage) ElementState.translateFromXMLString(
                    incomingMessage, translationSpace);
        }
        catch (XmlTranslationException e)
        {
            // drop down to request == null, below
        }

        if (request == null)
        {
            System.out.println("ERROR: " + incomingMessage);
            if (++badTransmissionCount >= MAXIMUM_TRANSMISSION_ERRORS)
            {
                throw new BadClientException("Too many Bad Transmissions: "
                        + badTransmissionCount);
            }
            // else
            error("ERROR: translation failed: badTransmissionCount="
                    + badTransmissionCount);
        }
        else
        {
            receivedAValidMsg = true;
            badTransmissionCount = 0;

            synchronized (requestQueue)
            {
                this.enqueueRequest(request);
            }
        }
    }

    /**
     * Adds the given request to this's request queue.
     * 
     * enqueueRequest(RequestMessage) is a hook method for ContextManagers that
     * need to implement other functionality, such as prioritizing messages.
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
     * Converts the given bytes into chars, then extracts any messages from the
     * chars and enqueues them.
     * 
     * @param message
     */
    public void enqueueStringMessage(CharBuffer message)
            throws CharacterCodingException, BadClientException
    {
        accumulator.append(message);

        if (show(5))
            System.out.println("accumulator after: " + accumulator.toString());

        int length = accumulator.length();
        int termPos = accumulator.indexOf("\n");
        if (termPos == -1)
        {
            termPos = accumulator.indexOf("\r");
        }

        while ((length > 0) && (termPos > 0))
        { // we have at least one whole message: process it
            // transform the message into a request and
            // perform the service

            processString(accumulator.substring(0, termPos));

            // erase the message from the accumulator
            accumulator.delete(0, termPos + 1);

            length = accumulator.length();
            termPos = accumulator.indexOf("\n");
            if (termPos == -1)
            {
                termPos = accumulator.indexOf("\r");
            }
        }
    }
}
