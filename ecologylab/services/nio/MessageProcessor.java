/*
 * Created on May 4, 2006
 */
package ecologylab.services.nio;

import java.nio.channels.SelectionKey;

import ecologylab.generic.Debug;
import ecologylab.generic.ObjectRegistry;
import ecologylab.generic.StartAndStoppable;
import ecologylab.services.BadClientException;
import ecologylab.services.ServerConstants;
import ecologylab.xml.TranslationSpace;

/**
 * Used as a worker thread and client information container.
 * 
 * One thread per connection, N threads altogether.
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
    
    protected NIOServerBase server;

    public MessageProcessor(Object token, SelectionKey key,
            TranslationSpace translationSpace, ObjectRegistry registry, NIOServerBase server)
    {
        this.context = generateClientContext(token, key, translationSpace,
                registry);

        this.key = key;
        
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
        while (running)
        {
            // read the channel
            try
			{
				context.readChannel();

	            // process all the messages
	            context.processAllMessagesAndSendResponses();
	            
			} catch (BadClientException e1)
			{
				// close down this evil connection
				error(e1.getMessage());

                server.invalidateKey(key);
                
				stop();
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
