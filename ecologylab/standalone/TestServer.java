/**
 * 
 */
package ecologylab.standalone;

import java.io.IOException;
import java.net.BindException;

import ecologylab.collections.Scope;
import ecologylab.net.NetTools;
import ecologylab.services.distributed.server.DoubleThreadedNIOServer;
import ecologylab.services.messages.DefaultServicesTranslations;

/**
 * @author Zachary O. Toups (zach@ecologylab.net)
 *
 */
public class TestServer
{

	/**
	 * @param args
	 * @throws IOException 
	 * @throws BindException 
	 */
	public static void main(String[] args) throws BindException, IOException
	{
		DoubleThreadedNIOServer s = DoubleThreadedNIOServer.getInstance(
				7833, 
				NetTools.getAllInetAddressesForLocalhost(), 
				DefaultServicesTranslations.get(), 
				new Scope(), 
				100000, 
				100000);
		
		s.start();
	}

}
