/**
 * 
 */
package ecologylab.io;

import ecologylab.generic.Debug;

/**
 * 
 *
 * @author andruid 
 */
public class BasicSite extends Debug
{
  protected String				domain;

  /**
   * counts of index pages and content pages
   * initialized to 1 so indexContentRatio is always well defined.
   */
  private int							indexPages = 1;
  private int							contentPages = 1;

  int											numTimeouts;

  static final int				MAX_TIMEOUTS	= 3;


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
	
	
	public double timeoutsFactor()
	{
		return (numTimeouts == 0) ? 1 : 1 / (numTimeouts + 1);
	}

	/**
	 * Adds 1 to the index page count
	 */
	public void newIndexPage()
	{
		indexPages++;
	}
	
	/**
	 * Adds 1 to the index page count
	 */
	public void newContentPage()
	{
		contentPages++;
	}
	
	/**
	 * Make crawler work better by taking into account our efficiency in retrieving documents and extracting content from this site.
	 * 
	 * @return	0 < weightingFactor <= 1
	 */
	public double weightingFactor()
	{
		return timeoutsFactor() * (double) contentPages / (double) indexPages;
	}
	
	/**
	 * Gets the ratio of index pages to content pages
	 * @return number of index pages divided by the number of content pages
	 */
	public double getIndexContentRatio()
	{
		return indexPages/(double)contentPages;
	}
	
}
