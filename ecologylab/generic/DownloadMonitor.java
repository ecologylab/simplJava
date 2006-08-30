package ecologylab.generic;


import java.util.*;
import java.nio.channels.ClosedByInterruptException;


/**
 * Non-linear flow multiplexor.
 * Tracks downloads of <code>Downloadable</code> objects.
 * Dispatches downloaded media to the appropriate <code>DispatchTarget</code>.
 * <p>
 * Looks out for timeout conditions. In case they happen, records
 * state in the <code>bad</code> slot, and dispatches, as well.
 */
public class DownloadMonitor
extends Monitor
implements Runnable
{
   static final int	TIMEOUT		= 25000;
   static final int	TIMEOUT_SLEEP	= 4000;
   //static final int	SHORT_SLEEP	= 50;
   static final int	SHORT_SLEEP	= 100;

   public static final int TIMEOUT_PRIORITY	= 4;
   public static final int HIGHER_PRIORITY	= 4;
   public static final int HIGH_PRIORITY	= 3;
   public static final int MID_PRIORITY		= 2;
   public static final int LOW_PRIORITY		= 1;
   
   int			lowPriority;
   int			midPriority;
   int			highPriority;

   public final int	highThreshold;
   public final int	midThreshold;
   
   int			timeouts;
   int			dispatched;
   int			pending;
   
   boolean		paused;

   //////////////////// queues for media that gets downloaded /////////////////
/**
 * This is the queue of DownloadClosures waiting to be downloaded.
 */
   Vector		toDownload	= new Vector(30);
/**
 * This is the queue of media that we start to download.
 * This queue drives potential timeout dispatches.
 */
   private Vector	potentialTimeouts	= new Vector(30);
/**
 * This is the queue of media that finish downloading.
 * This queue drives normal dispatch to <code>DispatchTarget</code>s, when
 * media arrives properly.
 */
   Vector		toDispatch	= new Vector(30);

   static HashMap	stemmersHash	= new HashMap();
   
   Thread		timeoutThread;
   Thread		dispatchThread	= null;
   Thread[]		downloadThreads;
   int[]		priorities;
   
   int			numDownloadThreads;
   String		name;
   
   static boolean	finished;
   
   static 		ThreadGroup	THREAD_GROUP	= new ThreadGroup("DownloadMonitor");
   
   FloatWeightSet		sourceSet;

   public DownloadMonitor(String name, int numDownloadThreads)
   {
      this(name, numDownloadThreads, 0);
   }
   public DownloadMonitor(String name, int numDownloadThreads, 
			  int priorityBoost)
   {
      this.numDownloadThreads	= numDownloadThreads;
      this.name			= name;
      
      highThreshold		= numDownloadThreads * 2;
      midThreshold		= numDownloadThreads + 1;
      finished			= false;

      lowPriority		= LOW_PRIORITY + priorityBoost;
      midPriority		= MID_PRIORITY + priorityBoost;
      highPriority		= HIGH_PRIORITY + priorityBoost;
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
			   //debug("checking() "+thatClosure);
			   
			   if (thatClosure.timeoutOrComplete(now)) // does Downloable work
			   {
				  if (potentialTimeouts.removeElement(thatClosure))
				  {
					 if (thatClosure.timedOut() && 
						 !thatClosure.timeoutResolved)
					 {
						println("\n\n");
						debug("restarting timeout thread for " + thatClosure);
						// hopefully we dont need this because its awkward
						kickDownloadThread(thatClosure.downloadingThread);
					 }
				  }
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
			//debug(1, "detectTimeouts() sleeping for " + sleep);
			Generic.sleep(sleep);
		 } catch (OutOfMemoryError e)
		 {
		 	finished		= true;
		 	OutOfMemoryErrorHandler.handleException(e);
		 	/*
		 	if (Memory.outOfMemory(e))
			   finished		= true;
			*/
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
		 finished		= false;
		 if (timeoutThread == null)
		 {
			timeoutThread	= new Thread(THREAD_GROUP, this, toString() + "-timeouts");
			timeoutThread.setPriority(TIMEOUT_PRIORITY);
			timeoutThread.start(); // to our run method
		 }
      }
   }

   //----------------------- perform dispatches -----------------------------//
/**
 * Entry point for siphon threads. We will queue a DispatchTarget
 * for dispatching by our dispatch thread, when we are ready.
 * Starts the dispatching thread, if necessary.
 * THIS CODE IS NOT USED!
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
   /**
    * 
    * THIS CODE IS NOT USED!
    */
   private void startDispatchMonitor()
   {
	  //      finished		= false;
      
      if (dispatchThread == null)
      {
		 //	 debug("startDispatcHMonitor()");
		 finished		= false;
		 dispatchThread	= new Thread(THREAD_GROUP, toString() + "-dispatching")
		 {
			// !!! if its not in its own thread, the java.awt imaging sys
			// can get messed up.
			// dont let imageUpdate threads call the client!
			public void run()
			{
			   performDispatches();
			}
		 };
		 dispatchThread.setPriority(lowPriority);
		 dispatchThread.setPriority(lowPriority);
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
			if (paused)
			   wait(toDispatch);
			if (toDispatch.isEmpty())
			   wait(toDispatch);
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
			if (Memory.outOfMemory(e))
			   finished		= true;
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
		 //	 debug("download("+thatDownloadable);
		 toDownload.addElement(new DownloadClosure(thatDownloadable,
												   dispatchTarget, this));
		 if (downloadThreads == null)
			startPerformDownloadsThreads();
		 else
			toDownload.notify();
      }
   }
   /**
    * Set the priority of the download thread.
    * The more backed up we are, the higher the priority.
    * 
    * @param t
    * @return
    */
   private int setDownloadPriority()
   {
	   return setDownloadPriority(Thread.currentThread());
   }
   /**
    * Set the priority of the download thread.
    * The more backed up we are, the higher the priority.
    * 
    * @param t
    * @return
    */
   private int setDownloadPriority(Thread t)
   {
      int waiting	= toDownload.size();
      int priority;

      if (waiting >= midThreshold)
		 priority	= midPriority;
      else if (waiting >= highThreshold)
		 priority	= midPriority;
      else
		 priority	= lowPriority;

      Generic.setPriority(t, priority);

      return priority;
   }
   private void kickDownloadThread(Thread t)
   {
	   debug("kickDownloadThread("+t+") - no-op");
/* put this back?! after fixing race condition. It is incompatible
   with ThreadStateDebugger
      if (t != null)
	 t.interrupt();
 */
/*
      Thread	oldThread	= t;
      for (int i=0; i<downloadThreads.length; i++)
      {
	 if (downloadThreads[i] == t)
	 {
	    try
	    {
	       println("\t stopping " + t);
	       t.stop();
	       t.stop();
	       if (t.isAlive())
		  println("\tSTOP failed!");

	       t		= newDownloadThread(i, "restarted");
	       downloadThreads[i]	= t;
	       int priority	= setDownloadPriority(t);
	       println("\tSetting new thread to priority "+priority);
	       t.start();
	       for (int j=0; j< 10; j++)
	       {
	          oldThread.stop();
	       }
	    } catch (ThreadDeath e)
	    {
	       println("\tThreadDeath while restarting stuck thread!");
	       e.printStackTrace();
	       throw e;
	    }
	    catch (Throwable e)
	    {
	       println("\tEXCEPTION while restarting stuck thread!");
	       e.printStackTrace();
	    }
	 }
      }
 */
   }
   /**
    * Create a new Thread that runs performDownloads().
    * 
    * @param i
    * @return
    */
   protected Thread newPerformDownloadsThread(int i)
   {
      return newPerformDownloadsThread(i, "");
   }
   /**
    * Create a new Thread that runs performDownloads().
    * 
    * @param i
    * @param s
    * @return
    */
   protected Thread newPerformDownloadsThread(int i, String s)
   {
	  return new Thread(THREAD_GROUP, toString()+"-download "+i+" "+s)
	  {
		 public void run()
		 {			  	
			performDownloads();
		 }
	  };
   }
/**
 * Creates and starts up our performDownloads() Threads.
 *
 */
   private void startPerformDownloadsThreads()
   {
      if (downloadThreads == null)
      {
		 finished			= false;
		 downloadThreads	= new Thread[numDownloadThreads];
		 priorities			= new int[numDownloadThreads];
		 for (int i=0; i<numDownloadThreads; i++)
		 {
			Thread thatThread	= newPerformDownloadsThread(i);
			downloadThreads[i]	= thatThread;
			thatThread.setPriority(lowPriority);
			priorities[i]		= lowPriority;
			ThreadDebugger.registerMyself(thatThread);
			thatThread.start();
		 }
      }
   }
   public void pause()
   {
      pause(true);
   }
   public void unpause()
   {
      pause(false);
      notifyAll(toDownload);
   }
   public void pause(boolean paused)
   {
      synchronized (toDownload)
      {
		 synchronized (toDispatch)
		 {
			debug(4, "pause("+paused);
			this.paused	= paused;

			int[] priorities		= this.priorities; // avoid race
			if (paused)
			{
			   if (downloadThreads != null)
			   {
				  for (int i=0; i<numDownloadThreads; i++)
				  {
					 Thread thatThread	= downloadThreads[i];
					 if (thatThread != null)
					 {
						int thatPriority	= thatThread.getPriority();
						priorities[i]		= thatPriority;
						if (Thread.MIN_PRIORITY < thatPriority)
							thatThread.setPriority(Thread.MIN_PRIORITY);
					 }
				  }
			   }
			}
			else
			{
			   if (downloadThreads != null)
			   {
				  for (int i=0; i<numDownloadThreads; i++)
				  {
					 // restore priorities
					 Thread t	= downloadThreads[i];
					 if ((t != null) && (priorities != null))
					 {
						debug("restore priority to " + priorities[i]);
						int thatPriority	= priorities[i];
						if (thatPriority <= 0)
							thatPriority	= 1;
						t.setPriority(thatPriority);
					 }
				  }
			   }
			}
		 }
      }
   }
   /**
    * The heart of the workhorse Threads.
    * It loops, pulling a DownloadClosure off the toDownload queue,
    * calling its performDownload() method, and then
    * calling dispatch() if there is a dispatchTarget.
    * 
    *
    */
   void performDownloads()
   {
      Thread downloadThread = Thread.currentThread();

      while (!finished)
      {

		 DownloadClosure thatClosure;
		 synchronized (toDownload)
		 {
			if (paused)
			   wait(toDownload);
			if (toDownload.isEmpty())
			   wait(toDownload);
			if (finished)
			   break;
			if (toDownload.isEmpty())
			   continue;
			thatClosure = (DownloadClosure) toDownload.remove(0);
		 }
		 detectPotentialTimeout(thatClosure, Thread.currentThread());
		 try
		 {
			pending++;
			ThreadDebugger.waitIfPaused(downloadThread);
			// NEW -- set the priority of the download, based on how backed up we are
			setDownloadPriority();
			
			thatClosure.performDownload();
			//	    debug("after performDownload() " + thatClosure);
			potentialTimeouts.remove(thatClosure); 
			thatClosure.dispatch();

		 } catch (ThreadDeath e)
		 { 
			debug("ThreadDeath in performDownloads() loop");
			e.printStackTrace();
			throw e;
		 } catch (ClosedByInterruptException e)
		 { 
			debug("Recovering from ClosedByInterruptException in performDownloads() loop.");
			e.printStackTrace();
			thatClosure.ioError();

		 } catch (OutOfMemoryError e)
		 { 
		 	finished		= true;	// give up!
		 	OutOfMemoryErrorHandler.handleException(e);
		 	/*
		 	if (Memory.outOfMemory(e))
			   finished		= true;	// give up!
			*/
			//else
			   //thatClosure.ioError();
		 } catch (Throwable e)
		 {
			boolean interrupted		= Thread.interrupted();
			String interruptedStr	= interrupted ? " interrupted" : "";
			debugA("performDownloads() -- recovering from "+interruptedStr+
				   " exception on " + thatClosure + ":");
			e.printStackTrace();
			thatClosure.ioError();
		 }
		 finally
		 {
			pending--;
		 }
		 if (hurry)
			Generic.sleep(SHORT_SLEEP);
		 else
			Generic.sleep(400 + MathTools.random(100));
      }  // while (!finished)
      debug("exiting -- "+Thread.currentThread());
   }
   public String toString()
   {
      return super.toString() + "["+ name + "]";
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
   public boolean midNumberWaiting()
   {
      return toDownload.size() > midThreshold;
   }
   public void setSourceSet(FloatWeightSet sourceSet)
   {
      this.sourceSet		= sourceSet;
   }
   public int lowPriority()
   {
      return lowPriority;
   }
   public int midPriority()
   {
      return midPriority;
   }
   public int highPriority()
   {
      return highPriority;
   }
   public int pending()
   {
      return pending;
   }

   boolean hurry;
   public void setHurry(boolean hurry)
   {
      this.hurry	= hurry;
      debug("setHurry("+hurry);
   }

   public static NewPorterStemmer getStemmer()
   {
      Thread currentThread		= Thread.currentThread();
      NewPorterStemmer stemmer		= 
		 (NewPorterStemmer) stemmersHash.get(currentThread);
      if (stemmer == null)
      {
		 stemmer			= new NewPorterStemmer();
		 stemmersHash.put(currentThread, stemmer);
      }
      return stemmer;
   }
}
