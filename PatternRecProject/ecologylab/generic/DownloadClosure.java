package cm.generic;

/**
 * Closure that keeps state about a download, as it progresses.
 */
public class DownloadClosure
extends Debug
{
   Downloadable		downloadable;
   long			startTime;
   DispatchTarget	dispatchTarget;
   DownloadMonitor	downloadMonitor;
   
   Thread		downloadingThread;

   boolean		timeoutResolved;
   
   private boolean	timeout;
   private boolean	dispatched;
   private boolean	ioError;

   DownloadClosure(Downloadable downloadable, DispatchTarget dispatchTarget,
		   DownloadMonitor downloadMonitor)
   {
      this.downloadable		= downloadable;
      this.dispatchTarget	= dispatchTarget;
      this.downloadMonitor	= downloadMonitor;
   }
   void startingNow()
   {
      startTime			= System.currentTimeMillis();
   }
/**
 * @return	true if complete or timeout. false if still pending.
 */
   boolean timeoutOrComplete(long now)
   {
      long  deltaTime	= now - startTime;
      if (downloadable.isDownloadDone())
	 return true;
//      debug("deltaTime="+deltaTime);
      if (deltaTime >= DownloadMonitor.TIMEOUT)
      {
	 timeout();
	 return true;
      }
      return false;
   }
   int deltaTime(long now)
   {
      return (int) (now - startTime);
   }
   synchronized void ioError()
   {
      ioError			= true;
      downloadable.handleIoError();
      dispatch();
   }
   private synchronized void timeout()
   {
      downloadMonitor.timeouts++;
      timeout			= true;
      timeoutResolved		= downloadable.handleTimeout();
      dispatch();
   }
   void performDownload()
      throws Exception
   {
      downloadingThread		= Thread.currentThread();
      System.out.println("preforming download on " + downloadable.getClass().getName());
      downloadable.performDownload();
   }
   public synchronized void dispatch()
   {
      if (!dispatched)
      {
//	 debug("dispatch()"+" "+downloadable+" -> "+dispatchTarget);
	 dispatched		= true;
	 downloadMonitor.dispatched++;
	 if (dispatchTarget != null)
	    dispatchTarget.delivery(downloadable);
      }
   }
   public boolean timedOut()
   {
      return timeout;
   }
   public String toString()
   {
      return super.toString() + "["+downloadable.toString() +" "+
	 downloadingThread + "]";
   }
}
