/*
 * Created on May 12, 2006
 */
package ecologylab.services.authentication.messages;

import ecologylab.generic.BooleanSlot;
import ecologylab.generic.ObjectRegistry;
import ecologylab.services.authentication.registryobjects.AuthClientRegistryObjects;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.xml_inherit;

@xml_inherit public class LoginStatusResponse extends ResponseMessage implements
        AuthMessages, AuthClientRegistryObjects
{
    @xml_attribute private String responseMessage = new String();

    public LoginStatusResponse(String responseMessage)
    {
        this.responseMessage = responseMessage;
    }

    public LoginStatusResponse()
    {
        super();
    }

    /**
     * @return Returns the responseMessage.
     */
    public String getResponseMessage()
    {
        return responseMessage;
    }

    public boolean isOK()
    {
        return LOGIN_SUCCESSFUL.equals(responseMessage);
    }

    public void processResponse(ObjectRegistry objectRegistry)
    {
        System.out.println("response about login: "+isOK());
        
        ((BooleanSlot) objectRegistry.lookupObject(LOGIN_STATUS)).value = isOK();
        objectRegistry.modifyObject(LOGIN_STATUS_STRING, responseMessage);
    }

    /**
     * @param responseMessage
     *            The responseMessage to set.
     */
    public void setResponseMessage(String responseMessage)
    {
        this.responseMessage = responseMessage;
    }

}
