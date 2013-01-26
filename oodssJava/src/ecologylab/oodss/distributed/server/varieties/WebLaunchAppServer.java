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
import ecologylab.net.NetTools;
import ecologylab.oodss.distributed.common.ServicesHostsAndPorts;
import ecologylab.oodss.distributed.server.DoubleThreadedNIOServer;
import ecologylab.oodss.distributed.server.clientsessionmanager.HTTPPostClientSessionManager;
import ecologylab.oodss.messages.DefaultServicesTranslations;

/**
 * A server that runs on an application that uses web launch, listening for commands from the web
 * browser that launched it. This server expects communications to come from a web-browser, to which
 * it responds with a redirect command. This server can also be used to shut down the launched-web
 * app using a browser. In order for this to work, the application must implement Shutdownable and
 * must be passed into the static get(...) method.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public class WebLaunchAppServer extends DoubleThreadedNIOServer implements ServicesHostsAndPorts
{
	private static final int	DEFAULT_IDLE_TIMEOUT	= 10000;

	public static WebLaunchAppServer get(Scope scope) throws BindException, IOException
	{
		return new WebLaunchAppServer(ServicesHostsAndPorts.WEB_START_APP_SERVICES_PORT, NetTools
				.getAllInetAddressesForLocalhost(), DefaultServicesTranslations.get(), scope,
				DEFAULT_IDLE_TIMEOUT, DEFAULT_MAX_MESSAGE_LENGTH_CHARS);
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
			SimplTypesScope requestTranslationSpace, Scope objectRegistry, int idleConnectionTimeout,
			int maxPacketSize) throws IOException, BindException
	{
		super(portNumber, inetAddresses, requestTranslationSpace, objectRegistry,
				idleConnectionTimeout, maxPacketSize);
	}

	@Override
	protected HTTPPostClientSessionManager generateContextManager(String token, SelectionKey sk,
			SimplTypesScope translationScopeIn, Scope registryIn)
	{
		return new HTTPPostClientSessionManager(token, maxMessageSize, this.getBackend(), this, sk,
				translationScopeIn, registryIn);
	}
}
