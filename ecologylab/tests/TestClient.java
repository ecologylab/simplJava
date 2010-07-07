/**
 * 
 */
package ecologylab.tests;

import java.io.IOException;
import java.net.BindException;
import java.net.UnknownHostException;

import ecologylab.services.distributed.client.NIOClient;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
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
