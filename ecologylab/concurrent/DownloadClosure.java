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

   private boolean	timeout;
   private boolean	dispatched;
   private boolean	ioError;

   DownloadClosure(Downloadable downloadable, DispatchTarget dispatchTarget)
   {
      this.downloadable		= downloadable;
      this.dispatchTarget	= dispatchTarget;
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
      if (deltaTime >= DownloadMonitor.TIMEOUT)
      {
	 timeout();
	 return true;
      }
      return false;
   }
   synchronized void ioError()
   {
      ioError			= true;
      downloadable.handleIoError();
      dispatch();
   }
   private synchronized void timeout()
   {
      timeout			= true;
      downloadable.handleTimeout();
      dispatch();
   }
   public synchronized void dispatch()
   {
      if (!dispatched)
      {
//	 debug("dispatch()"+" "+downloadable+" -> "+dispatchTarget);
	 dispatched		= true;
	 if (dispatchTarget != null)
	    dispatchTarget.delivery(downloadable);
      }
   }
}
