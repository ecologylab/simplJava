/*
 * Created on Feb 28, 2007
 */
package ecologylab.services.messages;

import ecologylab.collections.Scope;
import ecologylab.xml.TranslationScope;
import ecologylab.xml.XMLTranslationException;

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
	 * @see ecologylab.services.messages.RequestMessage#performService(ecologylab.collections.Scope)
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
			TranslationScope.get("init_connection_request", InitConnectionRequest.class,
					RequestMessage.class).serialize(System.out);
		}
		catch (XMLTranslationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
