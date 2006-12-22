/*
 * Created on May 3, 2006
 */
package ecologylab.services.nio.n_threaded;

import java.io.IOException;
import java.net.BindException;
import java.nio.channels.SelectionKey;
import java.util.HashMap;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.services.ServerConstants;
import ecologylab.services.nio.MessageProcessor;
import ecologylab.services.nio.NIOServerBackend;
import ecologylab.xml.TranslationSpace;

public class NIOServerNThreads extends NIOServerBackend implements
        ServerConstants
{

    protected HashMap<Object, MessageProcessor> pool = new HashMap<Object, MessageProcessor>();

    public NIOServerNThreads(int portNumber, TranslationSpace requestTranslationSpace,
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
                requestTranslationSpace, objectRegistry, this);
        
        pool.put(key.attachment(), temp);
        
        return temp;
    }

    /**
     * Shut down the connection associated with this SelectionKey.
     * Removes the key from our pool, then calls super.invalidateKey(SelectionKey)
     * to shut it down at the NIO level.
     * 
     * @param key	The SelectionKey that needs to be shut down.
     */
    public void invalidate(SelectionKey key)
    {
        debug("Key " + key.attachment()
                + " invalid; shutting down message processor.");
        
        if (pool.containsKey(key.attachment()))
        {
            pool.remove(key.attachment()).stop();
        }
        
        super.invalidate(key);
    }

    protected void read(SelectionKey key)
    {
        if (key.attachment() != null)
        {
            if (!pool.containsKey(key.attachment()))
            {
                placeKeyInPool(key).start();
            }

            try
            {
                MessageProcessor temp = pool.get(key.attachment());
                
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
        
        for (MessageProcessor mProc : pool.values())
        {
            mProc.stop();
        }
        
        pool.clear();
    }
}
