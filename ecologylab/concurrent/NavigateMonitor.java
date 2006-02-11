package ecologylab.generic;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

public class NavigateMonitor extends Thread
{
   private boolean			running;
   
   public static final int PORT = 8081;
   private Socket 			sock = null;
   
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
			//Generic.go(purl);
			//System.out.println("About to navigate!!!");
			goNavigate(purl);
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
	   if (sock == null)
	   {
		   InetAddress address = null;
			try 
			{
				address = InetAddress.getLocalHost();
				sock = new Socket(address, PORT);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
	   }
	   
		OutputStream out = null;
		//InputStream in = null;
		try 
		{
		    //in = sock.getInputStream();
			out = sock.getOutputStream(); 
		} 
		catch(IOException e) 
		{
		    e.printStackTrace();
		}
	
		//BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		PrintStream writer =
		    new PrintStream(out);
		//BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
		
		//TODO need to change this to XML and use ecologylab.xml
		String line1 = "navigate " + purl.toString();
		try 
		{
		    System.out.println("Navigating to " + purl);
			//line = reader.readLine();
			writer.println(line1);
			//writer.newLine();
			
			writer.flush();
		} 
		catch(Exception e) 
		{
		    e.printStackTrace();
		}
	
		System.out.println("just sent: ");
		System.out.println(line1);
   }
   
}
