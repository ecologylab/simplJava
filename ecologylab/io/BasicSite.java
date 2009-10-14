/**
 * 
 */
package ecologylab.io;

import ecologylab.xml.ElementState;
import ecologylab.xml.ElementState.xml_tag;

/**
 * 
 *
 * @author andruid 
 */

@xml_tag("site")
public 
class BasicSite extends ElementState
{
	@xml_attribute protected String	domain;

  int															numTimeouts;

  static final int								MAX_TIMEOUTS	= 3;

  @xml_attribute int							minDownloadInterval;
  
  /**
   * Timestamp of last download from this site;
   */
  long										lastDownloadAt;
  
	/**
	 * 
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
		this.lastDownloadAt = lastDownloadAt;
	}
	
	
}
