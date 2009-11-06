package ecologylab.tutorials;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;

import ecologylab.xml.TranslationScope;
import ecologylab.collections.Scope;
import ecologylab.services.distributed.client.*;
import ecologylab.services.distributed.exception.MessageTooLargeException;
import ecologylab.services.messages.DefaultServicesTranslations;

/** 
 * HistoryEchoServer: A sample server implemented via OODSS.
 * Intended to be used as a tutorial application.
 */
public class UDPHistoryEchoClient
{
	public static void main(String[] args) throws IOException
	{
		NIODatagramClient client;
		String serverAddress;
		int portNumber;
		
		Scanner scan = new Scanner(System.in);
		
		
		/*
		 * Input server address and port number
		 */
		System.out.print("Please enter server: ");
		serverAddress = scan.next();

		System.out.println("Please enter port: ");
		portNumber = Integer.parseInt(scan.next());
		
		System.out.println("Connecting to " + serverAddress + " on port# " + portNumber);
		
		Class[] historyEchoClasses = { HistoryEchoRequest.class,
												 HistoryEchoResponse.class };
		
		/*
		 *  Get base translations with static accessor
		 */
		TranslationScope baseServices = DefaultServicesTranslations.get();
		
		/*
		 * compose translations, to create the “histEchoTrans”
		 * space inheriting the base translations
		 */
		TranslationScope histEchoTranslations = 
			TranslationScope.get("histEchoTrans",
										baseServices,
										historyEchoClasses);	
		
		Scope clientScope = new Scope();
		
		InetSocketAddress addr = new InetSocketAddress(serverAddress, portNumber);
		
		client = new NIODatagramClient<Scope>(addr, new InetSocketAddress(58010), histEchoTranslations,clientScope, true, 500);
				
		int x = 1;
		
		while(true)
		{
			String input = "ping!" + scan.nextLine();
			
			if(input.trim().toLowerCase().equals("exit"))
				break;
			
			HistoryEchoRequest echoRequest = new HistoryEchoRequest(input);
			
			client.sendMessage(echoRequest);
			
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}
}
