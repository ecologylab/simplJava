/*
 * Created on May 3, 2006
 */
package ecologylab.services.nio;

import java.io.IOException;
import java.net.BindException;
import java.nio.channels.SelectionKey;

import ecologylab.generic.ObjectRegistry;
import ecologylab.services.ServerConstants;
import ecologylab.xml.NameSpace;

public class NIOServer2Threads extends NIOServerBase implements ServerConstants
{
    MessageProcessor2Threads messageProcessor = null;

    public NIOServer2Threads(int portNumber, NameSpace requestTranslationSpace,
            ObjectRegistry objectRegistry) throws IOException, BindException
    {
        super(portNumber, requestTranslationSpace, objectRegistry);

        messageProcessor = this.generateMessageProcessor(
                this.requestTranslationSpace, this.objectRegistry);
    }

    protected MessageProcessor2Threads generateMessageProcessor(
            NameSpace translationSpace, ObjectRegistry registry)
    {
        return new MessageProcessor2Threads(translationSpace, registry);
    }

    protected void invalidateKey(SelectionKey key)
    {
        messageProcessor.removeKey(key);

        key.cancel();
    }

    protected void readKey(SelectionKey key)
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

    public void stop()
    {
        super.stop();

        messageProcessor.stop();
    }
}
