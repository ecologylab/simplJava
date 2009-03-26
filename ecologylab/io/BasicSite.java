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
	
	/**
	 *	
	 * @return (numTimeouts == 0) ? 1 : 1.0 / (numTimeouts + 1);
	 */
	public double timeoutsFactor()
	{
		return (numTimeouts == 0) ? 1 : 1.0 / (numTimeouts + 1);
	}
	
}
