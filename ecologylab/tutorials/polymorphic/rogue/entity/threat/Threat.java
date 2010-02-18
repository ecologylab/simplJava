/*
 * Created on Feb 13, 2005
 */

package ecologylab.tutorials.polymorphic.rogue.entity.threat;


import ecologylab.tutorials.polymorphic.rogue.game2d.entity.Targetter;
import ecologylab.xml.xml_inherit;
import ecologylab.xml.ElementState.xml_tag;

/**
 * Threats are Targetters that hunt down SeekerAvatars in the game. Their in-game behavior is to
 * search (by looking in circles) for nearby SeekerAvatars, and then moving to touch those
 * SeekerAvatars (and thus make them out).
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 * @author William Hamilton (bill@ecologylab.net)
 */
@xml_inherit
@xml_tag("t")
public class Threat extends Targetter
{
	/**
	 * No-argument constructor, required for ElementState.
	 */
	public Threat()
	{
		super();
	}
}