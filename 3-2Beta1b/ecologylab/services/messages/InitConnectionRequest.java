/*
 * Created on Feb 28, 2007
 */
package ecologylab.services.messages;

import ecologylab.collections.Scope;

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
     * @see ecologylab.services.messages.RequestMessage#performService(ecologylab.collections.Scope)
     */
    @Override public ResponseMessage performService(
            Scope objectRegistry)
    {
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
