/**
 * 
 */
package ecologylab.services.distributed.server.varieties;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.nio.channels.SelectionKey;

import ecologylab.collections.Scope;
import ecologylab.net.NetTools;
import ecologylab.services.distributed.server.DoubleThreadedNIOServer;
import ecologylab.services.distributed.server.clientsessionmanager.AbstractClientSessionManager;
import ecologylab.services.distributed.server.clientsessionmanager.HTTPGetClientSessionManager;
import ecologylab.services.messages.DefaultServicesTranslations;
import ecologylab.xml.TranslationScope;

/**
 * @author toupsz
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
			TranslationScope requestTranslationSpace,
			Scope objectRegistry, int idleConnectionTimeout,
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
			TranslationScope requestTranslationSpace,
			Scope objectRegistry, int idleConnectionTimeout,
			int maxPacketSize) throws IOException, BindException
	{
		super(portNumber, inetAddress, requestTranslationSpace, objectRegistry,
				idleConnectionTimeout, maxPacketSize);
	}

	@Override protected AbstractClientSessionManager generateContextManager(
			Object token, SelectionKey sk, TranslationScope translationSpaceIn,
			Scope registryIn)
	{
		return new HTTPGetClientSessionManager(token, maxMessageSize, this.getBackend(),
				this, sk, translationSpaceIn, registryIn);
	}
}
