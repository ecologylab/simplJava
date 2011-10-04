/**
 * 
 */
package ecologylab.standalone;

import java.io.IOException;

import ecologylab.collections.Scope;
import ecologylab.oodss.distributed.client.NIOClient;
import ecologylab.oodss.distributed.exception.MessageTooLargeException;
import ecologylab.oodss.messages.DefaultServicesTranslations;
import ecologylab.oodss.messages.Ping;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 *
 */
public class TestClient
{

	/**
	 * @param args
	 * @throws IOException 
	 * @throws MessageTooLargeException 
	 */
	public static void main(String[] args) throws IOException, MessageTooLargeException
	{
		NIOClient c = new NIOClient("localhost", 7833,
				DefaultServicesTranslations.get(), new Scope());
		
		c.isServerRunning();

		c.connect();
		
		long startTime = System.currentTimeMillis();
		c.sendMessage(new Ping());
		long endTime = System.currentTimeMillis();
		System.out.println("round trip: "+(endTime - startTime)+"ms");
		
		startTime = System.currentTimeMillis();
		c.sendMessage(new Ping());
		endTime = System.currentTimeMillis();
		System.out.println("round trip: "+(endTime - startTime)+"ms");
		
		startTime = System.currentTimeMillis();
		c.sendMessage(new Ping());
		endTime = System.currentTimeMillis();
		System.out.println("round trip: "+(endTime - startTime)+"ms");
		
		c.disconnect();
	}

}
