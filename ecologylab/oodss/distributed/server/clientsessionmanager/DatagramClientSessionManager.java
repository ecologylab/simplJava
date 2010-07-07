/**
 * 
 */
package ecologylab.oodss.distributed.server.clientsessionmanager;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;

import ecologylab.collections.Scope;
import ecologylab.oodss.distributed.server.NIOServerProcessor;
import ecologylab.oodss.messages.RequestMessage;
import ecologylab.oodss.messages.ResponseMessage;

/**
 * @author Zachary O. Toups (zach@ecologylab.net)
 * 
 */
public class DatagramClientSessionManager extends BaseSessionManager
{
	SocketAddress	address;

	/**
	 * @param sessionId
	 * @param socket
	 * @param baseScope
	 */
	public DatagramClientSessionManager(String sessionId, NIOServerProcessor frontend,
			SelectionKey socket, Scope<?> baseScope, SocketAddress	address)
	{
		super(sessionId, frontend, socket, baseScope);
		
		this.address = address;
	}

	/**
	 * Calls RequestMessage.performService(Scope) and returns the result.
	 * 
	 * @param request
	 *          - the request message to process.
	 */
	@Override
	public ResponseMessage processRequest(RequestMessage request, InetAddress address)
	{
		return super.processRequest(request, address);
	}

	/**
	 * @return the address
	 */
	public SocketAddress getAddress()
	{
		return address;
	}
}
