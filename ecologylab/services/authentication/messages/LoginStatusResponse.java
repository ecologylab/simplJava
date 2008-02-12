/*
 * Created on May 12, 2006
 */
package ecologylab.services.authentication.messages;

import ecologylab.appframework.Scope;
import ecologylab.generic.BooleanSlot;
import ecologylab.services.authentication.registryobjects.AuthClientRegistryObjects;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.xml_inherit;

/**
 * Indicates the response from the server regarding an attempt to log in.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
@xml_inherit public class LoginStatusResponse extends ResponseMessage implements AuthMessages,
		AuthClientRegistryObjects
{
	/**
	 * The response from the server regarding the attempt to log in; indicates whether or not login was successful and
	 * why not, if it was not.
	 */
	@xml_attribute private String	responseMessage	= new String();

	/**
	 * Constructs a new LoginStatusResponse with the given responseMessage.
	 * 
	 * @param responseMessage
	 *           the response from the server regarding the attempt to log in.
	 */
	public LoginStatusResponse(String responseMessage)
	{
		this.responseMessage = responseMessage;
	}

	/** No-argument constructor for serialization. */
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

	/**
	 * Indicates whether or not the attempt to log in was successful.
	 * 
	 * @see ecologylab.services.messages.ResponseMessage#isOK()
	 * 
	 * @return true if login was successful, false otherwise.
	 */
	@Override public boolean isOK()
	{
		return LOGIN_SUCCESSFUL.equals(responseMessage);
	}

	/**
	 * Sets the LOGIN_STATUS BooleanSlot in the ObjectRegistry for the client, indicating whether or not login was
	 * successful.
	 * 
	 * @see ecologylab.services.messages.ResponseMessage#processResponse(ecologylab.appframework.Scope)
	 */
	@Override public void processResponse(Scope objectRegistry)
	{
		System.out.println("response about login: " + isOK());

		((BooleanSlot) objectRegistry.lookup(LOGIN_STATUS)).value = isOK();
		objectRegistry.bind(LOGIN_STATUS_STRING, responseMessage);
	}

	/**
	 * @param responseMessage
	 *           The responseMessage to set.
	 */
	public void setResponseMessage(String responseMessage)
	{
		this.responseMessage = responseMessage;
	}

}
