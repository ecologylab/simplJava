/*
 * Added by Qing Xing
 */

package ecologylab.tutorials.game;

import ecologylab.serialization.annotations.*;
/**
 * Predators are Targetters that hunt down SeekerAvatars in the game. Their in-game behavior is to
 * search (by looking in circles) for nearby SeekerAvatars, and then moving to touch those
 * SeekerAvatars (and thus make them out).
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 * @author Qing Xing
 */
public @simpl_inherit
@simpl_tag("nt")
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
