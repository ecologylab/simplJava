/*
 * Created on Feb 28, 2007
 */
package ecologylab.services.messages;

import ecologylab.collections.Scope;
import ecologylab.xml.TranslationScope;
import ecologylab.xml.XMLTranslationException;

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
    
    public static void main(String[] args){
    	try {
			TranslationScope.get("init_connection_request", InitConnectionRequest.class, RequestMessage.class).translateToXML(System.out);
		} catch (XMLTranslationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
