/**
 * 
 */
package ecologylab.services.nio.action_processor;

import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.util.HashMap;
import java.util.Iterator;

import ecologylab.generic.Debug;
import ecologylab.generic.ObjectRegistry;
import ecologylab.generic.StartAndStoppable;
import ecologylab.services.BadClientException;
import ecologylab.services.nio.ContextManager;
import ecologylab.services.nio.NIOServerBase;
import ecologylab.xml.TranslationSpace;

/**
 * Operates on its own thread to process messages coming in from a client.
 * 
 * @author Zach Toups
 * 
 */
public class DoubleThreadedActionProcessor extends Debug implements ServerActionProcessor,
        Runnable, StartAndStoppable
{
    Thread t = null;

    boolean running = false;
    
    NIOServerBase server = null;
    
    private TranslationSpace                translationSpace;

    private ObjectRegistry                  registry;
    
    HashMap<SocketChannel, ContextManager> contexts = new HashMap<SocketChannel, ContextManager>();

    /**
     * 
     */
    public DoubleThreadedActionProcessor(NIOServerBase server, TranslationSpace translationSpace, ObjectRegistry registry)
    {   
        this.server = server;
        this.translationSpace = translationSpace;
        this.registry = registry;
    }

    /**
     * @throws BadClientException 
     * @see ecologylab.services.nio.action_processor.ServerActionProcessor#process(ecologylab.services.nio.NIOServerBase,
     *      java.nio.channels.SocketChannel, byte[], int)
     */
    public void process(Object token, NIOServerBase base, SocketChannel sc, byte[] bs,
            int bytesRead) throws BadClientException
    {
        synchronized (contexts)
        {
            ContextManager cm = contexts.get(sc);

            if (cm == null)
            {
                cm = generateClientContext(token, sc, translationSpace,
                        registry);
                contexts.put(sc, cm);
            }

            try
            {
                cm.convertBytesToMessagesAndEnqueue(bs);
            }
            catch (CharacterCodingException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    protected ContextManager generateClientContext(Object token,
            SocketChannel sc, TranslationSpace translationSpace,
            ObjectRegistry registry)
    {
        return new ContextManager(token, server, sc, translationSpace, registry);
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
                        server.invalidate(sc);

                        // remove the manager from the collection
                        contextIter.remove();
                    }
                }
            }

            // sleep until notified of new messages
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

    public void start()
    {
        running = true;
        
        if (t == null)
        {
            t = new Thread(this);
        }
        
        t.start();
    }

    public void stop()
    {
        running = false;
    }

}
