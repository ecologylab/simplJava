package ecologylab.services.messages;

import java.net.InetAddress;

import ecologylab.xml.ElementState;

/**
 * Abstract base class for ecologylab.services DCF request and response messages.
 * 
 * @author blake
 */
public class ServiceMessage extends ElementState implements Comparable<ServiceMessage>
{
    @xml_attribute protected long           timeStamp = 0;

    @xml_attribute protected long           uid;

    /**
     * Contains the IP address of the host that sent this message. sender
     * currently must be set by a server that recieves the message and
     * associates it with the IP address from it's packet and/or channel.
     */
    protected InetAddress sender    = null;

    /**
     * Sets timeStamp to equal the current system time in milliseconds.
     * 
     */
    public void stampTime()
    {
        timeStamp = System.currentTimeMillis();
    }

    /**
     * @return Returns the timeStamp in milliseconds.
     */
    public long getTimeStamp()
    {
        return timeStamp;
    }

    public void setUid(long uid)
    {
        this.uid = uid;
    }

    public long getUid()
    {
        return uid;
    }

    /**
     * @return the sender's IP address
     */
    public InetAddress getSender()
    {
        return sender;
    }

    /**
     * This method should be called by a server when it translates this message.
     * 
     * @param sender
     *            the sender's IP address to set
     */
    public void setSender(InetAddress sender)
    {
        this.sender = sender;
    }

    public int compareTo(ServiceMessage otherRequest)
    {
        return (int)(this.uid - otherRequest.getUid());
    }
}
