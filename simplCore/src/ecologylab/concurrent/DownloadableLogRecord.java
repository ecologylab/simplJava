package ecologylab.concurrent;

import java.util.ArrayList;

import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_scalar;

public class DownloadableLogRecord
{
	@simpl_composite
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
