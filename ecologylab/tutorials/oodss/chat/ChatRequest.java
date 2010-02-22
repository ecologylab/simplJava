package ecologylab.tutorials.oodss.chat;

import java.util.HashMap;

import ecologylab.collections.Scope;
import ecologylab.services.distributed.common.SessionObjects;
import ecologylab.services.distributed.server.clientsessionmanager.SessionHandle;
import ecologylab.services.messages.OkResponse;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;

/**
 * Implements a message that will be sent to PublicChatServer to indicate that
 * this client is posting a message to the chat service.
 * 
 * @author bill
 */
public class ChatRequest extends RequestMessage
{
	/**
	 * The message being posted.
	 */
	@xml_attribute
	String									message;

	/**
	 * Constructor used on server. Fields populated automatically by
	 * ecologylab.xml
	 */
	public ChatRequest()
	{
	}

	/**
	 * Constructor used on client.
	 * 
	 * @param newEcho
	 *           a String that will be passed to the server to broadcast
	 * */
	public ChatRequest(String message)
	{
		this.message = message;
	}

	/**
	 * Called automatically by LSDCS on server
	 */
	@Override
	public ResponseMessage performService(Scope cSScope)
	{
		/*
		 * Get all of the handles for the sessions currently connected to the
		 * server
		 */
		HashMap<Object, SessionHandle> sessionHandleMap = (HashMap<Object, SessionHandle>) cSScope
				.get(SessionObjects.SESSIONS_MAP);

		/*
		 * Get this client's SessionHandle
		 */
		SessionHandle mySessionHandle = (SessionHandle) cSScope
				.get(SessionObjects.SESSION_HANDLE);

		/*
		 * Form a update message to send out to all of the
		 * other clients.
		 */
		ChatUpdate messageUpdate = new ChatUpdate(message, mySessionHandle);

		/*
		 * Loop through the other sessions.
		 */
		for (SessionHandle otherClientHandle : sessionHandleMap.values())
		{
			/*
			 * If this isn't me then send them an update.
			 */
			if (!mySessionHandle.equals(otherClientHandle))
			{
				otherClientHandle.sendUpdate(messageUpdate);
			}
		}

		/*
		 * Send back a response confirming that we got the request 
		 */
		return OkResponse.reusableInstance;
	}

	public boolean isDisposable()
	{
		return true;
	}
}
