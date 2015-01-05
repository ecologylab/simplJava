/**
 * 
 */
package ecologylab.tutorials.game;

import ecologylab.serialization.annotations.*;

/**
 * @author Zachary O. Toups (toupsz@ecologylab.net)
 * 
 */

@simpl_inherit
@simpl_tag("sst")
public class SingleSeekerThreat extends Threat
{
	@simpl_scalar
	int	targetOrd;

	/**
	 * Default Constructor
	 */
	public SingleSeekerThreat()
	{
	}
}
