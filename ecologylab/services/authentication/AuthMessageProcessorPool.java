/*
 * Created on May 4, 2006
 */
package ecologylab.services.authentication;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import ecologylab.services.nio.MessageProcessor;
import ecologylab.services.nio.MessageProcessorPool;
import ecologylab.services.nio.ServicesServerNIO;

public class AuthMessageProcessorPool extends MessageProcessorPool
{
    public AuthMessageProcessorPool(ServicesServerNIO server)
    {
        super(server);
    }

    /**
     * If the key does not have a matching MessageProcesor, creates a new one.
     * Passes the key on to the MessageProcessor and has it process on its own
     * thread.
     * 
     * @param key
     */
    public void addKey(SelectionKey key)
    {
        if (key.attachment() != null)
        {
            if (!pool.containsKey(key.attachment()))
            {
                pool.put(key.attachment(), 
                   new AuthMessageProcessor(this, 
                        key.channel(), 
                        key.attachment(), 
                        server.getRequestTranslationSpace(), 
                        server.getObjectRegistry(), 
                        ((SocketChannel) key.channel()).socket().getInetAddress()));
            }

            try
            {
                ((MessageProcessor) pool.get(key.attachment())).process(key);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        } else
        {
            debug("Null token!");
        }
    }
}
