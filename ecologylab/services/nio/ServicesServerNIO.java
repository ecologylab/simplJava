/*
 * Created on May 3, 2006
 */
package ecologylab.services.nio;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;

import ecologylab.generic.ObjectRegistry;
import ecologylab.services.ServerConstants;
import ecologylab.services.ServicesServerBase;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.NameSpace;
import ecologylab.xml.XmlTranslationException;

public class ServicesServerNIO extends ServicesServerBase implements
        ServerConstants
{
    private Selector               selector;

    private boolean                running;

    protected MessageProcessorPool messageProcessors;

    private ByteBuffer             buffer  = ByteBuffer
                                                   .allocate(MAX_PACKET_SIZE);

    // private Charset charset = Charset.forName("ISO-8859-1");
    private Charset                charset = Charset.forName("ASCII");

    private CharsetEncoder         encoder = charset.newEncoder();

    private Iterator               selectedKeyIter;

    public ServicesServerNIO(int portNumber, NameSpace requestTranslationSpace,
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
        System.err.println("Asdf");
        serverSocket.bind(new InetSocketAddress(portNumber));

        // register the channel with the selector to look for incoming
        // accept requests
        channel.register(selector, SelectionKey.OP_ACCEPT);

        messageProcessors = new MessageProcessorPool(this);
    }

    public void run()
    {
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
                        SelectionKey key = (SelectionKey) selectedKeyIter
                                .next();

                        selectedKeyIter.remove();

                        if (key.isAcceptable())
                        { // incoming connection
                            SocketChannel tempChannel = ((ServerSocketChannel) key
                                    .channel()).accept();

                            tempChannel.configureBlocking(false);

                            // when we register, we want to attach the proper
                            // session token to all of the keys associated with
                            // this connection, so we can sort them out later.
                            tempChannel.register(selector,
                                    SelectionKey.OP_READ, this
                                            .generateSessionToken(tempChannel
                                                    .socket()));

                            System.out.println("Now connected to "
                                    + tempChannel);
                        }

                        if (key.isReadable() && key.isValid())
                        { // incoming message
                            messageProcessors.addKey(key);
                        }
                    }
                }
            } catch (IOException e)
            {
                this.stop();

                debug("attempted to access selector after it was closed! shutting down");

                e.printStackTrace();
            }

        }
    }

    /**
     * Clears this.buffer, then translates responseMessage to XML and writes it
     * to the buffer. Writes the buffer to channel and clears it.
     * 
     * @param responseMessage
     * @param channel
     */
    protected void sendResponse(ResponseMessage responseMessage, Channel channel)
    {
//        long sendResponseTime = System.currentTimeMillis();
        buffer.clear();

        try
        {
            if (responseMessage != null)
            {
                String tempMsg = responseMessage.translateToXML(false);

                // System.out.println("sending: "+tempMsg);

                tempMsg = tempMsg.concat("\n");

                buffer.put(encoder.encode(CharBuffer.wrap(tempMsg)));
                buffer.flip();

  //              long writeTime = System.currentTimeMillis();
                ((SocketChannel) channel).write(buffer);
//                System.err.println("time to use channel.write(): "
    //                    + (System.currentTimeMillis() - writeTime));
            }
        } catch (XmlTranslationException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        buffer.clear();
//        System.err.println("Total time for sendResponse(): "
  //              + (System.currentTimeMillis() - sendResponseTime));
    }

    public boolean start()
    {
        // start the server running
        running = true;

        new Thread(this, "NIO Server running on port " + portNumber).start();

        return true;
    }

    public synchronized boolean stop()
    {
        try
        {
            running = false;

            selector.close();

            return true;

        } catch (IOException e)
        {
            e.printStackTrace();

            return false;
        }
    }
}
