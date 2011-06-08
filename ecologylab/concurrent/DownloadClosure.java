package ecologylab.concurrent;

import java.io.IOException;

import ecologylab.generic.Debug;
import ecologylab.generic.Continuation;
import ecologylab.io.BasicSite;
import ecologylab.io.Downloadable;

/**
 * Closure that keeps state about a download, as it progresses.
 */
public class DownloadClosure<T extends Downloadable>
extends Debug
{
	T													downloadable;
	
	private Continuation<T>	dispatchTarget;
	private DownloadMonitor								downloadMonitor;
	private Thread												downloadingThread;

	private boolean												dispatched;


	DownloadClosure(T downloadable, Continuation<T> dispatchTarget,DownloadMonitor downloadMonitor)
	{
		this.downloadable		= downloadable;
		this.dispatchTarget	= dispatchTarget;
		this.downloadMonitor	= downloadMonitor;
	}

	synchronized void ioError()
	{
		downloadable.handleIoError();
		dispatch();
	}

	boolean shouldCancel()
	{
		return downloadable.shouldCancel() || downloadable.isRecycled();
	}

	/**
	 * Do the work to download this.
	 * @throws IOException 
	 * 
	 * @throws Exception
	 */
	void performDownload() throws IOException
	{
		downloadingThread		= Thread.currentThread();
		//TODO need a lock here to prevent recycle() while downloading!!!!!!
		if (!downloadable.isRecycled())
		{
			//Update site statistics if available
			BasicSite site = downloadable.getSite();
			if(site != null)
				site.setLastDownloadAt(System.currentTimeMillis());
			downloadable.performDownload();
			downloadable.downloadAndParseDone();
		}
	}
	public synchronized void dispatch()
	{
		if (!dispatched)
		{
			//	 debug("dispatch()"+" "+downloadable+" -> "+dispatchTarget);
			dispatched		= true;
			downloadMonitor.dispatched++;
			if (dispatchTarget != null)
				dispatchTarget.callback(downloadable);
		}
	}

	public String toString()
	{
		return super.toString() + "["+downloadable.toString() +" "+
		downloadingThread + "]";
	}

}
