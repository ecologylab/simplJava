/**
 * 
 */
package ecologylab.services.distributed.server.varieties;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.nio.channels.SelectionKey;

import ecologylab.appframework.Scope;
import ecologylab.net.NetTools;
import ecologylab.services.distributed.server.DoubleThreadedNIOServer;
import ecologylab.services.distributed.server.clientmanager.AbstractClientManager;
import ecologylab.services.distributed.server.clientmanager.HTTPGetClientManager;
import ecologylab.services.messages.DefaultServicesTranslations;
import ecologylab.xml.TranslationSpace;

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
			TranslationSpace requestTranslationSpace,
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
			TranslationSpace requestTranslationSpace,
			Scope objectRegistry, int idleConnectionTimeout,
			int maxPacketSize) throws IOException, BindException
	{
		super(portNumber, inetAddress, requestTranslationSpace, objectRegistry,
				idleConnectionTimeout, maxPacketSize);
	}

	@Override protected AbstractClientManager generateContextManager(
			Object token, SelectionKey sk, TranslationSpace translationSpaceIn,
			Scope registryIn)
	{
		return new HTTPGetClientManager(token, maxPacketSize, this.getBackend(),
				this, sk, translationSpaceIn, registryIn);
	}
}
