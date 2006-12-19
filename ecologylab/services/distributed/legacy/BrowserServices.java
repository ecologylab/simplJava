package ecologylab.services;


import java.io.IOException;


import ecologylab.generic.ObjectRegistry;
import ecologylab.xml.TranslationSpace;

/**
 * Provide socket based browser services.
 * 
 * @author blake
 */
public class BrowserServices 
extends ServicesServer
implements SessionObjects, ServicesHostsAndPorts
{
	/**
	 * Creates a new instance of the BrowserServer listening on the default port
	 */
	public BrowserServices(int portNumber, ObjectRegistry objectRegistry)
	throws IOException, java.net.BindException
	{
		//Create the socket server. For now don't use a socket manager because we expect
		//only 1 connection at a time.
		super(portNumber, 0, BROWSER_SERVICES_TRANSLATIONS, objectRegistry);
	}
	
	/**
	 * Creates a new instance of the BrowserServer listening on the default port
	 */
	public BrowserServices(ObjectRegistry objectRegistry)
	throws IOException, java.net.BindException
	{
		//Create the socket server. For now don't use a socket manager because we expect
		//only 1 connection at a time.
		super(BROWSER_SERVICES_PORT, 0, BROWSER_SERVICES_TRANSLATIONS, objectRegistry);
	}
	
	public BrowserServices(int portNumber, TranslationSpace translations, ObjectRegistry objectRegistry)
	throws IOException, java.net.BindException
	{
		super(portNumber, 0, translations, objectRegistry);
	}

	/**
	 * This is the actual way to create an instance of this.
	 * 
	 * @param portNumber
	 * @param objectRegistry
	 * @return	A server instance, or null if it was not possible to open a ServerSocket
	 * 			on the port on this machine.
	 */
	public static BrowserServices get(int portNumber, ObjectRegistry objectRegistry)
	{
		return (BrowserServices) get(portNumber, BROWSER_SERVICES_TRANSLATIONS, objectRegistry);
	}
	
	public static ServicesServer get(int portNumber, TranslationSpace translations, ObjectRegistry objectRegistry)
	{
		BrowserServices newServer	= null;
		try
		{
			newServer	= new BrowserServices(portNumber, translations, objectRegistry);
		} catch (IOException e)
		{
			println("BrowserServices ERROR: can't open ServerSocket on port " + portNumber);
			e.printStackTrace();
		}
		return newServer;
	}
}
