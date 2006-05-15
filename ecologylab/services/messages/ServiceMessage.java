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
	
}

