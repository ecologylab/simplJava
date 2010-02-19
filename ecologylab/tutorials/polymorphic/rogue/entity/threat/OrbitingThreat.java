/**
 * 
 */
package ecologylab.tutorials.polymorphic.rogue.entity.threat;

import ecologylab.xml.xml_inherit;
import ecologylab.xml.ElementState.xml_tag;

/**
 * A special predator that finds a goal and stays near it.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */

@xml_inherit
@xml_tag("ot")
public class OrbitingThreat extends Threat
{
	/** Constructor stub for XML translation. */
	public OrbitingThreat()
	{
	}
}
