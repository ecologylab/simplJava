/**
 * 
 */
package ecologylab.concurrent;

import java.util.Random;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;
import ecologylab.serialization.types.element.IMappable;

/**
 * 
 *
 * @author andruid 
 */

@simpl_tag("site")
public 
class BasicSite extends ElementState implements IMappable<String>, Site
{
	
	static Random random = new Random(System.currentTimeMillis());
	
	@simpl_scalar protected String	domain;

	@simpl_scalar
  int															numTimeouts;

	@simpl_scalar
  int															numFileNotFounds;

	@simpl_scalar
  int															numOtherIoErrors;

	@simpl_scalar
  int															numNormalDownloads;

	protected int										downloadsQueuedOrInProgress;

  boolean													ignore;
  
  long														nextAvailableTime;
  
	static final int							MAX_TIMEOUTS	= 6;

  /**
   * Minimum time to wait between downloads for this domain
   * Specified in seconds
   */
  @simpl_scalar protected float							minDownloadInterval;
  
    
  /**
   * Timestamp of last download from this site;
   */
  long														lastDownloadAt;
  
  @simpl_scalar protected boolean ignoreSemanticBoost;
  
  boolean													isDownloading;
  
	/**
	 * Use for XML Translation
	 */
	public BasicSite()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
  public void countTimeout(ParsedURL location)
	{
		numTimeouts++;
		warning("Timeout " + numTimeouts + "\t" + location);
	}
	
	@Override
  public void countFileNotFound(ParsedURL location)
	{
		numFileNotFounds++;
		warning("FileNotFound " + numFileNotFounds + "\t" + location);
	}
	
	@Override
  public void countOtherIoError(ParsedURL location)
	{
		numOtherIoErrors++;
		warning("Other IO Error " + numOtherIoErrors + "\t" + location);
	}
	
	@Override
  public void countNormalDownload(ParsedURL location)
	{
		numNormalDownloads++;
	}
	
	@Override
  public synchronized void queueDownload(ParsedURL location)
	{
		downloadsQueuedOrInProgress++;
	}

	@Override
  public synchronized void endDownload(ParsedURL location)
	{
	  debug("Ending downloading for site");
		isDownloading	= false;
		downloadsQueuedOrInProgress--;
	}

  public boolean tooManyTimeouts()
	{
		return numTimeouts >= MAX_TIMEOUTS;
	}
	
	@Override
  public boolean isDown()
	{
		boolean result = tooManyTimeouts();
		if (result)
			warning("Cancelling because " + numTimeouts + " timeouts");
		return result;
	}
	
	/**
	 *	
	 * @return (numTimeouts == 0) ? 1 : 1.0 / (numTimeouts + 1);
	 */
  public double timeoutsFactor()
	{
		return (numTimeouts == 0) ? 1 : 1.0 / (numTimeouts + 1);
	}

	/**
	 * @return the minDownloadInterval
	 */
  public float getMinDownloadInterval()
	{
		return minDownloadInterval;
	}

	/**
	 * @param minDownloadInterval the minDownloadInterval to set
	 */
  public void setMinDownloadInterval(float minDownloadInterval)
	{
		this.minDownloadInterval = minDownloadInterval;
	}

	/**
	 * @return the lastDownloadAt
	 */
	@Override
  public long getLastDownloadAt()
	{
		return lastDownloadAt;
	}

	/**
	 * Register that we are down loading, and the current time as when the down load started.
	 */
	@Override
  public void beginDownload(ParsedURL location)
	{
		this.isDownloading	= true;
		this.lastDownloadAt = System.currentTimeMillis();
	}

  @Override
  public long getDownloadInterval()
  {
    return (long) (minDownloadInterval * 1000);
  }

	/**
	 * Swing delays between downloads for a site by a large margin. 
	 * @return time in millis 
	 */
	@Override
  public long getDecentDownloadInterval()
	{
		int millis = (int) (minDownloadInterval * 1000);
		return millis + random.nextInt(millis / 2);
	}
	@Override
  public boolean isDownloadingConstrained()
	{
		return minDownloadInterval > 0;
	}
	@Override
	public String key()
	{
		return domain;
	}

	/**
	 * 
	 * 
	 * @return the isDownloading
	 */
	@Override
  public boolean isDownloading()
	{
		return isDownloading;
	}
	
	/**
	 * Call this when a download to a site was actually fulfilled by a local copy.
	 */
  public void resetLastDownloadAt()
	{
		this.lastDownloadAt	= 0;
	}

	@Override
  public String domain()
	{
		return domain;
	}
 
	public boolean ignoreSemanticBoost()
	{
		return ignoreSemanticBoost;
	}

	@Override
  public boolean isIgnored()
	{
		return ignore;
	}

	/**
	 * Recycle and Mark this site as recycled.
	 * 
	 */
	@Override
  public void setIgnored(boolean ignore)
	{
		this.ignore = ignore;
	}
	
  @Override
  public long getNextAvailableTime()
	{
		return nextAvailableTime;
	}

  @Override
	public void advanceNextAvailableTime()
	{
		this.nextAvailableTime = System.currentTimeMillis() + getDecentDownloadInterval();
	}

	private static final int	TWELVE_HOURS_IN_MILLIS	= 1000*60*60*12;

	@Override
  public void setAbnormallyLongNextAvailableTime()
	{
		this.nextAvailableTime	= System.currentTimeMillis() + TWELVE_HOURS_IN_MILLIS;
	}

  public int numOfNormalDownloads()
  {
    return numNormalDownloads;
  }

  public int numOfTimeouts()
  {
    return numTimeouts;
  }

  public int numOfNotFounds()
  {
    return numFileNotFounds;
  }

  public int numOfOtherIoError()
  {
    return numOtherIoErrors;
  }

}
