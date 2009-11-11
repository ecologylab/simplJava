/**
 * 
 */
package ecologylab.tests;

import java.io.IOException;
import java.net.BindException;
import java.net.UnknownHostException;

import ecologylab.generic.Debug;
import ecologylab.net.NetTools;
import ecologylab.services.distributed.client.NIOClient;
import ecologylab.services.distributed.server.DoubleThreadedNIOServer;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 *
 */
public class TestServer
{

	/**
	 * @param args
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws BindException 
	 */
	public static void main(String[] args) throws BindException, UnknownHostException, IOException
	{
		DoubleThreadedNIOServer s = DoubleThreadedNIOServer.getInstance(14444, NetTools.getAllInetAddressesForLocalhost(), null, null, 11111111, 1111111111);
		
		s.start();
		
		NIOClient c = new NIOClient("localhost", 14444, null, null);
		
		Debug.println("------------------------------------ Start connect.");
		c.connect();
		Debug.println("------------------------------------ connect returned.");
	}

}
