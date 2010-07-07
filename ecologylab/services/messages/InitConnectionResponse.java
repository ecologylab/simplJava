/*
 * Created on Feb 28, 2007
 */
package ecologylab.services.messages;

import ecologylab.xml.simpl_inherit;

/**
 * Response to a request to connect to a server. On a successful connection, sessionId will contain
 * the server-assigned session identifier. If the connection failed, sessionId will be null.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public @simpl_inherit
class InitConnectionResponse extends ResponseMessage
{
	/**
	 * The session identifier used for all communications between this client and the server. If the
	 * value is null, it means the connection failed.
	 */
	@simpl_scalar
	String	sessionId;

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
	@Override
	public boolean isOK()
	{
		return sessionId != null;
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId()
	{
		return sessionId;
	}

}
