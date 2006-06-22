/*
 * Created on May 4, 2006
 */
package ecologylab.services.nio;

import java.nio.channels.SelectionKey;

import ecologylab.generic.Debug;
import ecologylab.generic.ObjectRegistry;
import ecologylab.generic.StartAndStoppable;
import ecologylab.services.ServerConstants;
import ecologylab.xml.NameSpace;

/**
 * Used as a worker thread and client information container.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public class MessageProcessor extends Debug implements Runnable,
        ServerConstants, StartAndStoppable
{
    private Thread         thread;

    private boolean        running = false;

    private ContextManager context = null;

    protected SelectionKey key;

    public MessageProcessor(Object token, SelectionKey key,
            NameSpace translationSpace, ObjectRegistry registry)
    {
        this.context = generateClientContext(token, key, translationSpace,
                registry);

        this.key = key;
    }

    protected ContextManager generateClientContext(Object token,
            SelectionKey key, NameSpace translationSpace,
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
        while (running)
        {
            // read the channel
            context.readChannel();

            // process all the messages
            context.processAllMessagesAndSendResponses();

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

        debug("Message Processor " + key.attachment() + " terminating.");
    }

    public void start()
    {
        running = true;

        if (thread == null)
        {
            thread = new Thread(this, "Message Processor for "
                    + context.getToken());
            thread.start();
        }
    }

    public void stop()
    {
        running = false;
    }

}
