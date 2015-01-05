/**
 * 
 */
package ecologylab.appframework;

import java.io.IOException;

import ecologylab.concurrent.Downloadable;
import ecologylab.generic.Continuation;
import ecologylab.generic.Debug;
import ecologylab.io.DownloadProcessor;

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
	@Override
	public void stop()
	{
	}

	/**
	 * Download it now, in this thread.
	 * 
	 * @param downloadable
	 *          The thing to download.
	 * @param dispatchTarget
	 *          Ignored, since we are not asynchronous, there are no callbacks.
	 */
	// TODO improve error handling here
	@Override
	public void download(T downloadable, Continuation<T> continuation)
	{
		try
		{
			downloadable.performDownload();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			downloadable.handleIoError(e);
		}
		finally
		{
			if (continuation != null)
				continuation.callback(downloadable);
		}
	}

	@Override
	public void requestStop()
	{
		// TODO Auto-generated method stub

	}

}
