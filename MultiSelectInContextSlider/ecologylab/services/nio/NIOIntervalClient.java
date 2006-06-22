/**
 * 
 */
package ecologylab.services.nio;

import java.io.IOException;

import ecologylab.generic.ObjectRegistry;
import ecologylab.services.messages.RequestMessage;
import ecologylab.xml.NameSpace;

/**
 * Works like NIOClient, but runs on an interval and allows subclasses to send to
 * the server on that interval.
 * 
 * Sends the message specified in the constructor, or modified through the
 * setMessage() method. Note that it is *NOT* guaranteed that the message will
 * be sent with any timeliness. It is normally assumed that the messageToSend
 * will be backed by a subclass of RequestMessage that references an object that
 * changes over time.
 * 
 * @author Zach Toups
 * 
 */
public class NIOIntervalClient extends NIOClient
{
    private int            maxInterval   = 0;

    private RequestMessage messageToSend = null;

    /**
     * @param server
     * @param port
     * @param messageSpace
     * @param objectRegistry
     */
    public NIOIntervalClient(String server, int port, NameSpace messageSpace,
            ObjectRegistry objectRegistry, int interval,
            RequestMessage messageToSend)
    {
        super(server, port, messageSpace, objectRegistry);

        this.messageToSend = messageToSend;
        this.maxInterval = interval;
    }

    public NIOIntervalClient(String server, int port, NameSpace messageSpace,
            ObjectRegistry objectRegistry)
    {
        this(server, port, messageSpace, objectRegistry, 0, null);
    }

    /**
     * Performs a nonBlockingSend, then sets selectInterval to be interval -
     * total time to run this method.
     */
    protected void sendData()
    {
        long time = System.currentTimeMillis();

        this.sendMessageOnInterval();

        if (maxInterval > 0)
        {
            // make select interval equal to interval - the time to run this
            // method,
            // or 1, whichever is greater.
            time = System.currentTimeMillis() - time;
            
            if ((selectInterval - time) > 0)
            {
                selectInterval -= time;
            }
            else
            {
                selectInterval = 1;
            }
        } 
    }

    protected void sendMessageOnInterval()
    {
        if (messageToSend != null)
        {
            try
            {
                this.nonBlockingSendMessage(messageToSend);
            }
            catch (IOException e)
            {
                debug(e.getMessage());
            }
        }
    }

    protected void startSending(){
        isSending = true;
    }
    
    protected void startSending(RequestMessage request)
    {
        this.messageToSend = request;
        
        isSending = true;
    }
    
    protected void stopSending()
    {
        isSending = false;
    }
    
    protected void resetSelectInterval()
    {
        selectInterval = maxInterval;
    }

    public void setMessageToSend(RequestMessage messageToSend)
    {
        this.messageToSend = messageToSend;
    }
}
