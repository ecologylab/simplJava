/*
 * Created on May 4, 2006
 */
package ecologylab.services.nio.two_threaded;

import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Iterator;

import ecologylab.generic.Debug;
import ecologylab.generic.ObjectRegistry;
import ecologylab.generic.StartAndStoppable;
import ecologylab.services.BadClientException;
import ecologylab.services.ServerConstants;
import ecologylab.services.nio.ContextManager;
import ecologylab.services.nio.NIOServerBase;
import ecologylab.xml.TranslationSpace;

/**
 * Used as a worker thread and client information container.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public class MessageProcessor2Threads extends Debug implements Runnable,
        ServerConstants, StartAndStoppable
{
    private Thread                          thread;

    private boolean                         running  = false;

    private HashMap<Object, ContextManager> contexts = new HashMap<Object, ContextManager>();

    protected SelectionKey                  key;

    private TranslationSpace                translationSpace;

    private ObjectRegistry                  registry;

    protected NIOServerBase                   server;

    public MessageProcessor2Threads(TranslationSpace translationSpace,
            ObjectRegistry registry, NIOServerBase server)
    {
        this.translationSpace = translationSpace;
        this.registry = registry;
        this.server = server;
    }

    protected ContextManager generateClientContext(Object token,
            SelectionKey key, TranslationSpace translationSpace,
            ObjectRegistry registry)
    {
        return new ContextManager(token, key, translationSpace, registry);
    }

    /**
     * Processes the next String in the messageQueue, sleeps when there are none
     * left.
     */
    public synchronized void run()
    {
        Iterator<ContextManager> contextIterator;

        while (running)
        {
            synchronized (contexts)
            {
                contextIterator = contexts.values().iterator();
                // process all of the messages in the queues
                while (contextIterator.hasNext())
                {
                    ContextManager cMan = contextIterator.next();

                    try
                    {
                        cMan.processAllMessagesAndSendResponses();
                    }
                    catch (BadClientException e)
                    {
                        // Handle BadClientException! -- remove it
                        error(e.getMessage());

                        // invalidate the manager's key
                        server.invalidate(cMan.getKey());

                        // remove the manager from the collection
                        contextIterator.remove();
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

        contexts.clear();

        debug("Message Processor " + key.attachment() + " terminating.");
    }

    public void readKey(SelectionKey key) throws BadClientException
    {
        ContextManager temp = (ContextManager) contexts.get(key.attachment());
        {
            if (temp == null)
            {
                synchronized (contexts)
                {
                    contexts.put(key.attachment(), this.generateClientContext(
                            key.attachment(), key, translationSpace, registry));
                }

                temp = (ContextManager) contexts.get(key.attachment());
            }

            temp.readChannel();
        }
    }

    protected void removeKey(SelectionKey key)
    {
        debug("Key " + key.attachment()
                + " invalid; shutting down message processor.");

        synchronized (contexts)
        {
            contexts.remove(key.attachment());
        }
    }

    public void start()
    {
        running = true;

        if (thread == null)
        {
            thread = new Thread(this, "Message Processor");
            thread.start();
        }
    }

    public void stop()
    {
        running = false;
    }

}
