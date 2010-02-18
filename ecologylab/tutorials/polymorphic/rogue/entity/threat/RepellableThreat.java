/*
 * Added by Qing Xing
 */

package ecologylab.tutorials.polymorphic.rogue.entity.threat;

import ecologylab.xml.xml_inherit;
import ecologylab.xml.ElementState.xml_tag;

/**
 * Predators are Targetters that hunt down SeekerAvatars in the game. Their in-game behavior is to
 * search (by looking in circles) for nearby SeekerAvatars, and then moving to touch those
 * SeekerAvatars (and thus make them out).
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 * @author Qing Xing
 */
public @xml_inherit
@xml_tag("nt")
class RepellableThreat extends Threat
{
	/**
	 * No-argument constructor, required for ElementState.
	 */
	public RepellableThreat()
	{
		super();
	}
}
