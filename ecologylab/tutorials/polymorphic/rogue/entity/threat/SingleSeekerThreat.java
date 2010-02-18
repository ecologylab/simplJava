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
public @xml_inherit
@xml_tag("sst")
class SingleSeekerThreat extends Threat
{
	@xml_attribute
	int	targetOrd;

	/**
	 * 
	 */
	public SingleSeekerThreat()
	{
	}
}
