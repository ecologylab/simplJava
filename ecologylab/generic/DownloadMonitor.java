package cm.generic;

import java.util.*;

/**
 * Non-linear flow multiplexor.
 * Tracks downloads of <code>Downloadable</code> objects.
 * Dispatches downloaded media to the appropriate <code>DispatchTarget</code>.
 * <p>
 * Looks out for timeout conditions. In case they happen, records
 * state in the <code>bad</code> slot, and dispatches, as well.
 */
public class DownloadMonitor
extends ObservableDebug
implements Runnable
{
   static final int	TIMEOUT		= 25000;
   static final int	TIMEOUT_SLEEP	= 4000;
   static final int	SHORT_SLEEP	= 50;

//   public static final int HIGH_PRIORITY	= 4;
   public static final int HIGH_PRIORITY	= 3;
   public static final int MID_PRIORITY		= 2;
   public static final int LOW_PRIORITY		= 2;
   
   public final int highThreshold;
   public final int midThreshold;
   
   int			timeouts;
   int			dispatched;

   boolean		getUrgent;

   //////////////////// queues for media that gets downloaded /////////////////
/**
 * This is the queue of DownloadClosures waiting to be downloaded.
 */
   Vector		toDownload	= new Vector(30);
/**
 * This is the queue of media that we start to download.
 * This queue drives potential timeout dispatches.
 */
   Vector		potentialTimeouts	= new Vector(30);
/**
 * This is the queue of media that finish downloading.
 * This queue drives normal dispatch to <code>DispatchTarget</code>s, when
 * media arrives properly.
 */
   Vector		toDispatch	= new Vector(30);

   Thread		timeoutThread;
   Thread		dispatchThread	= null;
   Thread[]		downloadThreads;
   
   int			numDownloadThreads;
   String		name;
   
   static boolean	finished;
   
   public DownloadMonitor()
   {
      this(1);
   }
   public DownloadMonitor(int numDownloadThreads)
   {
      this("", numDownloadThreads, false);
   }
   public DownloadMonitor(String name, int numDownloadThreads)
   {
      this (name, numDownloadThreads, false);
   }
   public DownloadMonitor(String name, int numDownloadThreads, 
			  boolean getUrgent)
   {
      this.numDownloadThreads	= numDownloadThreads;
      this.name			= name;
      this.getUrgent		= getUrgent;
      
      highThreshold		= numDownloadThreads * 2;
      midThreshold		= numDownloadThreads + 1;
      finished			= false;
   }

   public void run()
   {
      detectTimeouts();
   }
   
/**
 * This loop looks out for timeouts.
 */
   void detectTimeouts()
   {
      while (!finished)
      {
	 try
	 {
	    long now	= System.currentTimeMillis();
	    DownloadClosure thatClosure	= null;
	    while (!potentialTimeouts.isEmpty())
	    {
	       thatClosure	= 
		  (DownloadClosure) potentialTimeouts.firstElement();
	       if (thatClosure.timeoutOrComplete(now))
	       {
		  potentialTimeouts.remove(0);
		  if (thatClosure.timedOut() && !thatClosure.timeoutResolved)
		     restartDownloadThread(thatClosure.downloadingThread);
	       }
	       else
		  break;
	    }
	    int sleep;	    
	    if (thatClosure != null)
	    {
	       int thatDue	= TIMEOUT - thatClosure.deltaTime(now);
//	       debug("detectTimeouts() deltaTime="+thatClosure.deltaTime(now)+
//		     " thatDue="+thatDue);
	       sleep	= (thatDue > TIMEOUT_SLEEP) ? thatDue : TIMEOUT_SLEEP;
	    }
	    else
	       sleep		= TIMEOUT;
//	    debug("detectTimeouts() sleeping for " + sleep);
	    Generic.sleep(sleep);
	 } catch (OutOfMemoryError e)
	 {
	    Memory.recover(e, getClassName() + ".run() trying to recover.");
	 }
      }
      timeoutThread	= null;
   }
/**
 * Entry point for <code>Downloadable</code>s that are already downloading
 * through some other mechanism. All we do for these is make sure they dont
 * timeout.
 */
   public DownloadClosure detectPotentialTimeout(Downloadable thatMedia,
					       DispatchTarget dispatchTarget)
   {
      DownloadClosure closure = 
	 new DownloadClosure(thatMedia,  dispatchTarget, this);
      detectPotentialTimeout(closure);
      return closure;
   }
   protected void detectPotentialTimeout(DownloadClosure closure,
					 Thread downloadingThread)
   {
      closure.downloadingThread	= downloadingThread;
      detectPotentialTimeout(closure);
   }
   protected void detectPotentialTimeout(DownloadClosure closure)
   {
      closure.startingNow();
      potentialTimeouts.addElement(closure);
      if (timeoutThread == null)
	 startTimeoutMonitor();
   }
   private void startTimeoutMonitor()
   {
      synchronized (potentialTimeouts)
      {
	 if (timeoutThread == null)
	 {
	    timeoutThread	= new Thread(this, toString() + "-timeouts");
	    timeoutThread.setPriority(LOW_PRIORITY);
	    timeoutThread.start(); // to our run method
	 }
      }
   }

   //----------------------- perform dispatches -----------------------------//
/**
 * Entry point for siphon threads. We will queue a DispatchTarget
 * for dispatching by our dispatch thread, when we are ready.
 * Starts the dispatching thread, if necessary.
 */
   public void dispatch(Downloadable thatDownloadable,
			DispatchTarget dispatchTarget)
   {
//    debug("dispatch("+thatDownloadable);
      DownloadClosure	downloadClosure = 
	 new DownloadClosure(thatDownloadable, dispatchTarget, this);
      potentialTimeouts.remove(downloadClosure);
      synchronized (toDispatch)
      {
//	 debug("dispatch(in synch"+thatDownloadable);
	 toDispatch.addElement(downloadClosure);
	 if (dispatchThread == null)
	    startDispatchMonitor();
	 else
	    toDispatch.notify();
      }
   }
   private void startDispatchMonitor()
   {
//      finished		= false;
      
      if (dispatchThread == null)
      {
//	 debug("startDispatcHMonitor()");
	 dispatchThread	= new Thread(toString() + "-dispatching")
	 {
	    // !!! if its not in its own thread, the java.awt imaging sys
	    // can get messed up.
	    // dont let imageUpdate threads call the client!
	    public void run()
	    {
	       performDispatches();
	    }
	 };
	 dispatchThread.setPriority(LOW_PRIORITY);
	 dispatchThread.start();
      }
   }

   void performDispatches()
   {
      while (!finished)
      {
	 DownloadClosure thatClosure;
	 synchronized (toDispatch)
	 {
	    if (toDispatch.isEmpty())
	       try
	       {
//		  debug("dispatchDownloads() wait()");
		  toDispatch.wait();
//		  debug("dispatchDownloads() notified");
	       } catch (InterruptedException e)
	       {
		  // interrupt means stop
	       }
	    if (finished)
	       break;
	    thatClosure = (DownloadClosure) toDispatch.remove(0);
	 }
	 // must be outside the lock on toDispatch!
	 try
	 {
	    thatClosure.dispatch();
	 } catch (OutOfMemoryError e)
	 { 
	    Memory.recover(e, getClassName() + 
			   ".performDispatches() trying to recover.");
	 } catch (Throwable e)
	 {
	    debugA(".dispatch -- got exception:");
	    e.printStackTrace();
	 }
	 Generic.sleep(SHORT_SLEEP);
      }  // while (!finished)
   }

   //----------------------- perform downloads ------------------------------//
/**
 * Entry point for <code>Downloadable</code>s that want to be downloaded
 * by 1 of our performDownload() threads.
 * Starts the performDownload() threads, if necessary.
 */
   public void download(Downloadable thatDownloadable,
			DispatchTarget dispatchTarget)
   {
      synchronized (toDownload)
      {
	 toDownload.addElement(new DownloadClosure(thatDownloadable,
						   dispatchTarget, this));
	 if (downloadThreads == null)
	    startDownloadMonitor();
	 else
	    toDownload.notify();
      }
   }
   private int setDownloadPriority(Thread t)
   {
      int waiting	= toDownload.size();
      int priority;
      if (waiting >= midThreshold)
	 priority	= MID_PRIORITY;
      else if (waiting >= highThreshold)
	 priority	= HIGH_PRIORITY;
      else
	 priority	= LOW_PRIORITY;
      t.setPriority(priority);
      return priority;
   }
   private void restartDownloadThread(Thread t)
   {
      debug("restartDownloadThread(" + t);

      for (int i=0; i<downloadThreads.length; i++)
      {
	 if (downloadThreads[i] == t)
	 {
	    t.stop();
	    t		= newDownloadThread(i);
	    downloadThreads[i]	= t;
	    int priority	= setDownloadPriority(t);
	    println("\t found thread " + i +
		    " set new one to priority "+priority);
	    t.start();
	 }
      }
   }
   private Thread newDownloadThread(int i)
   {
      return new Thread(toString()+"-download "+i)
	    {
	       public void run()
	       {
		  performDownloads();
	       }
	    };
   }
   private void startDownloadMonitor()
   {
      if (downloadThreads == null)
      {
	 downloadThreads	= new Thread[numDownloadThreads];
	 for (int i=0; i<numDownloadThreads; i++)
	 {
	    Thread thatThread	= newDownloadThread(i);
	    downloadThreads[i]	= thatThread;
	    thatThread.setPriority(LOW_PRIORITY);
	    thatThread.start();
	 }
      }
   }
   void performDownloads()
   {
      while (!finished)
      {
	 DownloadClosure thatClosure;
	 synchronized (toDownload)
	 {
	    if (toDownload.isEmpty())
	       try
	       {
//		  debug("performDownloads() wait() " + Thread.currentThread());
		  toDownload.wait();
	       } catch (InterruptedException e)
	       {
		  // interrupt means stop
	       }
	    if (finished)
	       break;
	    if (toDownload.isEmpty())
	       continue;
	    thatClosure = (DownloadClosure) toDownload.remove(0);
	 }
	 detectPotentialTimeout(thatClosure);
	 try
	 {
//	    debug("performDownload() "+
//		  thatClosure.downloadable+" "+ Thread.currentThread());
	    if (getUrgent)
	       setDownloadPriority(Thread.currentThread());

	    thatClosure.performDownload();
	    potentialTimeouts.remove(thatClosure); 
	    thatClosure.dispatch();
/*	 } catch (ClosedByInterruptException e)
	 {
	    // interrupt means timeout or stop
	    debugA("performDownloads() -- got interrupted.");
*/	 } catch (ThreadDeath e)
	 { 
	    break;
	 } catch (OutOfMemoryError e)
	 { 
	    Memory.recover(e, getClassName() + 
			   ".performDownloads() trying to recover.");
	 } catch (Throwable e)
	 {
	    debugA("performDownloads() -- got exception:");
	    e.printStackTrace();
	    thatClosure.ioError();
	 }
	 Generic.sleep(SHORT_SLEEP);
      }  // while (!finished)
      debug("exiting -- "+Thread.currentThread());
   }
   public String toString()
   {
      return super.toString() + "["+ name + "]";
   }
   public static void notifyAll(Object o)
   {
      synchronized (o)
      {
	 o.notifyAll();
      }
   }
/**
 * Stop our threads.
 */
   public void stop()
   {
//      debug("stop()");
      finished			= true;

      notifyAll(toDispatch);
      notifyAll(potentialTimeouts);
      notifyAll(toDownload);

      timeoutThread		= null;
      dispatchThread		= null;
      if (downloadThreads != null)
      {
	 for (int i=0; i<downloadThreads.length; i++)
	 {
	    downloadThreads[i]	= null;
	 }
	 downloadThreads		= null;
      }
   }
   public int waitingToDownload()
   {
      return toDownload.size();
   }
/**
 * @return	true if we're backed up with unresloved downloads.
 */
   public boolean highNumberWaiting()
   {
      return toDownload.size() > highThreshold;
   }
}
