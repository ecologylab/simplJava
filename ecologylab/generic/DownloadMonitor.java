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
extends Debug
implements Runnable
{
   static final int	TIMEOUT		= 40000;
   static final int	TIMEOUT_SLEEP	= TIMEOUT / 3;
   static final int	SHORT_SLEEP	= 50;
   static final int	PRIORITY	= 2;
   
   //////////////////// queues for media that gets downloaded /////////////////
/**
 * This is the queue of DownloadClosures waiting to be downloaded.
 */
   Vector		toDownload	= new Vector(30);
/**
 * This is the queue of media that we start to download.
 * This queue drives potential timeout dispatches.
 */
   Vector		downloading	= new Vector(30);
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

   static boolean	finished;
   
   public DownloadMonitor()
   {
      this(1);
   }
   public DownloadMonitor(int numDownloadThreads)
   {
      this.numDownloadThreads	= numDownloadThreads;
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
	    while (!downloading.isEmpty())
	    {
	       DownloadClosure thatClosure	= 
		  (DownloadClosure) downloading.firstElement();
	       if (thatClosure.timeoutOrComplete(now))
		  downloading.remove(0);
	       else
		  break;
	    }
//	    debug("sleeping");
	    Generic.sleep(TIMEOUT_SLEEP);
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
      DownloadClosure closure = new DownloadClosure(thatMedia,  dispatchTarget);
      detectPotentialTimeout(closure);
      return closure;
   }
   protected void detectPotentialTimeout(DownloadClosure closure)
   {
      closure.startingNow();
      downloading.addElement(closure);
      if (timeoutThread == null)
	 startTimeoutMonitor();
   }
   private void startTimeoutMonitor()
   {
      synchronized (downloading)
      {
	 if (timeoutThread == null)
	 {
	    timeoutThread	= new Thread(this, "DownloadMonitor-timeouts");
	    timeoutThread.setPriority(PRIORITY);
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
      synchronized (toDispatch)
      {
//	 debug("dispatch(in synch"+thatDownloadable);
	 toDispatch.addElement(new DownloadClosure(thatDownloadable,
						 dispatchTarget));
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
	 debug("startDispatcHMonitor()");
	 dispatchThread	= new Thread("DownloadMonitor-dispatching")
	 {
	    // !!! if its not in its own thread, the java.awt imaging sys
	    // can get messed up.
	    // dont let imageUpdate threads call the client!
	    public void run()
	    {
	       performDispatches();
	    }
	 };
	 dispatchThread.setPriority(PRIORITY);
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
		  e.printStackTrace();
		  stop();
		  return;	   // interrupt ends us
	       }
	    thatClosure = (DownloadClosure) toDispatch.remove(0);
	 }
	 // must be outside the lock on toDispatch!
	 try
	 {
	    thatClosure.dispatch();
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
						 dispatchTarget));
	 if (downloadThreads == null)
	    startDownloadMonitor();
	 else
	    toDownload.notify();
      }
   }
   private void startDownloadMonitor()
   {
      if (downloadThreads == null)
      {
	 downloadThreads	= new Thread[numDownloadThreads];
	 for (int i=0; i<numDownloadThreads; i++)
	 {
	    Thread thatThread	= new Thread("DownloadMonitor-downloading "+i)
	    {
	       public void run()
	       {
		  performDownloads();
	       }
	    };
	    downloadThreads[i]	= thatThread;
	    thatThread.setPriority(PRIORITY);
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
//		  debug("dispatchDownloads() wait()");
		  toDownload.wait();
	       } catch (InterruptedException e)
	       {
		  e.printStackTrace();
		  stop();
		  return;	   // interrupt ends us
	       }
	    thatClosure = (DownloadClosure) toDispatch.remove(0);
	 }
	 detectPotentialTimeout(thatClosure);
	 try
	 {
	    thatClosure.downloadable.performDownload();
	    thatClosure.dispatch();
	 } catch (Throwable e)
	 {
	    debugA(".dispatch -- got exception:");
	    thatClosure.ioError();
	    e.printStackTrace();
	 }
	 Generic.sleep(SHORT_SLEEP);
      }  // while (!finished)
   }
/**
 * Stop our threads.
 */
   public void stop()
   {
//      debug("stop()");
      finished	= true;
      timeoutThread	= null;
      dispatchThread	= null;
   }
}
