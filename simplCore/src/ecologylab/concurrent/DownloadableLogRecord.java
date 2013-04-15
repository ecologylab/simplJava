package ecologylab.concurrent;

import java.util.ArrayList;

import simpl.annotations.dbal.simpl_collection;
import simpl.annotations.dbal.simpl_composite;
import simpl.annotations.dbal.simpl_scalar;


public class DownloadableLogRecord
{
	@simpl_collection("peek_interval")
	ArrayList<Long> queuePeekIntervals = new ArrayList<Long>();
	
	private long enQueueTimestamp;
	
	@simpl_scalar
	boolean htmlCacheHit;
	
	@simpl_scalar
	String urlHash;

	public void addQueuePeekInterval(long queuePeekInterval)
	{
		this.queuePeekIntervals.add(queuePeekInterval);
	}

	public ArrayList<Long> getQueuePeekIntervals()
	{
		return queuePeekIntervals;
	}

	public void setQueuePeekIntervals(ArrayList<Long> queuePeekIntervals)
	{
		this.queuePeekIntervals = queuePeekIntervals;
	}

	public long getEnQueueTimestamp()
	{
		return enQueueTimestamp;
	}

	public void setEnQueueTimestamp(long enQueueTimestamp)
	{
		this.enQueueTimestamp = enQueueTimestamp;
	}

	public boolean isHtmlCacheHit()
	{
		return htmlCacheHit;
	}

	public void setHtmlCacheHit(boolean bHTMLCacheHit)
	{
		this.htmlCacheHit = bHTMLCacheHit;
	}

	public String getUrlHash()
	{
		return urlHash;
	}

	public void setUrlHash(String urlHash)
	{
		this.urlHash = urlHash;
	}
}
