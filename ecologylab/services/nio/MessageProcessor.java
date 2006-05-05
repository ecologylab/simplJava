/*
 * Created on May 4, 2006
 */
package ecologylab.services.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
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
    private Thread                 thread       = null;

    protected Object               token;

    private Channel                channel;

    private StringBuffer           accumulator  = new StringBuffer();

    protected MessageProcessorPool pool;

    private LinkedList             keysQueue    = new LinkedList();

    protected ResponseMessage      response;

    private RequestMessage         request;

    private volatile boolean       running      = false;

    private volatile boolean       active       = false;

    private ByteBuffer             rawBytes     = ByteBuffer
                                                        .allocate(MAX_PACKET_SIZE);

    private CharBuffer             messageChars = CharBuffer
                                                        .allocate(MAX_PACKET_SIZE);

    private SelectionKey           tempKey;

    // private Charset charset = Charset.forName("ISO-8859-1");
    private Charset                charset      = Charset.forName("ASCII");

    private CharsetDecoder         decoder      = charset.newDecoder();

    private NameSpace              translationSpace;

    protected ObjectRegistry       registry;

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
            // add to the queue; if we are not running, then start
            synchronized (keysQueue)
            {
                keysQueue.add(key);
            }

            if (thread == null)
            {
                thread = new Thread(this, "Message Processor for " + token);
                thread.start();
            }

            if (!running)
            {
                running = true;
                
                synchronized (this)
                {
                    notify();
                }
            }

        } else
        {
            throw new Exception("Token mismatch!");
        }

    }

    public void run()
    {
        running = true;
        active = true;

        response = null;
        request = null;
        
//        long time = System.currentTimeMillis();

        while (active)
        {
            while (keysQueue.size() > 0)
            {
//                if (accumulator.length() == 0)
  //              {
    //                time = System.currentTimeMillis();
      //          }
                
                // take the first key off the queue, read its buffer
                synchronized (keysQueue)
                {
                    tempKey = (SelectionKey) keysQueue.removeFirst();

                    try
                    {
                        ((SocketChannel) tempKey.channel()).read(rawBytes);
                    } catch (IOException e1)
                    {
                        e1.printStackTrace();
                    }
                }

                if (show(5))
                    debug("got raw message: " + rawBytes.remaining());

                try
                {
                    rawBytes.flip();

                    messageChars = decoder.decode(rawBytes);

                    accumulator = accumulator.append(messageChars.toString());

                    if (accumulator.length() > 0)
                    {
                        if ((accumulator.charAt(accumulator.length() - 1) == '\n')
                                || (accumulator
                                        .charAt(accumulator.length() - 1) == '\r'))
                        { // when we have accumulated an entire message,
                            // process
                            // it
                            // System.out.println("recieved: "
                            // + accumulator.toString());

                            request = translateXMLStringToRequestMessage(accumulator
                                    .toString());

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

//                            System.out.println(System.currentTimeMillis() - time);
                            
                            // clear the accumulator
                            accumulator = new StringBuffer();

                        }
                    }

                    // clear the buffers
                    rawBytes.clear();
                    messageChars.clear();

                } catch (XmlTranslationException e)
                {
                    e.printStackTrace();
                } catch (CharacterCodingException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            running = false;

            try {
                    synchronized(this) {
                        while (!running)
                            wait();
                    }
            } catch (InterruptedException e){
            }

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
}
