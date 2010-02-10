/**
 * 
 */
package ecologylab.tests;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import ecologylab.services.distributed.client.NIOClient;
import ecologylab.services.distributed.server.DoubleThreadedNIOServer;

/**
 * @author Zachary O. Toups (zach@ecologylab.net)
 *
 */
public class TestClient
{

	/**
	 * @param args
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws BindException 
	 */
	public static void main(String[] args) throws BindException, UnknownHostException, IOException
	{
		NIOClient c = new NIOClient("localhost", 14444, null, null);
		
		c.connect();
	}

}
