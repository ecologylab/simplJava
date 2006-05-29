/*
 * Created on May 3, 2006
 */
package ecologylab.services.nio;

import java.io.IOException;
import java.net.BindException;
import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Iterator;

import ecologylab.generic.ObjectRegistry;
import ecologylab.services.ServerConstants;
import ecologylab.xml.NameSpace;

public class NIOServerMultiThreaded extends NIOServerBase implements
        ServerConstants
{

    protected HashMap pool = new HashMap();

    public NIOServerMultiThreaded(int portNumber, NameSpace requestTranslationSpace,
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
    protected MessageProcessor placeKeyInPool(SelectionKey key)
    {
        MessageProcessor temp = new MessageProcessor(key.attachment(), key,
                requestTranslationSpace, objectRegistry);
        
        pool.put(key.attachment(), temp);
        
        return temp;
    }

    protected void invalidateKey(SelectionKey key)
    {
        debug("Key " + key.attachment()
                + " invalid; shutting down message processor.");
        
        if (pool.containsKey(key.attachment()))
        {
            ((NIOServerBase) pool.remove(key.attachment())).stop();
        }
        
        key.cancel();
    }

    protected void readKey(SelectionKey key)
    {
        if (key.attachment() != null)
        {
            if (!pool.containsKey(key.attachment()))
            {
                placeKeyInPool(key).start();
            }

            try
            {
                MessageProcessor temp = (MessageProcessor) pool.get(key.attachment());
                
                synchronized(temp)
                {
                    temp.notify();
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
    
    public void stop()
    {
        super.stop();
        
        Iterator temp = pool.values().iterator();
        
        while (temp.hasNext())
        {
            ((MessageProcessor)temp.next()).stop();
        }
        
        pool.clear();
    }
}
