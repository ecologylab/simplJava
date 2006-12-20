/*
 * Created on May 3, 2006
 */
package ecologylab.services.nio.two_threaded;

import java.io.IOException;
import java.net.BindException;
import java.nio.channels.SelectionKey;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.services.BadClientException;
import ecologylab.services.ServerConstants;
import ecologylab.services.nio.NIOServerBase;
import ecologylab.xml.TranslationSpace;

public class NIOServer2Threads extends NIOServerBase implements ServerConstants
{
    MessageProcessor2Threads messageProcessor = null;

    public NIOServer2Threads(int portNumber, TranslationSpace requestTranslationSpace,
            ObjectRegistry objectRegistry) throws IOException, BindException
    {
        super(portNumber, requestTranslationSpace, objectRegistry);

        messageProcessor = this.generateMessageProcessor(
                this.requestTranslationSpace, this.objectRegistry);
    }

    protected MessageProcessor2Threads generateMessageProcessor(
            TranslationSpace translationSpace, ObjectRegistry registry)
    {
        return new MessageProcessor2Threads(translationSpace, registry, null);
    }

    /**
     * Shut down the connection associated with this SelectionKey.
     * Removes the key from our message processor, then calls super.invalidateKey(SelectionKey)
     * to shut it down at the NIO level.
     * 
     * @param key	The SelectionKey that needs to be shut down.
     */
    public void invalidate(SelectionKey key)
    {
        messageProcessor.removeKey(key);
        super.invalidate(key);
    }

    protected void read(SelectionKey key) throws BadClientException
    {
        if (key.attachment() != null)
        {
            messageProcessor.readKey(key);

            // boot the processor thread
            try
            {
                synchronized (messageProcessor)
                {
                    messageProcessor.notify();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            debug("Null token!");
        }
    }

    public void start()
    {
        super.start();

        messageProcessor.start();
    }

    /**
     * close() is called after the server has stopped.
     * 
     * @see ecologylab.services.nio.NIOServerBase#close()
     */
    protected void close()
    {
        super.close();

        messageProcessor = null;
    }

    public void stop()
    {
        super.stop();

        messageProcessor.stop();
    }
}
