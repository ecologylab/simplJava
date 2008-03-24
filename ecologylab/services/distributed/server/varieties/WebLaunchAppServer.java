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
import ecologylab.services.distributed.common.ServicesHostsAndPorts;
import ecologylab.services.distributed.server.DoubleThreadedNIOServer;
import ecologylab.services.distributed.server.clientmanager.AbstractClientManager;
import ecologylab.services.distributed.server.clientmanager.HTTPPostClientManager;
import ecologylab.services.messages.DefaultServicesTranslations;
import ecologylab.xml.TranslationScope;

/**
 * A server that runs on an application that uses web launch, listening for
 * commands from the web browser that launched it. This server expects
 * communications to come from a web-browser, to which it responds with a
 * redirect command. This server can also be used to shut down the launched-web
 * app using a browser. In order for this to work, the application must
 * implement Shutdownable and must be passed into the static get(...) method.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public class WebLaunchAppServer extends DoubleThreadedNIOServer implements
		ServicesHostsAndPorts
{
	private static final int	DEFAULT_IDLE_TIMEOUT	= 10000;

	public static WebLaunchAppServer get(Scope scope) throws BindException,
			IOException
	{
		return new WebLaunchAppServer(
				ServicesHostsAndPorts.WEB_START_APP_SERVICES_PORT, NetTools
						.getAllInetAddressesForLocalhost(),
				DefaultServicesTranslations.get(), scope, DEFAULT_IDLE_TIMEOUT,
				MAX_PACKET_SIZE_CHARACTERS);
	}

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
	protected WebLaunchAppServer(int portNumber, InetAddress[] inetAddresses,
			TranslationScope requestTranslationSpace, Scope objectRegistry,
			int idleConnectionTimeout, int maxPacketSize) throws IOException,
			BindException
	{
		super(portNumber, inetAddresses, requestTranslationSpace, objectRegistry,
				idleConnectionTimeout, maxPacketSize);
	}

	@Override protected AbstractClientManager generateContextManager(
			Object token, SelectionKey sk, TranslationScope translationSpaceIn,
			Scope registryIn)
	{
		return new HTTPPostClientManager(token, maxPacketSize, this.getBackend(),
				this, sk, translationSpaceIn, registryIn);
	}
}
