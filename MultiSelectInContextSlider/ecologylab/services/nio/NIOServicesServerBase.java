/*
 * Created on May 3, 2006
 */
package ecologylab.services.nio;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import ecologylab.generic.ObjectRegistry;
import ecologylab.services.ServerConstants;
import ecologylab.services.ServicesServerBase;
import ecologylab.xml.NameSpace;

public abstract class NIOServicesServerBase extends ServicesServerBase implements
        ServerConstants
{
    protected Selector                            selector;

    private boolean                             running;

    public NIOServicesServerBase(int portNumber, NameSpace requestTranslationSpace,
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
        while (running)
        {
            try
            {
                // block until some connection has something to do
                if (selector.select() > 0)
                {
                    // get an iterator of the keys that have something to do
                    Iterator selectedKeyIter = selector.selectedKeys().iterator();

                    while (selectedKeyIter.hasNext())
                    {
                        // get the key corresponding to the event and process it
                        // appropriately
                        SelectionKey key = (SelectionKey) selectedKeyIter.next();

                        selectedKeyIter.remove();

                        if (key.isValid())
                        {
                            if (key.isAcceptable())
                            { // incoming connection
                                acceptKey(key);
                            }

                            if (key.isReadable())
                            { // incoming readable key
                                readKey(key);
                            }
                        }
                        else
                        {
                            invalidateKey(key);
                        }
                    }
                }
            }
            catch (IOException e)
            {
                this.stop();

                debug("attempted to access selector after it was closed! shutting down");

                e.printStackTrace();
            }
        }

        try
        {
            selector.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    protected abstract void invalidateKey(SelectionKey key);
    
    protected abstract void readKey(SelectionKey key);
    
    protected SocketChannel acceptKey(SelectionKey key)
    {
        try
        {
            SocketChannel tempChannel = ((ServerSocketChannel) key.channel())
                    .accept();

            tempChannel.configureBlocking(false);

            // when we register, we want to attach the proper
            // session token to all of the keys associated with
            // this connection, so we can sort them out later.
            tempChannel.register(selector, SelectionKey.OP_READ, this
                    .generateSessionToken(tempChannel.socket()));

            System.out.println("Now connected to " + tempChannel);

            return tempChannel;

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
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
