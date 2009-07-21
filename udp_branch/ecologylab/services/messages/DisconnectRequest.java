/**
 * 
 */
package ecologylab.services.messages;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import ecologylab.collections.Scope;
import ecologylab.services.distributed.client.NIOClient;
import ecologylab.services.distributed.exception.MessageTooLargeException;
import ecologylab.services.distributed.server.DoubleThreadedNIOServer;
import ecologylab.services.distributed.server.clientsessionmanager.AbstractClientSessionManager;
import ecologylab.xml.xml_inherit;

/**
 * A request that indicates that the client wishes to be permanently
 * disconnected from the server.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
@xml_inherit public class DisconnectRequest extends RequestMessage
{
	public static final DisconnectRequest	REUSABLE_INSTANCE	= new DisconnectRequest();

	/**
	 * 
	 */
	public DisconnectRequest()
	{
		super();
	}

	/**
	 * @see ecologylab.services.messages.RequestMessage#performService(ecologylab.collections.Scope)
	 */
	@Override public ResponseMessage performService(Scope localScope)
	{
		debug("**** running disconnect request ****");
		
		AbstractClientSessionManager cm = (AbstractClientSessionManager) localScope
				.get(AbstractClientSessionManager.CLIENT_MANAGER);

		cm.setInvalidating(true);

		return OkResponse.reusableInstance;
	}

	public static void main(String[] args) throws BindException,
			UnknownHostException, IOException, MessageTooLargeException
	{
		DoubleThreadedNIOServer server = DoubleThreadedNIOServer.getInstance(
				10000, InetAddress.getLocalHost(), DefaultServicesTranslations
						.get(), new Scope(), 9999, 99999);

		server.start();

		System.err.println("c1 instantiate");
		NIOClient c1 = new NIOClient("128.194.147.181", 10000,
				DefaultServicesTranslations.get(), new Scope());

		System.err.println("c1 connect");
		c1.connect();

		System.err.println("c2 instantiate");
		NIOClient c2 = new NIOClient("128.194.147.181", 10000,
				DefaultServicesTranslations.get(), new Scope());

		System.err.println("c2 connect");
		c2.connect();

		System.err.println("c1 disconnect");
		c1.sendMessage(new DisconnectRequest());

		System.err.println("c2 disconnect");
		c2.sendMessage(new DisconnectRequest());

		System.err.println("c1 disconnect again");
		c1.sendMessage(new DisconnectRequest());
	}
}
