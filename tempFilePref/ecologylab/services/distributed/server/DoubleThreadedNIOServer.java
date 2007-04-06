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
            InetAddress[] inetAddress,
            TranslationSpace requestTranslationSpace,
            ObjectRegistry objectRegistry, int idleConnectionTimeout,
            int maxPacketSize) throws IOException, BindException
    {
        return new DoubleThreadedNIOServer(portNumber, inetAddress,
                requestTranslationSpace, objectRegistry, idleConnectionTimeout,
                maxPacketSize);
    }

    public static DoubleThreadedNIOServer getInstance(int portNumber,
            InetAddress inetAddress, TranslationSpace requestTranslationSpace,
            ObjectRegistry objectRegistry, int idleConnectionTimeout,
            int maxPacketSize) throws IOException, BindException
    {
        InetAddress[] address =
        { inetAddress };
        return getInstance(portNumber, address, requestTranslationSpace,
                objectRegistry, idleConnectionTimeout, maxPacketSize);
    }

    Thread                          t        = null;

    boolean                         running  = false;

    HashMap<Object, ContextManager> contexts = new HashMap<Object, ContextManager>();

    private static CharsetDecoder   decoder  = Charset.forName(
                                                     CHARACTER_ENCODING)
                                                     .newDecoder();

    protected int                   maxPacketSize;

    /**
     * 
     */
    protected DoubleThreadedNIOServer(int portNumber,
            InetAddress[] inetAddresses,
            TranslationSpace requestTranslationSpace,
            ObjectRegistry objectRegistry, int idleConnectionTimeout,
            int maxPacketSize) throws IOException, BindException
    {
        super(portNumber, inetAddresses, requestTranslationSpace, objectRegistry,
                idleConnectionTimeout);

        this.maxPacketSize = maxPacketSize;
    }

    /**
     * 
     */
    protected DoubleThreadedNIOServer(int portNumber,
            InetAddress inetAddress,
            TranslationSpace requestTranslationSpace,
            ObjectRegistry objectRegistry, int idleConnectionTimeout,
            int maxPacketSize) throws IOException, BindException
    {
        super(portNumber, inetAddress, requestTranslationSpace, objectRegistry,
                idleConnectionTimeout);

        this.maxPacketSize = maxPacketSize;
    }
    
    /**
     * @throws BadClientException
     *             See
     *             ecologylab.services.nio.servers.NIOServerFrontend#process(ecologylab.services.nio.NIOServerBackend,
     *             java.nio.channels.SocketChannel, byte[], int)
     */
    public void processRead(Object sessionId, NIOServerBackend base,
            SocketChannel sc, byte[] bs, int bytesRead)
            throws BadClientException
    {
        if (bytesRead > 0)
        {
            synchronized (contexts)
            {
                ContextManager cm = contexts.get(sessionId);

                if (cm == null)
                {
                    debug("server creating context manager for " + sessionId);

                    cm = generateContextManager(sessionId, sc,
                            translationSpace, registry);
                    contexts.put(sessionId, cm);
                }

                try
                {
                    cm
                            .enqueueStringMessage(decoder.decode(ByteBuffer
                                    .wrap(bs)));
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
        return new ContextManager(token, maxPacketSize, this.getBackend(),
                this, sc, translationSpace, registry);
    }

    public void run()
    {
        Iterator<ContextManager> contextIter;

        while (running)
        {
            synchronized (contexts)
            {
                contextIter = contexts.values().iterator();

                // process all of the messages in the queues
                while (contextIter.hasNext())
                {
                    ContextManager cm = contextIter.next();

                    try
                    {
                        cm.processAllMessagesAndSendResponses();
                    }
                    catch (BadClientException e)
                    {
                        // Handle BadClientException! -- remove it
                        error(e.getMessage());

                        // invalidate the manager's key
                        this.getBackend().setPendingInvalidate(cm.getSocket(),
                                true);

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
     * @see ecologylab.services.nio.servers.NIOServerFrontend#accept(java.lang.Object,
     *      ecologylab.services.nio.NIOServerBackend,
     *      java.nio.channels.SocketChannel)
     */
    public void accept(Object token, NIOServerBackend base, SocketChannel sc)
    {
        // TODO Auto-generated method stub

    }

    /**
     * @see ecologylab.services.nio.servers.NIOServerFrontend#invalidate(java.lang.Object,
     *      ecologylab.services.nio.NIOServerBackend,
     *      java.nio.channels.SocketChannel)
     */
    public ContextManager invalidate(Object sessionId, NIOServerBackend base,
            SocketChannel sc, boolean permanent)
    {
        ContextManager cm;

        if (permanent)
        {
            synchronized (contexts)
            {
                cm = contexts.remove(sessionId);
            }
        }
        else
        {
            synchronized (contexts)
            {
                cm = contexts.get(sessionId);
            }
        }

        if (cm != null)
        {
            while (cm.isMessageWaiting())
            {
                try
                {
                    cm.processAllMessagesAndSendResponses();
                }
                catch (BadClientException e)
                {
                    e.printStackTrace();
                }
            }

            cm.shutdown();
        }

        return cm;
    }

    /**
     * Attempts to switch the ContextManager for a SocketChannel. oldId
     * indicates the session id that was used for the connection previously (in
     * order to find the correct ContextManager) and newContextManager is the
     * recently-created (and now, no longer necessary) ContextManager for the
     * connection.
     * 
     * @param oldId
     * @param newContextManager
     * @return true if the restore was successful, false if it was not.
     */
    public boolean restoreContextManagerFromSessionId(Object oldSessionId,
            ContextManager newContextManager)
    {
        debug("attempting to restore old session...");

        ContextManager oldContextManager;

        synchronized (contexts)
        {
            oldContextManager = this.contexts.get(oldSessionId);
        }
        if (oldContextManager == null)
        { // cannot restore old context
            debug("restore failed.");
            return false;
        }
        else
        {
            oldContextManager.setSocket(newContextManager.getSocket());

            debug("old session restored!");
            return true;
        }
    }
}
