package ecologylab.services.distributed.server.clientsessionmanager;

import java.net.InetAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;

import ecologylab.collections.Scope;
import ecologylab.services.messages.UpdateMessage;

/**
 * 
 * @author William A. Hamilton (bill@ecologylab.net)
 */
public class SessionHandle
{
	private TCPClientSessionManager	sessionManager;

	public SessionHandle(TCPClientSessionManager cm)
	{
		sessionManager = cm;
	}

	private Socket getSocket()
	{
		return ((SocketChannel) sessionManager.getSocketKey().channel()).socket();
	}

	public InetAddress getInetAddress()
	{
		return getSocket().getInetAddress();
	}

	public int getPortNumber()
	{
		return getSocket().getPort();
	}

	public void sendUpdate(UpdateMessage update)
	{
		sessionManager.sendUpdateToClient(update);
	}

	public Scope getSessionScope()
	{
		return sessionManager.getScope();
	}

	public void invalidate()
	{
		sessionManager.setInvalidating(true);
	}

	public Object getSessionId()
	{
		return sessionManager.getSessionId();
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof SessionHandle)
		{
			return this.getSessionId().equals(((SessionHandle) other).getSessionId());
		}
		else
		{
			return false;
		}
	}
}
