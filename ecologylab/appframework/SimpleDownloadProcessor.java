/**
 * 
 */
package ecologylab.appframework;

import java.io.IOException;

import ecologylab.generic.Debug;
import ecologylab.generic.Continuation;
import ecologylab.io.DownloadProcessor;
import ecologylab.io.Downloadable;

/**
 * A simple download processor: just download the Downloadable immediately.
 * 
 * @author andruid
 */
public class SimpleDownloadProcessor<T extends Downloadable> extends Debug 
implements DownloadProcessor<T>
{
	/**
	 * 
	 */
	public SimpleDownloadProcessor()
	{
		super();
	}

	/*
	 * A no-op to conform to the interface spec. We have no threads to stop :-)
	 */
	public void stop()
	{
	}

	/**
	 * Download it now, in this thread.
	 * 
	 * @param thatDownloadable
	 *          The thing to download.
	 * @param dispatchTarget
	 *          Ignored, since we are not asynchronous, there are no callbacks.
	 */
	// TODO improve error handling here
	public void download(T thatDownloadable, Continuation<T> dispatchTarget)
	{
		try
		{
			thatDownloadable.performDownload();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void requestStop()
	{
		// TODO Auto-generated method stub

	}

}
