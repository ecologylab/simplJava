package ecologylab.oodss.distributed.server.clientsessionmanager;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import ecologylab.collections.Scope;
import ecologylab.oodss.messages.UpdateMessage;

public class WebSocketSessionHandle extends SessionHandle{
	private WebSocketClientSessionManager	webSocketSessionManager;
	
	public WebSocketSessionHandle(WebSocketClientSessionManager cm)
	{
		webSocketSessionManager = cm;
	}

	public InetSocketAddress getSocketAddress()
	{
		return webSocketSessionManager.getAddress();
	}

	public void sendUpdate(UpdateMessage update)
	{
		webSocketSessionManager.sendUpdateToClient(update);
	}

	public Scope getSessionScope()
	{
		return webSocketSessionManager.getScope();
	}

	public void invalidate()
	{
		webSocketSessionManager.setInvalidating(true);
	}

	public Object getSessionId()
	{
		return webSocketSessionManager.getSessionId();
	}
}
