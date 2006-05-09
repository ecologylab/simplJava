/*
 * Created on May 4, 2006
 */
package ecologylab.services.nio;

import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.LinkedList;

import ecologylab.services.ServerConstants;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.ElementState;
import ecologylab.xml.NameSpace;
import ecologylab.xml.XmlTranslationException;
import ecologylab.generic.Debug;
import ecologylab.generic.ObjectRegistry;

/**
 * Used as a worker thread and client information container.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public class MessageProcessor extends Debug implements Runnable,
        ServerConstants
{
    protected Object               token;

    private Channel                channel;

    private StringBuffer           accumulator  = new StringBuffer();

    protected MessageProcessorPool pool;

    protected ResponseMessage      response;

    private RequestMessage         request;

    private Thread                 thread;

    private ByteBuffer             rawBytes     = ByteBuffer
                                                        .allocate(MAX_PACKET_SIZE);

    // private CharBuffer messageChars = CharBuffer
    // .allocate(MAX_PACKET_SIZE);

    private LinkedList             messageQueue = new LinkedList();

    // private int bytesRead;

    // private Charset charset = Charset.forName("ISO-8859-1");
    private Charset                charset      = Charset.forName("ASCII");

    private CharsetDecoder         decoder      = charset.newDecoder();

    private NameSpace              translationSpace;

    protected ObjectRegistry       registry;

    boolean                        running      = true;

    public MessageProcessor(MessageProcessorPool pool, Channel channel,
            Object token, NameSpace translationSpace, ObjectRegistry registry)
    {
        this.pool = pool;

        this.channel = channel;

        this.token = token;

        this.translationSpace = translationSpace;

        this.registry = registry;
    }

    public void process(SelectionKey key) throws Exception
    {
        if (token.equals(key.attachment()))
        {
            rawBytes.clear();

            // bytesRead = ((SocketChannel) key.channel()).read(rawBytes);
            ((SocketChannel) key.channel()).read(rawBytes);

            rawBytes.flip();

            if (show(5))
                debug("got raw message: " + rawBytes.remaining());

            accumulator.append(decoder.decode(rawBytes).toString());

            if (accumulator.length() > 0)
            {
                if ((accumulator.charAt(accumulator.length() - 1) == '\n')
                        || (accumulator.charAt(accumulator.length() - 1) == '\r'))
                { // when we have accumulated an entire message,
                    // process it

                    messageQueue.add(accumulator.toString());

                    if (thread == null)
                    {
                        thread = new Thread(this, "Message Processor for "
                                + token);
                        thread.start();
                    }

                    synchronized (this)
                    {
                        notify();
                    }
                    // clear the accumulator
                    // accumulator = new StringBuffer();
                    accumulator.delete(0, accumulator.length());

                }
            }
        } else
        {
            throw new Exception("Token mismatch!");
        }
    }

    /**
     * Calls performService on the given RequestMessage using the local
     * ObjectRegistry.
     * 
     * Can be overridden by subclasses to provide more specialized
     * functionality.
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
    public void run()
    {
        while (running)
        {
            while (messageQueue.size() > 0)
            {
                try
                {
                    //String temp = (String) messageQueue
//                    .removeFirst();
                    
//                    System.out.println(temp);
                    
  //                  request = translateXMLStringToRequestMessage(temp);
                    request = translateXMLStringToRequestMessage((String) messageQueue.removeFirst());
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
                    
                    pool.messageProcessed(response, channel);

                    // TODO bad transmissions
                }
            }

            try
            {
                synchronized (this)
                {
                    while (messageQueue.size() == 0)
                        wait();
                }
            } catch (InterruptedException e)
            {
            }
        }
    }
}
