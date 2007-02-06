/**
 * 
 */
package ecologylab.services.nio.servers;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.HashMap;
import java.util.Iterator;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.services.ServerConstants;
import ecologylab.services.exceptions.BadClientException;
import ecologylab.services.nio.ContextManager;
import ecologylab.services.nio.NIOServerBackend;
import ecologylab.xml.TranslationSpace;

/**
 * Operates on its own thread to process messages coming in from a client.
 * 
 * @author Zach Toups
 * 
 */
public class DoubleThreadedNIOServer extends NIOServerBase implements
        ServerConstants
{
    public static DoubleThreadedNIOServer getInstance(int portNumber,
            InetAddress inetAddress, TranslationSpace requestTranslationSpace,
            ObjectRegistry objectRegistry, int idleConnectionTimeout) throws IOException, BindException
    {
        return new DoubleThreadedNIOServer(portNumber, inetAddress,
                requestTranslationSpace, objectRegistry, idleConnectionTimeout);
    }

    Thread                                 t        = null;

    boolean                                running  = false;

    HashMap<SocketChannel, ContextManager> contexts = new HashMap<SocketChannel, ContextManager>();

    private static CharsetDecoder          decoder  = Charset.forName(
                                                            CHARACTER_ENCODING)
                                                            .newDecoder();

    /**
     * 
     */
    protected DoubleThreadedNIOServer(int portNumber, InetAddress inetAddress,
            TranslationSpace requestTranslationSpace,
            ObjectRegistry objectRegistry, int idleConnectionTimeout) throws IOException, BindException
    {
        super(portNumber, inetAddress, requestTranslationSpace, objectRegistry, idleConnectionTimeout);
    }

    /**
     * @throws BadClientException
     * See ecologylab.services.nio.servers.NIOServerFrontend#process(ecologylab.services.nio.NIOServerBackend,
     *      java.nio.channels.SocketChannel, byte[], int)
     */
    public void processRead(Object token, NIOServerBackend base, SocketChannel sc,
            byte[] bs, int bytesRead) throws BadClientException
    {
        if (bytesRead > 0)
        {
            synchronized (contexts)
            {
                ContextManager cm = contexts.get(sc);

                if (cm == null)
                {
                    cm = generateContextManager(token, sc, translationSpace,
                            registry);
                    contexts.put(sc, cm);
                }

                try
                {
                    cm.enqueueStringMessage(decoder.decode(ByteBuffer.wrap(bs)));
                }
                catch (CharacterCodingException e)
                {
                    e.printStackTrace();
                }
            }

            synchronized (this)
            {
                this.notify();
            }
        }
    }

    /**
     * Hook method to allow changing the ContextManager to enable specific extra
     * functionality.
     * 
     * @param token
     * @param sc
     * @param translationSpace
     * @param registry
     * @return
     */
    protected ContextManager generateContextManager(Object token,
            SocketChannel sc, TranslationSpace translationSpace,
            ObjectRegistry registry)
    {
        return new ContextManager(token, this.getBackend(), sc,
                translationSpace, registry);
    }

    public void run()
    {
        Iterator<SocketChannel> contextIter;

        while (running)
        {
            synchronized (contexts)
            {
                contextIter = contexts.keySet().iterator();

                // process all of the messages in the queues
                while (contextIter.hasNext())
                {
                    SocketChannel sc = contextIter.next();

                    try
                    {
                        contexts.get(sc).processAllMessagesAndSendResponses();
                    }
                    catch (BadClientException e)
                    {
                        // Handle BadClientException! -- remove it
                        error(e.getMessage());

                        // invalidate the manager's key
                        this.getBackend().invalidate(sc);

                        // remove the manager from the collection
                        contextIter.remove();
                    }
                }
            }

            // sleep until notified of new messages
            synchronized (this)
            {
                try
                {
                    wait();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                    Thread.interrupted();
                }
            }
        }
    }

    /**
     * @see ecologylab.generic.StartAndStoppable#start()
     */
    public void start()
    {
        running = true;

        if (t == null)
        {
            t = new Thread(this);
        }

        t.start();

        super.start();
    }

    /**
     * @see ecologylab.generic.StartAndStoppable#stop()
     */
    public void stop()
    {
        running = false;

        super.stop();
    }

    /**
     * @see ecologylab.services.Shutdownable#shutdown()
     */
    public void shutdown()
    {
        // TODO Auto-generated method stub

    }

    /**
     * @see ecologylab.services.nio.servers.NIOServerFrontend#accept(java.lang.Object, ecologylab.services.nio.NIOServerBackend, java.nio.channels.SocketChannel)
     */
    public void accept(Object token, NIOServerBackend base, SocketChannel sc)
    {
        // TODO Auto-generated method stub
        
    }

    /**
     * @see ecologylab.services.nio.servers.NIOServerFrontend#invalidate(java.lang.Object, ecologylab.services.nio.NIOServerBackend, java.nio.channels.SocketChannel)
     */
    public void invalidate(Object token, NIOServerBackend base, SocketChannel sc)
    {
        ContextManager cm = contexts.remove(sc);

        if (cm != null)
        {
            cm.shutdown();
        }
    }

}
