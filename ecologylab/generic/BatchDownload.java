package ecologylab.generic;


import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Observable;

public class BatchDownload extends Observable 
implements DispatchTarget
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
	final int totalNumDownloads;
	
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
    
	
    /**
     * @param batchName     The name that identifies this batch of downloads. Used
     *                      to name the DownloadMonitor.
     * @param downloadables    A HashMap of identifier keys mapped to Downloadables for download
     */
	public BatchDownload(String batchName, HashMap downloadables, int numDownloadThreads)
	{
		totalNumDownloads         = downloadables.size();
		finalDownloadables        = new Hashtable(totalNumDownloads);
        invertedIndexDownloadables= new HashMap(totalNumDownloads);
        this.batchName            = batchName;
        this.downloadables        = downloadables;
        this.numDownloadThreads   = numDownloadThreads;
        
        //build the downloadables inverted index for fast lookup based on
        //the Downloadable object.
        Iterator it               = downloadables.keySet().iterator();
        while (it.hasNext())
        {
            Object key            = it.next();
            Object value          = downloadables.get(key);
            invertedIndexDownloadables.put(value, key);
        }
        
        downloadMonitor = new DownloadMonitor(batchName, numDownloadThreads);
	}
	
	/**
	 * Begin downloading each page and retrieving it's html code.
	 */
	public void startDownloads()
	{
		Iterator urlIterator = downloadables.keySet().iterator();
        while (urlIterator.hasNext())
        {
            Downloadable downloadable = (Downloadable) downloadables.get(urlIterator.next());
            downloadMonitor.download(downloadable, this);
        }
	}

	public void delivery(Object o)
	{
        if (o == null)
        {
            notifyObserversIfFinished();
            numberDownloadsFailed++;
            return;
        }
        
        numberDownloadsSuccessful++;
        
        Downloadable downloadable = (Downloadable) o;
        if (downloadable == null)
        {
            System.err.println("Null download delivery!!");
            return;
        }
        
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
     */
    public void freeResources()
    {
        finalDownloadables.clear();
        finalDownloadables          = null;
        
        downloadables.clear();
        downloadables               = null;
        
        invertedIndexDownloadables.clear();
        invertedIndexDownloadables  = null;
        
        downloadMonitor.stop();
        downloadMonitor             = null;
    }
    
}
