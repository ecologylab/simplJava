package ecologylab.generic;

/**
 * Generic facilities for concurrent programming.
 */
public class Monitor
extends ObservableDebug
{
   boolean done;
   
   /**
    * Create a Monitor with a single done flag, that can be waited on.
    *
    */
   public Monitor()
   {
	   
   }
   /**
    * If this is not yet done, then wait(); 
    * else return immediately.
    */
   public synchronized void waitForDone()
   {
	   if (!done)
		   wait(this);
   }
   /**
    * Mark this as done, as needed, and notify all waiting threads.
    */
   public synchronized void done()
   {
	   done		= true;
	   notifyAll(this);
   }
   /**
    * Short-hand form: wait for the Object. Handles Interrupted exceptions.
    * @param toLock
    * @return	true if the wait was completed without getting interrupted.
    */
   public static boolean wait(Object toLock)
   {
      synchronized (toLock)
      {
		 try
		 {
		    toLock.wait();
		    return true;
	//	    debug("dispatchDownloads() notified");
		 } catch (InterruptedException e)
		 {
		    // interrupt means stop
		    e.printStackTrace();
		    return false;
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
   public static void notify(Object lock)
   {
      synchronized (lock)
      {
		 lock.notify();
      }
   }
}
