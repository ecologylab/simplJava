/**
 * 
 */
package ecologylab.oodss.distributed.server.varieties;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.nio.channels.SelectionKey;

import simpl.core.SimplTypesScope;
import ecologylab.collections.Scope;
import ecologylab.oodss.distributed.server.DoubleThreadedNIOServer;
import ecologylab.oodss.distributed.server.clientsessionmanager.HTTPGetClientSessionManager;

/**
 * @author Zachary O. Toups (zach@ecologylab.net)
 * 
 */
public class HttpGetServer extends DoubleThreadedNIOServer
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
	public HttpGetServer(int portNumber, InetAddress[] inetAddresses,
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
	public HttpGetServer(int portNumber, InetAddress inetAddress,
			SimplTypesScope requestTranslationSpace, Scope objectRegistry, int idleConnectionTimeout,
			int maxPacketSize) throws IOException, BindException
	{
		super(portNumber, inetAddress, requestTranslationSpace, objectRegistry, idleConnectionTimeout,
				maxPacketSize);
	}

	@Override
	protected HTTPGetClientSessionManager generateContextManager(String token, SelectionKey sk,
			SimplTypesScope translationScopeIn, Scope registryIn)
	{
		return new HTTPGetClientSessionManager(token, maxMessageSize, this.getBackend(), this, sk,
				translationScopeIn, registryIn);
	}
}
