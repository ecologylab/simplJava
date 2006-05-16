/*
 * Created on May 4, 2006
 */
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

import ecologylab.services.ServerConstants;
import ecologylab.services.messages.BadSemanticContentResponse;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.ElementState;
import ecologylab.xml.NameSpace;
import ecologylab.xml.XmlTranslationException;
import ecologylab.generic.Debug;
import ecologylab.generic.ObjectRegistry;
import ecologylab.generic.StartAndStoppable;

/**
 * Used as a worker thread and client information container.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public class MessageProcessor extends Debug implements Runnable,
        ServerConstants, StartAndStoppable
{
    private SocketChannel     channel;

    private StringBuffer      accumulator    = new StringBuffer();

    protected ResponseMessage response       = new BadSemanticContentResponse();

    private RequestMessage    request;

    private Thread            thread;

    private ByteBuffer        rawBytes       = ByteBuffer
                                                     .allocate(MAX_PACKET_SIZE);

    private CharBuffer        outgoingChars  = CharBuffer
                                                     .allocate(MAX_PACKET_SIZE);

    // private Charset charset = Charset.forName("ISO-8859-1");
    private Charset           charset        = Charset.forName("ASCII");

    private CharsetDecoder    decoder        = charset.newDecoder();

    private CharsetEncoder    encoder        = charset.newEncoder();

    private NameSpace         translationSpace;

    protected ObjectRegistry  registry;

    private boolean           running        = false;

    protected SelectionKey    key            = null;

    private boolean           messageWaiting = false;

    private int               bytesRead      = 0;

    public MessageProcessor(SelectionKey key, NameSpace translationSpace,
            ObjectRegistry registry)
    {
        this.key = key;

        channel = (SocketChannel) key.channel();

        this.translationSpace = translationSpace;

        this.registry = registry;
    }

    /**
     * Disables read notifications on the key associated with this, then
     * notifies the run method to process it.
     * 
     * @throws Exception
     */
    public synchronized void process() throws Exception
    {
        this.start();

        // tell the run method to wake up.
        messageWaiting = true;

        notify();
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

    /**
     * Use the ServicesServer and its ObjectRegistry to do the translation. Can
     * be overridden to provide special functionalities
     * 
     * @param messageString
     * @return
     * @throws XmlTranslationException
     */
    protected RequestMessage translateXMLStringToRequestMessage(
            String messageString) throws XmlTranslationException
    {
        return translateXMLStringToRequestMessage(messageString, true);
    }

    public RequestMessage translateXMLStringToRequestMessage(
            String messageString, boolean doRecursiveDescent)
            throws XmlTranslationException
    {
        return (RequestMessage) ElementState.translateFromXMLString(
                messageString, translationSpace, doRecursiveDescent);
    }

    /**
     * Processes the next String in the messageQueue, sleeps when there are none
     * left.
     */
    public synchronized void run()
    {
        while (running)
        {
            try
            {
                while ((bytesRead = channel.read(rawBytes)) > 0)
                {
                    if (bytesRead < MAX_PACKET_SIZE)
                    {
                        rawBytes.flip();

                        accumulator.append(decoder.decode(rawBytes));

                        rawBytes.clear();

                        if (accumulator.length() > 0)
                        {
                            if ((accumulator.charAt(accumulator.length() - 1) == '\n')
                                    || (accumulator
                                            .charAt(accumulator.length() - 1) == '\r'))
                            { // when we have accumulated an entire message,
                                // process it

                                // in case we have several messages that are
                                // split by returns
                                while (accumulator.length() > 0)
                                {
                                    // transform the message into a request and
                                    // perform the service
                                    // long time = System.currentTimeMillis();

                                    processString(accumulator.substring(0,
                                            accumulator.indexOf("\n")));

                                    // System.out.println("time:
                                    // "+(System.currentTimeMillis()-time));

                                    // erase the message from the accumulator
                                    accumulator.delete(0, accumulator
                                            .indexOf("\n") + 1);
                                }
                            }
                        }

                        messageWaiting = false;
                    } else
                    { // TODO might be able to catch too large messages
                        // better.
                        debug("Packet too large. Terminating connection.");
                        running = false;
                        break;
                    }
                }
            } catch (CharacterCodingException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e1)
            {
                debug("Connection closed by client. Terminating message processor: "+channel.toString());
                this.stop();
            }

            // re-enable reading on the key and wake up the selector.
            key.interestOps(key.interestOps() | SelectionKey.OP_READ);
            key.selector().wakeup();

            try
            {
                while (!messageWaiting)
                {
                    wait();
                }
            } catch (InterruptedException e)
            {
                e.printStackTrace();
                thread.interrupted();
            }
        }
        // }
    }

    private void processString(String incomingMessage)
    {
        try
        {
            request = translateXMLStringToRequestMessage(incomingMessage);

        } catch (XmlTranslationException e)
        {
            e.printStackTrace();
        }

        if (request == null)
        {
            debug("ERROR: translation failed: ");

        } else
        {
            // perform the service being requested
            response = performService(request);

            if (response != null)
            { // if the response is null, then we do nothing else
                try
                {
                    // System.out.println("response: "
                    // + response.translateToXML(false));
                    // translate the response and store it, then
                    // encode it and write it
                    outgoingChars.clear();
                    outgoingChars.put(response.translateToXML(false)).put('\n');
                    outgoingChars.flip();

                    channel.write(encoder.encode(outgoingChars));

                } catch (XmlTranslationException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (CharacterCodingException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
    }

    public void start()
    {
        running = true;
        
        if (thread == null)
        {
            thread = new Thread(this, "Message Processor for "
                    + key.attachment());
            thread.start();
        }
    }

    public void stop()
    {
        running = false;
    }
    
}
