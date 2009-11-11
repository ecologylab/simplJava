/*
 * Created on Feb 28, 2007
 */
package ecologylab.services.messages;

import ecologylab.xml.xml_inherit;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public @xml_inherit class InitConnectionResponse extends ResponseMessage
{
    @xml_attribute String sessionId;

    /**
     * 
     */
    public InitConnectionResponse()
    {
    }
    
    public InitConnectionResponse(String sessionId)
    {
        this.sessionId = sessionId;
    }

    /**
     * @see ecologylab.services.messages.ResponseMessage#isOK()
     */
    @Override public boolean isOK()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @return the sessionId
     */
    public String getSessionId()
    {
        return sessionId;
    }

}
