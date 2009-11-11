/**
 * 
 */
package ecologylab.standalone;

import java.io.IOException;

import ecologylab.collections.Scope;
import ecologylab.services.distributed.client.NIOClient;
import ecologylab.services.distributed.exception.MessageTooLargeException;
import ecologylab.services.messages.DefaultServicesTranslations;
import ecologylab.services.messages.Ping;

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
