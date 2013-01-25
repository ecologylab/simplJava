package ecologylab.oodss.distributed.server.clientsessionmanager;

import java.net.InetSocketAddress;

import ecologylab.collections.Scope;
import ecologylab.oodss.messages.UpdateMessage;

public class WebSocketSessionHandle extends SessionHandle{
	private WebSocketClientSessionManager	webSocketSessionManager;
	
	public WebSocketSessionHandle(WebSocketClientSessionManager cm)
	{
		webSocketSessionManager = cm;
	}

	@Override
	public InetSocketAddress getSocketAddress()
	{
		return webSocketSessionManager.getAddress();
	}

	@Override
	public void sendUpdate(UpdateMessage update)
	{
		webSocketSessionManager.sendUpdateToClient(update);
	}

	@Override
	public Scope getSessionScope()
	{
		return webSocketSessionManager.getScope();
	}

	@Override
	public void invalidate()
	{
		webSocketSessionManager.setInvalidating(true);
	}

	@Override
	public Object getSessionId()
	{
		return webSocketSessionManager.getSessionId();
	}
}
