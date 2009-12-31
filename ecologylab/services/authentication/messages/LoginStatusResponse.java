/*
 * Created on May 12, 2006
 */
package ecologylab.services.authentication.messages;

import ecologylab.collections.Scope;
import ecologylab.generic.BooleanSlot;
import ecologylab.services.authentication.registryobjects.AuthClientRegistryObjects;
import ecologylab.services.messages.ExplanationResponse;
import ecologylab.xml.xml_inherit;

/**
 * Indicates the response from the server regarding an attempt to log in.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
@xml_inherit public class LoginStatusResponse<S extends Scope> extends
		ExplanationResponse<S> implements AuthMessages, AuthClientRegistryObjects
{
	/**
	 * Constructs a new LoginStatusResponse with the given responseMessage.
	 * 
	 * @param responseMessage
	 *           the response from the server regarding the attempt to log in.
	 */
	public LoginStatusResponse(String responseMessage)
	{
		super(responseMessage);
	}

	/** No-argument constructor for serialization. */
	public LoginStatusResponse()
	{
		super();
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
		return LOGIN_SUCCESSFUL.equals(explanation);
	}

	/**
	 * Sets the LOGIN_STATUS BooleanSlot in the ObjectRegistry for the client,
	 * indicating whether or not login was successful.
	 * 
	 * @see ecologylab.services.messages.ResponseMessage#processResponse(ecologylab.collections.Scope)
	 */
	@Override public void processResponse(S objectRegistry)
	{
		System.out.println("response about login: " + isOK());

		((BooleanSlot) objectRegistry.get(LOGIN_STATUS)).value = isOK();
		objectRegistry.put(LOGIN_STATUS_STRING, explanation);
	}
}
