/*
 * Created on Feb 28, 2007
 */
package ecologylab.services.messages;

import ecologylab.appframework.ObjectRegistry;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public class InitConnectionRequest extends RequestMessage
{
    @xml_attribute String sessionId;

    /**
     * 
     */
    public InitConnectionRequest()
    {
    }
    
    public InitConnectionRequest(String sessionId)
    {
        this.sessionId = sessionId;
    }

    /**
     * @see ecologylab.services.messages.RequestMessage#performService(ecologylab.appframework.ObjectRegistry)
     */
    @Override public ResponseMessage performService(
            ObjectRegistry objectRegistry)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @return the sessionId
     */
    public String getSessionId()
    {
        return sessionId;
    }

}
