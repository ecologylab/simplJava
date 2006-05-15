/*
 * Created on May 3, 2006
 */
package ecologylab.services.nio;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Iterator;

import ecologylab.generic.ObjectRegistry;
import ecologylab.services.ServerConstants;
import ecologylab.services.ServicesServerBase;
import ecologylab.services.messages.BadSemanticContentResponse;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.ElementState;
import ecologylab.xml.NameSpace;
import ecologylab.xml.XmlTranslationException;

public class ServicesServerNIOSingleThreaded extends ServicesServerBase implements ServerConstants
{
    protected ResponseMessage response       = new BadSemanticContentResponse();

    private RequestMessage    request;

    protected ObjectRegistry  registry;

    private Selector          selector;

    private boolean           running;

    private Iterator          selectedKeyIter;

    private SelectionKey      key            = null;

    private ByteBuffer        rawBytes       = ByteBuffer.allocate(MAX_PACKET_SIZE);

    private CharBuffer        outgoingChars  = CharBuffer.allocate(MAX_PACKET_SIZE);

    // private Charset charset = Charset.forName("ISO-8859-1");
    private Charset           charset        = Charset.forName("ASCII");

    private CharsetDecoder    decoder        = charset.newDecoder();

    private CharsetEncoder    encoder        = charset.newEncoder();

    private StringBuffer      accumulator    = new StringBuffer();

    private int               bytesRead      = 0;

    public ServicesServerNIOSingleThreaded(int portNumber, NameSpace requestTranslationSpace,
            ObjectRegistry objectRegistry) throws IOException, BindException
    {
        super(portNumber, requestTranslationSpace, objectRegistry);

        // acquire the static Selector object
        selector = Selector.open();

        // acquire the static ServerSocketChannel object
        ServerSocketChannel channel = ServerSocketChannel.open();

        // disable blocking
        channel.configureBlocking(false);

        // get the socket associated with the channel
        serverSocket = channel.socket();

        // bind to the port for this server
        serverSocket.bind(new InetSocketAddress(portNumber));

        // register the channel with the selector to look for incoming
        // accept requests
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void run()
    {
        long timer = System.currentTimeMillis();
        long timeDiff;

        while (running)
        {
            try
            {
                // block until some connection has something to do
                if (selector.select() > 0)
                {
                    // get an iterator of the keys that have something to do
                    selectedKeyIter = selector.selectedKeys().iterator();

                    while (selectedKeyIter.hasNext())
                    {
                        // get the key corresponding to the event and process it
                        // appropriately
                        key = (SelectionKey) selectedKeyIter.next();

                        selectedKeyIter.remove();

                        if (key.isAcceptable())
                        { // incoming connection
                            acceptKey(key);
                        }

                        if (key.isReadable())
                        { // incoming message
                            // disable the key for reading; done here to prevent
                            // any issues with hanging select()'s

                            try
                            {
                                while ((bytesRead = ((SocketChannel) key.channel()).read(rawBytes)) > 0)
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
                                                            accumulator.indexOf("\n")), key);

                                                    // System.out.println("time:
                                                    // "+(System.currentTimeMillis()-time));

                                                    // erase the message from the accumulator
                                                    accumulator.delete(0,
                                                            accumulator.indexOf("\n") + 1);
                                                }
                                            }
                                        }
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
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }

                        }
                    }
                } else if ((timeDiff = (System.currentTimeMillis() - timer)) < MAX_TARDINESS)
                {
                    // System.out.println("server waiting: "+timeDiff);

                    synchronized (this)
                    {
                        try
                        {
                            wait(MAX_TARDINESS - timeDiff);
                        } catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                // System.err.println("hi "+(System.currentTimeMillis() -
                // timer));
                timer = System.currentTimeMillis();

            } catch (IOException e)
            {
                this.stop();

                debug("attempted to access selector after it was closed! shutting down");

                e.printStackTrace();
            }

        }

        try
        {
            selector.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    protected SocketChannel acceptKey(SelectionKey key)
    {
        try
        {
            SocketChannel tempChannel = ((ServerSocketChannel) key.channel()).accept();

            tempChannel.configureBlocking(false);

            // when we register, we want to attach the proper
            // session token to all of the keys associated with
            // this connection, so we can sort them out later.
            tempChannel.register(selector, SelectionKey.OP_READ, this
                    .generateSessionToken(tempChannel.socket()));

            System.out.println("Now connected to " + tempChannel);

            return tempChannel;

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Use the ServicesServer and its ObjectRegistry to do the translation. Can be overridden to
     * provide special functionalities
     * 
     * @param messageString
     * @return
     * @throws XmlTranslationException
     */
    protected RequestMessage translateXMLStringToRequestMessage(String messageString)
            throws XmlTranslationException
    {
        return translateXMLStringToRequestMessage(messageString, true);
    }

    public RequestMessage translateXMLStringToRequestMessage(String messageString,
            boolean doRecursiveDescent) throws XmlTranslationException
    {
        return (RequestMessage) ElementState.translateFromXMLString(messageString,
                requestTranslationSpace, doRecursiveDescent);
    }

    private void processString(String incomingMessage, SelectionKey key)
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

                    ((SocketChannel)key.channel()).write(encoder.encode(outgoingChars));

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
        // start the server running
        running = true;

        new Thread(this, "NIO Server running on port " + portNumber).start();
    }

    public synchronized void stop()
    {
        running = false;
    }
}
