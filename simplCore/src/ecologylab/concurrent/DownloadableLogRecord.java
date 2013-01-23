package ecologylab.concurrent;

import java.util.ArrayList;

import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_scalar;

public class DownloadableLogRecord
{
	@simpl_composite
	ArrayList<Float> queuePeekIntervals = new ArrayList<Float>();
	
	private float enQueueTimestamp;
	
	@simpl_scalar
	boolean htmlCacheHit;
	
	@simpl_scalar
	String urlHash;

	public ArrayList<Float> getQueuePeekIntervals()
	{
		return queuePeekIntervals;
	}

	public void setQueuePeekIntervals(ArrayList<Float> queuePeekIntervals)
	{
		this.queuePeekIntervals = queuePeekIntervals;
	}
	
	public void addQueuePeekInterval(float queuePeekInterval)
	{
		this.queuePeekIntervals.add(queuePeekInterval);
	}

	public float getEnQueueTimestamp()
	{
		return enQueueTimestamp;
	}

	public void setEnQueueTimestamp(float enQueueTimestamp)
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
