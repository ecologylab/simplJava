package ecologylab.generic;

public class NavigateMonitor extends Thread
{
   private boolean		running;
   
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
			Generic.go(purl);
		 } catch (InterruptedException e)
		 {
			if (running)
			   Debug.println("NavigateMonitor.stop()!");
		 }
	  }
   }
}
