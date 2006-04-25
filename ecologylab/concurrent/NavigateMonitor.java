package ecologylab.generic;

import ecologylab.services.ServicesClient;
import ecologylab.services.ServicesHostsAndPorts;
import ecologylab.services.messages.Navigate;
import ecologylab.xml.NameSpace;

/**
 * Provide navigation to a web page, in a web browser.
 * Does this in a separate thread from the caller's.
 * Thus, this is suitable for call from an event handler.
 * <p/>
 * Uses the BrowserServer, if one can be found.
 * Otherwise, uses JavaScript to a browser, if we're inside a Playlet.
 * Otherwise, seeks Firefox and if found, exec's a command at the OS level.
 *
 * @author andruid
 */
public class NavigateMonitor extends Thread
{
   private boolean			running;
   
   private static final NameSpace messageSpace = NameSpace.get("Browse", "ecologylab.services.messages");
   
   protected ServicesClient servicesClient = new ServicesClient(ServicesHostsAndPorts.BROWSER_SERVER_PORT, messageSpace);
   
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
		// interrupt();
		 notify();
	  }
   }
   
/**
 * The next location we'll navigate to.
 */
   ParsedURL			purl;
   
   /**
    * Provide navigation to a web page, in a web browser.
    * Does this in a separate thread from the caller's.
    * Thus, this is suitable for call from an event handler.
    * <p/>
    * Uses the BrowserServer, if one can be found.
    * Otherwise, uses JavaScript to a browser, if we're inside a Playlet.
    * Otherwise, seeks Firefox and if found, exec's a command at the OS level.
    */
   public synchronized void navigate(ParsedURL purl)
   {
	  //println("RolloverFrame.delayThenShow()");
	  this.purl		= purl;
	  notify();
   }
   
   public void run()
   {
	   synchronized (this)
	   {
		   while (running)
		   {
			   try
			   {  
				   wait();
				   if (!running)
					   break;
				   
			   } catch (InterruptedException e)
			   {
				   e.printStackTrace();
			   }
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
	   Navigate browseCommand = new Navigate(purl);
	   
	   //send it
	   Debug.println(this, "Use BrowserServer to navigate to " + purl);
	   servicesClient.sendMessage(browseCommand);
   }
 }
