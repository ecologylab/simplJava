package ecologylab.concurrent;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Observable;

import ecologylab.generic.DispatchTarget;
import ecologylab.io.Downloadable;

public class BatchDownload extends Observable implements DispatchTarget
{
	/**
	 * Where the Downloadables are placed after download.
	 * The key can be arbitrary and is not used during download. It IS
	 * respected and will be maintained across downloads for later
	 * identification.
	 */
	Hashtable finalDownloadables;

	/**
	 * The number of total downloads that SHOULD be performed.
	 */
	int totalNumDownloads;

	/**
	 * The number of downloads that have COMPLETED.
	 */
	int numberDownloadsSuccessful = 0;

	/**
	 * The number of downloads that failed and will NOT be attempted again.
	 */
	int numberDownloadsFailed = 0;

	DownloadMonitor downloadMonitor;

	String batchName = "Batch Downloader";

	/**
	 * Number of threads to use for downloading these Downloadables
	 */
	int numDownloadThreads;

	/**
	 * HashMap of downloadables keyed with an arbitrary index.
	 */
	HashMap downloadables;

	/**
	 * An inverted index of downloadables used to map the delivered Downloadable object
	 * to its key when building the finalDownloadables Hashtable.
	 */
	HashMap invertedIndexDownloadables;
	
	boolean firstRun = true;

	/**
	 * @param batchName     The name that identifies this batch of downloads. Used
	 *                      to name the DownloadMonitor.
	 * @param numDownloadThreads	The number of threads to use for simultaneous downloads.
	 */
	public BatchDownload(String batchName, int numDownloadThreads)
	{
		this.batchName = batchName;
		this.numDownloadThreads = numDownloadThreads;

		downloadMonitor = new DownloadMonitor(batchName, numDownloadThreads);
	}

	/**
	 * Set the hash map of downloadables that will be downloaded on start.
	 * 
	 * @param downloadables    A HashMap of identifier keys mapped to Downloadables for download
	 */
	public void setDownloads(HashMap downloadables)
	{
		//free previous resources (if any), but don't kill the download monitor.
		freeResources(false);
		
		numberDownloadsFailed			= 0;
		numberDownloadsSuccessful		= 0;

		this.downloadables 			= new HashMap(downloadables);
		totalNumDownloads 			= downloadables.size();
		finalDownloadables 			= new Hashtable(totalNumDownloads);
		invertedIndexDownloadables	= new HashMap(totalNumDownloads);

		//		build the downloadables inverted index for fast lookup based on
		//the Downloadable object.
		Iterator it = downloadables.keySet().iterator();
		while (it.hasNext())
		{
			Object key = it.next();
			Object value = downloadables.get(key);
			invertedIndexDownloadables.put(value, key);
		}
	}

	/**
	 * Begin downloading each page and retrieving it's content.
	 */
	public void startDownloads()
	{
		Iterator urlIterator = downloadables.keySet().iterator();
		while (urlIterator.hasNext())
		{
			Downloadable downloadable = (Downloadable) downloadables
					.get(urlIterator.next());
			downloadMonitor.download(downloadable, this);
		}
		
		/*
		if (!firstRun)
		{
			System.out.println("starting downloads manually!");
			downloadMonitor.performDownloads();
			System.out.println("finished blocking at performDownloads()");
		}
		firstRun = false;
		*/
	}

	public void delivery(Object o)
	{
		if (o == null)
		{
			System.err.println("Null download delivery");
			notifyObserversIfFinished();
			numberDownloadsFailed++;
			return;
		}

		numberDownloadsSuccessful++;

		Downloadable downloadable = (Downloadable) o;
		Object originalKey = invertedIndexDownloadables.get(downloadable);

		finalDownloadables.put(originalKey, downloadable);
		notifyObserversIfFinished();
	}

	public boolean isDone()
	{
		return ((numberDownloadsFailed + numberDownloadsSuccessful) == totalNumDownloads);
	}

	protected void notifyObserversIfFinished()
	{
		if (isDone())
		{
			setChanged();
			notifyObservers();
		}
	}

	public Hashtable getDownloads()
	{
		return finalDownloadables;
	}

	/**
	 * Free any resources associated with instantiating and using this object.
	 * This is only necessary to attempt to minimize how much time elapses
	 * before this object is garbage collected after use and nullification.
	 * 
	 * @param freeDownloadMonitor	Whether or not to stop and clear the download monitor.
	 */
	protected void freeResources(boolean freeDownloadMonitor)
	{
		if (finalDownloadables != null)
		{
			finalDownloadables.clear();
			finalDownloadables = null;
		}

		if (downloadables != null)
		{
			downloadables.clear();
			downloadables = null;
		}

		if (invertedIndexDownloadables != null)
		{
			invertedIndexDownloadables.clear();
			invertedIndexDownloadables = null;
		}

		if (freeDownloadMonitor && downloadMonitor != null)
		{
			downloadMonitor.stop();
			downloadMonitor = null;
		}
	}

	public void freeResources()
	{
		freeResources(true);
	}

}
