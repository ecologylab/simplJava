package cm.generic;

/**
 * Generic facilities for concurrent programming.
 */
public class Monitor
extends ObservableDebug
{
   public static void wait(Object toLock)
   {
      synchronized (toLock)
      {
	 try
	 {
	    toLock.wait();
//	    debug("dispatchDownloads() notified");
	 } catch (InterruptedException e)
	 {
	    // interrupt means stop
	 }
      }
   }

   public static void notifyAll(Object lock)
   {
      synchronized (lock)
      {
	 lock.notifyAll();
      }
   }
}
