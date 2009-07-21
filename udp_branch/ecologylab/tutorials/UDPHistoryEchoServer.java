package ecologylab.tutorials;
import java.io.IOException;
import java.net.InetAddress;

import ecologylab.collections.Scope;
import ecologylab.net.NetTools;
import ecologylab.services.distributed.server.DoubleThreadedNIOServer;
import ecologylab.services.distributed.server.NIODatagramServer;
import ecologylab.services.messages.DefaultServicesTranslations;
import ecologylab.xml.TranslationScope;

/** 
 * HistoryEchoServer: A sample server implemented via OODSS.
 * Intended to be used as a tutorial application.
 */
public class UDPHistoryEchoServer
{
	private static final int idleTimeout = -1;
	private static final int MTU = 1200;
	
	public static void main(String[] args) throws IOException
	{
		/*
		 *  get base translations with static accessor
		 */
		TranslationScope baseServices = DefaultServicesTranslations.get();
		
		/* 
		 * Classes that must be translated by the translation scope
		 * in order for the server to communicate w/ the client
		 */
		Class[] historyEchoClasses = { HistoryEchoRequest.class,
				 HistoryEchoResponse.class };
		
		/*
		 * compose translations, to create the “histEchoTrans”
		 * space inheriting the base translations
		 */
		TranslationScope histEchoTranslations = TranslationScope.get("histEchoTrans",
				historyEchoClasses, baseServices);	
		
		/* 
		 * Creates a scope for the server to use as an application scope
		 * as well as individual client session scopes.
		 */
		Scope applicationScope = new Scope();
		
		/* Acquire an array of all local ip-addresses */
		InetAddress[] locals = NetTools.getAllInetAddressesForLocalhost();
		
		/* 
		 * Create the server and start the server so that it can
		 * accept incoming connections.
		 */
		NIODatagramServer historyServer = new NIODatagramServer(2107,
																				  histEchoTranslations,
																				  applicationScope, true);
		
	}
	
}
