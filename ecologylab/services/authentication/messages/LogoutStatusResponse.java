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
 * Indicates the response from the server regarding an attempt to log out.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
@xml_inherit public class LogoutStatusResponse extends ResponseMessage implements AuthMessages,
		AuthClientRegistryObjects
{
	/**
	 * The response from the server regarding the attempt to log out; indicates whether or not logout was successful and
	 * why not, if it was not.
	 */
	@xml_attribute private String	responseMessage	= new String();

	/**
	 * Constructs a new LogoutStatusResponse with the given responseMessage.
	 * 
	 * @param responseMessage
	 *           the response from the server regarding the attempt to log out.
	 */
	public LogoutStatusResponse(String responseMessage)
	{
		this.responseMessage = responseMessage;
	}

	/** No-argument constructor for serialization. */
	public LogoutStatusResponse()
	{
		super();
	}

	/**
	 * Indicates whether or not the attempt to log out was successful.
	 * 
	 * @see ecologylab.services.messages.ResponseMessage#isOK()
	 * 
	 * @return true if logout was successful, false otherwise.
	 */
	@Override public boolean isOK()
	{
		return LOGOUT_SUCCESSFUL.equals(responseMessage);
	}

	/**
	 * Sets the LOGIN_STATUS BooleanSlot in the ObjectRegistry for the client to false.
	 * 
	 * @see ecologylab.services.messages.ResponseMessage#processResponse(ecologylab.appframework.Scope)
	 */
	@Override public void processResponse(Scope objectRegistry)
	{
		((BooleanSlot) objectRegistry.lookup(LOGIN_STATUS)).value = false;

		objectRegistry.bind(LOGIN_STATUS_STRING, responseMessage);
	}

	/**
	 * @return Returns the responseMessage.
	 */
	public String getResponseMessage()
	{
		return responseMessage;
	}

}
