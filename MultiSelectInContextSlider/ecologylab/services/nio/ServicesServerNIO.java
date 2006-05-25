/*
 * Created on May 3, 2006
 */
package ecologylab.services.nio;

import java.io.IOException;
import java.net.BindException;
import java.nio.channels.SelectionKey;
import java.util.HashMap;

import ecologylab.generic.ObjectRegistry;
import ecologylab.services.ServerConstants;
import ecologylab.xml.NameSpace;

public class ServicesServerNIO extends NIOServicesServerBase implements
        ServerConstants
{

    protected HashMap pool = new HashMap();

    public ServicesServerNIO(int portNumber, NameSpace requestTranslationSpace,
            ObjectRegistry objectRegistry) throws IOException, BindException
    {
        super(portNumber, requestTranslationSpace, objectRegistry);
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

    protected void invalidateKey(SelectionKey key)
    {
        debug("Key " + key.attachment()
                + " invalid; shutting down message processor.");
        
        if (pool.containsKey(key.attachment()))
        {
            ((NIOServicesServerBase) pool.remove(key.attachment())).stop();
        }
        
        key.cancel();
    }

    protected void readKey(SelectionKey key)
    {
        // disable the key for reading; done here to
        // prevent
        // any issues with hanging select()'s
        key.interestOps(key.interestOps() & (~SelectionKey.OP_READ));

        if (key.attachment() != null)
        {
            if (!pool.containsKey(key.attachment()))
            {
                placeKeyInPool(key);
            }

            try
            {
                ((MessageProcessor) pool.get(key.attachment())).process();
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
}
