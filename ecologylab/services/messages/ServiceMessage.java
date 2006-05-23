package ecologylab.services.messages;

import ecologylab.xml.ElementState;

/**
 * Generic message used for tcp/ip socket messages.
 * 
 * @author blake
 */
public class ServiceMessage extends ElementState 
{
    public long timeStamp = 0;
    
    public long uid;
    
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
}

