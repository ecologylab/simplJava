/**
 * 
 */
package ecologylab.io;

import java.util.Random;

import ecologylab.xml.ElementState;
import ecologylab.xml.ElementState.xml_tag;
import ecologylab.xml.types.element.Mappable;

/**
 * 
 *
 * @author andruid 
 */

@xml_tag("site")
public 
class BasicSite extends ElementState implements Mappable<String>
{
	
	static Random random = new Random(System.currentTimeMillis());
	
	@simpl_scalar protected String	domain;

  int															numTimeouts;

  static final int							MAX_TIMEOUTS	= 3;

  /**
   * Minimum time to wait between downloads for this domain
   * Specified in seconds
   */
  @simpl_scalar protected int							minDownloadInterval;
  
  /**
   * Timestamp of last download from this site;
   */
  long														lastDownloadAt;
  
  boolean													isDownloading;
  
	/**
	 * Use for XML Translation
	 */
	public BasicSite()
	{
		// TODO Auto-generated constructor stub
	}

	public void incrementNumTimeouts()
	{
		numTimeouts++;
		warning("Timeout " + numTimeouts);
	}
	
	public boolean tooManyTimeouts()
	{
		return numTimeouts >= MAX_TIMEOUTS;
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
	public int getMinDownloadInterval()
	{
		return minDownloadInterval;
	}

	/**
	 * @param minDownloadInterval the minDownloadInterval to set
	 */
	public void setMinDownloadInterval(int minDownloadInterval)
	{
		this.minDownloadInterval = minDownloadInterval;
	}

	/**
	 * @return the lastDownloadAt
	 */
	public long getLastDownloadAt()
	{
		return lastDownloadAt;
	}

	/**
	 * @param lastDownloadAt the lastDownloadAt to set
	 */
	public void setLastDownloadAt(long lastDownloadAt)
	{
		this.isDownloading	= true;
		this.lastDownloadAt = lastDownloadAt;
	}
	
	/**
	 * Swing delays between downloads for a site by a large margin. 
	 * @return time in millis 
	 */
	public long getDecentDownloadInterval()
	{
		return minDownloadInterval * 1000 + random.nextInt((minDownloadInterval * 1000)/ 2);
	}
	public boolean constrainDownloadInterval()
	{
		return minDownloadInterval > 0;
	}
	public String key()
	{
		return domain;
	}

	/**
	 * 
	 * 
	 * @return the isDownloading
	 */
	public boolean isDownloading()
	{
		return isDownloading;
	}
	
	public void endDownloading()
	{
		isDownloading	= false;
	}
	
	/**
	 * Call this when a download to a site was actually fulfilled by a local copy.
	 */
	public void resetLastDownloadAt()
	{
		this.lastDownloadAt	= 0;
	}

	public String domain()
	{
		return domain;
	}
}
