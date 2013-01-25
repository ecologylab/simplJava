/**
 * 
 */
package ecologylab.oodss.distributed.server.varieties;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.nio.channels.SelectionKey;

import ecologylab.collections.Scope;
import ecologylab.oodss.distributed.server.DoubleThreadedNIOServer;
import ecologylab.oodss.distributed.server.clientsessionmanager.HTTPPostClientSessionManager;
import ecologylab.serialization.SimplTypesScope;

/**
 * @author Nic Lupfer (nic@ecologylab.net)
 * 
 */
public class HttpPostServer extends DoubleThreadedNIOServer
{
	/**
	 * @param portNumber
	 * @param inetAddresses
	 * @param requestTranslationSpace
	 * @param objectRegistry
	 * @param idleConnectionTimeout
	 * @param maxPacketSize
	 * @throws IOException
	 * @throws BindException
	 */
	public HttpPostServer(int portNumber, InetAddress[] inetAddresses,
			SimplTypesScope requestTranslationSpace, Scope objectRegistry, int idleConnectionTimeout,
			int maxPacketSize) throws IOException, BindException
	{
		super(portNumber, inetAddresses, requestTranslationSpace, objectRegistry,
				idleConnectionTimeout, maxPacketSize);
	}

	/**
	 * @param portNumber
	 * @param inetAddress
	 * @param requestTranslationSpace
	 * @param objectRegistry
	 * @param idleConnectionTimeout
	 * @param maxPacketSize
	 * @throws IOException
	 * @throws BindException
	 */
	public HttpPostServer(int portNumber, InetAddress inetAddress,
			SimplTypesScope requestTranslationSpace, Scope objectRegistry, int idleConnectionTimeout,
			int maxPacketSize) throws IOException, BindException
	{
		super(portNumber, inetAddress, requestTranslationSpace, objectRegistry, idleConnectionTimeout,
				maxPacketSize);
	}

	@Override
	protected HTTPPostClientSessionManager generateContextManager(String token, SelectionKey sk,
			SimplTypesScope translationScopeIn, Scope registryIn)
	{
		return new HTTPPostClientSessionManager(token, maxMessageSize, this.getBackend(), this, sk,
				translationScopeIn, registryIn);
	}
}
