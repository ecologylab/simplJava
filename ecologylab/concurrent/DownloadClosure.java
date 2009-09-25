package ecologylab.concurrent;

import java.io.IOException;

import ecologylab.generic.Debug;
import ecologylab.generic.DispatchTarget;
import ecologylab.io.Downloadable;

/**
 * Closure that keeps state about a download, as it progresses.
 */
public class DownloadClosure<T extends Downloadable>
extends Debug
{
	T													downloadable;
	
	private DispatchTarget<T>	dispatchTarget;
	private DownloadMonitor								downloadMonitor;
	private Thread												downloadingThread;

	private boolean												dispatched;


	DownloadClosure(T downloadable, DispatchTarget<T> dispatchTarget,
			DownloadMonitor downloadMonitor)
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

	boolean cancel()
	{
		return downloadable.cancel() || downloadable.isRecycled();
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
			downloadable.performDownload();
			downloadable.downloadDone();
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
				dispatchTarget.delivery(downloadable);
		}
	}

	public String toString()
	{
		return super.toString() + "["+downloadable.toString() +" "+
		downloadingThread + "]";
	}

}
