package ecologylab.generic;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

import ecologylab.services.ServicesClient;
import ecologylab.services.messages.BrowseCommand;
import ecologylab.xml.NameSpace;
import ecologylab.xml.XmlTranslationException;

public class NavigateMonitor extends Thread
{
   private boolean			running;
   
   public static final int PORT = 10001;
   
   private static final NameSpace messageSpace = 
	new NameSpace("Browse", "ecologylab.services.messages");
   
   public ServicesClient servicesClient = new ServicesClient(PORT, messageSpace);
   
   static 
   {
   		messageSpace.addTranslation("ecologylab.services.messages", "BrowseCommand");
   }
   
   /**
    * Initialiazed to true, hoping for the best.
    * Will get set to false if the assumption turns out not to be valid.
    * Indicates there is a server to connect to to perform navigation.
    * Otherwise, try to use more local options.
    */
   private boolean			hasNavigateServer	= true;
   
   public NavigateMonitor(String name)
   {
	  super(name);
	  running			= true;
	  start();
   }
   public synchronized void stopRunning()
   {
	  if (running)
	  {
		 running		= false;
		 interrupt();
	  }
   }
   
/**
 * The next location we'll navigate to.
 */
   ParsedURL			purl;
   
   /**
	* Raise embellishments after a suitable delay, unless a cancel comes
	* in first().
	*/
   public synchronized void navigate(ParsedURL purl)
   {
	  //println("RolloverFrame.delayThenShow()");
	  this.purl		= purl;
	  notify();
   }
   
   public synchronized void run()
   {
	  while (running)
	  {
		 try
		 {  
			wait();
			// does the actual navigate
			if (hasNavigateServer)
			{
				Debug.println("Navigate with navigateServer to " + purl);
				goNavigate(purl);
			}
			else
			{
				Debug.println("Navigate with local Generic.go() to " + purl);
				Generic.go(purl);
			}
			
		 } catch (InterruptedException e)
		 {
			if (running)
			   Debug.println("NavigateMonitor.stop()!");
		 }
	  }
   }
   
   /**
    * Acts as a client to a BrowserServer running as an applet in some browser on the
    * default port. Connects if necessary (lazy evaluation) and then sends navigation
    * urls. 
    * @param purl
    */
   private void goNavigate(ParsedURL purl)
   {
	   //lazy evaluation for socket creation
	   if (!servicesClient.connected())
	   		servicesClient.connect();
	   
	   if (!servicesClient.connected())
	   {
	   		hasNavigateServer = false;
	   		Generic.go(purl);
	   		return;
	   }
	
	   //Create the browse message
	   BrowseCommand browseCommand = new BrowseCommand("navigate", purl.toString());
	   
	   //send it
	   System.out.println("Navigating to " + purl);
	   servicesClient.sendMessage(browseCommand);
		
   }
   
}
