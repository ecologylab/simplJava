/*
 * Created on May 4, 2006
 */
package ecologylab.services.nio;

import java.nio.CharBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.util.HashMap;

import ecologylab.generic.Debug;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.XmlTranslationException;

/**
 * Maintains a pool of MessageProcessors for use by an NIO Services Server.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public class MessageProcessorPool extends Debug
{
    /**
     * HashMap<String><MessageProcessor>; tracks all of the currently-in-use
     * threads.
     */
    protected HashMap           pool = new HashMap();

    protected ServicesServerNIO server;
    
    private CharBuffer temp;

    public MessageProcessorPool(ServicesServerNIO server)
    {
        this.server = server;
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
                pool.put(key.attachment(), new MessageProcessor(this, key
                        .channel(), key.attachment(), server
                        .getRequestTranslationSpace(), server
                        .getObjectRegistry()));
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

    /**
     * Called by a MessageProcessor when it is done processing its message.
     * Messages are done whenever they have produced a ResponseMessage.
     * 
     * This method calls sendResponse() on the server, then checks to see if
     * there are any waiting messages (messages that have not been given a
     * MessageProcessor because there were none available) and starts execution
     * if necessary.
     * 
     */
    public void messageProcessed(ResponseMessage response, Channel channel)
    {
        try
        {
            synchronized(response)
            {
                server.sendResponse(CharBuffer.wrap(response.translateToXML(false).concat("\n")), channel);
            }
            
        } catch (XmlTranslationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @return Returns the server.
     */
    public ServicesServerNIO getServer()
    {
        return server;
    }
}
