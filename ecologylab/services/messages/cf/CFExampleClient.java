/**
 * 
 */
package ecologylab.services.messages.cf;

import ecologylab.generic.Debug;
import ecologylab.generic.Generic;
import ecologylab.services.ServicesClient;
import ecologylab.services.ServicesHostsAndPorts;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.TranslationSpace;

/**
 * Example of how to use ecologylab.services to form a combinFormation client,
 * and send search queries and such to the server.
 * 
 * @author andruid
 */
public class CFExampleClient extends Debug
{
	static final TranslationSpace	CF_TRANSLATIONS		= CFMessagesTranslations.get();
	 
	/**
	 * 
	 */
	public CFExampleClient()
	{
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		ServicesClient cfClient	= new ServicesClient(ServicesHostsAndPorts.CF_SERVICES_PORT, CF_TRANSLATIONS);
		
		// in case the server is not up yet, wait for it.
		cfClient.waitForConnect();

		SearchState	search1	= new SearchState("google", "ann hamilton installation");
		
		SeedCf	seedCf		= new SeedCf();
		
		seedCf.add(search1);
		
		// send message and wait for response
		ResponseMessage response	= cfClient.sendMessage(seedCf);
		
		if (!response.isOK())
		{
			println("Bad response from server. Quitting.\n" + response);
			return;
		}
			
		
		// wait for 40 seconds
		Generic.sleep(40000);
		
		SearchState	search2	= new SearchState("yahoo_news", "iraq");
		
		seedCf.clear();
		
		seedCf.add(search2);
		
		// send message and wait for response
		response	= cfClient.sendMessage(seedCf);
		
		if (!response.isOK())
		{
			println("Bad response from server. Quitting.\n" + response);
			return;
		}
	}

}
