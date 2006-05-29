package ecologylab.services.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.LinkedList;

import ecologylab.generic.Debug;
import ecologylab.generic.ObjectRegistry;
import ecologylab.services.ServerConstants;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.ElementState;
import ecologylab.xml.NameSpace;
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
    private StringBuffer     accumulator    = new StringBuffer(MAX_PACKET_SIZE);

    private int              bytesRead      = 0;

    private SocketChannel    channel;

    private CharsetDecoder   decoder        = Charset.forName(
                                                    CHARACTER_ENCODING)
                                                    .newDecoder();

    protected SelectionKey   key            = null;

    protected boolean        messageWaiting = false;

    private ByteBuffer       rawBytes       = ByteBuffer
                                                    .allocate(MAX_PACKET_SIZE);

    private RequestMessage   request;

    protected LinkedList     requestQueue   = new LinkedList();

    private Object           token          = null;

    private CharBuffer       outgoingChars  = CharBuffer
                                                    .allocate(MAX_PACKET_SIZE);

    private CharsetEncoder   encoder        = Charset.forName(
                                                    CHARACTER_ENCODING)
                                                    .newEncoder();

    protected ObjectRegistry registry;

    /**
     * Used to translate incoming message XML strings into RequestMessages.
     */
    private NameSpace        translationSpace;

    public ContextManager(Object token, SelectionKey key,
            NameSpace translationSpace, ObjectRegistry registry)
    {
        this.token = token;

        this.key = key;

        channel = (SocketChannel) key.channel();

        this.registry = registry;
        this.translationSpace = translationSpace;
    }

    /**
     * @return the next message in the requestQueue.
     */
    protected RequestMessage getNextMessage()
    {
        synchronized (requestQueue)
        {
            int queueSize = requestQueue.size();

            if (queueSize == 0)
            {
                return null;
            }
            else
            {
                if (queueSize == 1)
                {
                    messageWaiting = false;
                }

                return (RequestMessage) requestQueue.removeFirst();
            }
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
        return requestMessage.performService(registry);
    }

    private void processRequest(RequestMessage request)
    {
        ResponseMessage response = null;

        if (show(5))
        {
            try
            {
                debug("processing: " + request.translateToXML(false));
            }
            catch (XmlTranslationException e1)
            {
                e1.printStackTrace();
            }
        }

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

                    // System.out.println("response: "
                    // + response.translateToXML(false));
                    // translate the response and store it, then
                    // encode it and write it
                    outgoingChars.clear();
                    outgoingChars.put(response.translateToXML(false)).put('\n');
                    outgoingChars.flip();

                    channel.write(encoder.encode(outgoingChars));

                }
                catch (XmlTranslationException e)
                {
                    e.printStackTrace();
                }
                catch (CharacterCodingException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
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

    public void processAllMessagesAndSendResponses()
    {
        while (isMessageWaiting())
        {
            this.processNextMessageAndSendResponse();
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
     */
    private void processString(String incomingMessage)
    {
        if (show(5))
            debug("processing: " + incomingMessage);

        try
        {
            request = (RequestMessage) ElementState.translateFromXMLString(
                    incomingMessage, translationSpace);

        }
        catch (XmlTranslationException e)
        {
            e.printStackTrace();
        }

        if (request == null)
        {
            debug("ERROR: translation failed: ");
        }
        else
        {
            synchronized (requestQueue)
            {
                this.enqueueRequest(request);
            }
        }
    }

    protected void enqueueRequest(RequestMessage request)
    {
        requestQueue.add(request);
        messageWaiting = true;
    }

    private void readBytesIntoAccumulator() throws CharacterCodingException
    {
        if (bytesRead < MAX_PACKET_SIZE)
        {
            rawBytes.flip();

            accumulator.append(decoder.decode(rawBytes));

            rawBytes.clear();

            if (accumulator.length() > 0)
            {
                if ((accumulator.charAt(accumulator.length() - 1) == '\n')
                        || (accumulator.charAt(accumulator.length() - 1) == '\r'))
                { // when we have accumulated an entire message,
                    // process it

                    // in case we have several messages that are
                    // split by returns
                    while (accumulator.length() > 0)
                    {
                        // transform the message into a request and
                        // perform the service
                        // long time = System.currentTimeMillis();

                        processString(accumulator.substring(0, accumulator
                                .indexOf("\n")));

                        // System.out.println("time:
                        // "+(System.currentTimeMillis()-time));

                        // erase the message from the accumulator
                        accumulator.delete(0, accumulator.indexOf("\n") + 1);
                    }
                }
            }
        }
        else
        { // TODO might be able to catch too large messages
            // better.
            debug("Packet too large. Terminating connection.");
            key.cancel();
        }
    }

    /**
     * Removes key from the OP_READ interest set of the selector, drains all
     * bytes from it, parses them into messages, then restores the OP_READ
     * interest.
     * 
     * This method blocks and reads one time from the channel.
     */
    public void readChannel()
    {
        // disable reading interest on the key
        key.interestOps(key.interestOps() & (~SelectionKey.OP_READ));

        try
        {
            if ((bytesRead = channel.read(rawBytes)) > 0)
            {
                readBytesIntoAccumulator();
            }
        }
        catch (CharacterCodingException e1)
        {
            e1.printStackTrace();
        }
        catch (IOException e1)
        {
            debug("Connection closed by client. Terminating message processor: "
                    + channel.toString());
            key.cancel();
        }

        // re-enable reading on the key and wake up the selector.
        key.interestOps(key.interestOps() | SelectionKey.OP_READ);
        key.selector().wakeup();
    }

    /**
     * Removes key from the OP_READ interest set of the selector, drains all
     * bytes from it, parses them into messages, then restores the OP_READ
     * interest.
     * 
     * This method blocks and reads until there is nothing left on the channel;
     * 
     * @see readChannel.
     */
    public void readChannelUntilEmpty()
    {
        // disable reading interest on the key
        key.interestOps(key.interestOps() & (~SelectionKey.OP_READ));

        try
        {
            while ((bytesRead = channel.read(rawBytes)) > 0)
            {
                readBytesIntoAccumulator();
            }
        }
        catch (CharacterCodingException e1)
        {
            e1.printStackTrace();
        }
        catch (IOException e1)
        {
            debug("Connection closed by client. Terminating message processor: "
                    + channel.toString());
            key.cancel();
        }

        // re-enable reading on the key and wake up the selector.
        key.interestOps(key.interestOps() | SelectionKey.OP_READ);
        key.selector().wakeup();
    }
}
