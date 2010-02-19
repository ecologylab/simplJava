/**
 * 
 */
package ecologylab.tutorials.polymorphic.rogue.entity.threat;

import ecologylab.xml.xml_inherit;
import ecologylab.xml.ElementState.xml_tag;

/**
 * @author Zachary O. Toups (toupsz@ecologylab.net)
 * 
 */

@xml_inherit
@xml_tag("sst")
public class SingleSeekerThreat extends Threat
{
	@xml_attribute
	int	targetOrd;

	/**
	 * Default Constructor
	 */
	public SingleSeekerThreat()
	{
	}
}
