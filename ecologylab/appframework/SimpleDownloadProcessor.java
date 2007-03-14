/**
 * 
 */
package ecologylab.appframework;

import ecologylab.generic.Debug;
import ecologylab.generic.DispatchTarget;
import ecologylab.generic.DownloadProcessor;
import ecologylab.generic.Downloadable;

/**
 * Just download the thing immediately.
 * 
 * @author andruid
 */
public class SimpleDownloadProcessor extends Debug implements DownloadProcessor
{
	/**
	 * 
	 */
	public SimpleDownloadProcessor()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see ecologylab.generic.DownloadProcessor#stop()
	 */
	public void stop()
	{
		// TODO Auto-generated method stub

	}

/**
 * Download it now, in this thread.
 */
	//TODO improve error handling here
	public void download(Downloadable thatDownloadable,
			DispatchTarget dispatchTarget)
	{
		try
		{
			thatDownloadable.performDownload();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
