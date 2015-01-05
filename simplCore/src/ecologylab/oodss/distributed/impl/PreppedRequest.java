/*
 * Created on Apr 4, 2007
 */
package ecologylab.oodss.distributed.impl;

import ecologylab.generic.Debug;

/**
 * Represents a RequestMessage that has been translated to XML. This object encapsulates the XML
 * String, along with the request's UID.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public class PreppedRequest extends Debug implements Comparable<PreppedRequest>
{
	private long					uid	= -1;

	private StringBuilder	request;

	private boolean				disposable;

	/**
	 * 
	 */
	public PreppedRequest(StringBuilder request, long uid, int requestSize, boolean disposable)
	{
		this(requestSize);

		this.uid = uid;

		this.setRequest(request);

		this.disposable = disposable;
	}

	public PreppedRequest(int requestSize)
	{
		this.request = new StringBuilder(requestSize);
	}

	/**
	 * Resets this PreppedRequest for re-use.
	 */
	public void clear()
	{
		this.uid = -1;
		request.setLength(0);
		this.disposable = false;
	}

	/**
	 * @return the request
	 */
	public StringBuilder getRequest()
	{
		return request;
	}

	public void setRequest(StringBuilder request)
	{
		this.request.setLength(0);
		this.request.append(request);

//		debug(request.toString());
	}

	/**
	 * @return the uid
	 */
	public long getUid()
	{
		return uid;
	}

	@Override
	public int compareTo(PreppedRequest arg0)
	{
		return (int) (this.uid - arg0.getUid());
	}

	/**
	 * @param uid
	 *          the uid to set
	 */
	public void setUid(long uid)
	{
		this.uid = uid;
	}

	/**
	 * @return the disposable
	 */
	public boolean isDisposable()
	{
		return disposable;
	}

	/**
	 * @param disposable
	 *          the disposable to set
	 */
	public void setDisposable(boolean disposable)
	{
		this.disposable = disposable;
	}
}
