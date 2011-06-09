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
	
	private Continuation<T>		continuation;
	private DownloadMonitor		downloadMonitor;
	private Thread						downloadingThread;

	private boolean						continued;


	DownloadClosure(T downloadable, Continuation<T> dispatchTarget,DownloadMonitor downloadMonitor)
	{
		this.downloadable			= downloadable;
		this.continuation			= dispatchTarget;
		this.downloadMonitor	= downloadMonitor;
	}

	synchronized void handleIoError()
	{
		downloadable.handleIoError();
		callContinuation();
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
	protected synchronized void callContinuation()
	{
		if (!continued)
		{
			//	 debug("dispatch()"+" "+downloadable+" -> "+dispatchTarget);
			continued		= true;
			downloadMonitor.dispatched++;
			if (continuation != null)
				continuation.callback(downloadable);
		}
	}

	public String toString()
	{
		return super.toString() + "["+downloadable.toString() +" "+
		downloadingThread + "]";
	}

}
