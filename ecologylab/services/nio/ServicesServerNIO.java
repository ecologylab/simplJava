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
import java.util.HashMap;
import java.util.Iterator;

import ecologylab.generic.ObjectRegistry;
import ecologylab.services.ServerConstants;
import ecologylab.services.ServicesServerBase;
import ecologylab.xml.NameSpace;

public class ServicesServerNIO extends ServicesServerBase implements
        ServerConstants
{
    private Selector     selector;

    private boolean      running;

    private Iterator     selectedKeyIter;

    private SelectionKey key  = null;

    protected HashMap    pool = new HashMap();

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

                        if (key.isValid())
                        {
                            if (key.isAcceptable())
                            { // incoming connection
                                acceptKey(key);
                            }

                            if (key.isReadable())
                            { // incoming message
                                // disable the key for reading; done here to
                                // prevent
                                // any issues with hanging select()'s
                                key.interestOps(key.interestOps()
                                        & (~SelectionKey.OP_READ));

                                if (key.attachment() != null)
                                {
                                    if (!pool.containsKey(key.attachment()))
                                    {
                                        placeKeyInPool(key);
                                    }

                                    try
                                    {
                                        ((MessageProcessor) pool.get(key
                                                .attachment())).process();
                                    } catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                } else
                                {
                                    debug("Null token!");
                                }
                                // messageProcessors.addKey(key);
                            }
                        } else
                        {
                            debug("Key "
                                    + key.attachment()
                                    + " invalid; shutting down message processor.");
                            if (pool.containsKey(key.attachment()))
                            {
                                ((MessageProcessor) pool.remove(key
                                        .attachment())).stop();
                            }
                            key.cancel();
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

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Hook method to allow subclasses to specify what kind of MessageProcessor
     * to use.
     * 
     * @param key
     */
    protected void placeKeyInPool(SelectionKey key)
    {
        pool.put(key.attachment(), new MessageProcessor(key,
                requestTranslationSpace, objectRegistry));
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
