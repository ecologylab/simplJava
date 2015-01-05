/*
 * Created on Feb 28, 2007
 */
package ecologylab.oodss.messages;

import ecologylab.collections.Scope;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.formatenums.StringFormat;

/**
 * Request to start a new connection to a server. If the message has no sessionId value, then it is
 * attempting to open a completely new connection. If it has a value for sessionId, it is the
 * sessionId provided by a previous connection.
 * 
 * Sending a message with a past sessionId is no guarantee of restoring the old connection; the
 * server may have disposed of it.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public class InitConnectionRequest extends RequestMessage
{
	@simpl_scalar
	String	sessionId;

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
	 * Returns null. Logic for handling initializing messages must be handled by a ClientSessionScope
	 * object.
	 * 
	 * @see ecologylab.oodss.messages.RequestMessage#performService(ecologylab.collections.Scope)
	 */
	@Override
	public ResponseMessage performService(Scope objectRegistry)
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

	public static void main(String[] args)
	{
		try
		{
			SimplTypesScope.serialize(SimplTypesScope.get("init_connection_request", InitConnectionRequest.class,
					RequestMessage.class), System.out, StringFormat.XML);
		}
		catch (SIMPLTranslationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
