/*
 * Created on Feb 13, 2005
 */

package ecologylab.tutorials.game;


//import tutorials.polymorphic.rogue.game2d.entity.Entity;
import ecologylab.serialization.annotations.*;

/**
 * Threats are Targetters that hunt down SeekerAvatars in the game. Their in-game behavior is to
 * search (by looking in circles) for nearby SeekerAvatars, and then moving to touch those
 * SeekerAvatars (and thus make them out).
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 * @author William Hamilton (bill@ecologylab.net)
 */
@simpl_inherit
@simpl_tag("t")
public class Threat
{
	@simpl_scalar
	public double tVal;
	@simpl_scalar
	public String id;
	@simpl_scalar
	public int ord;
	
	@simpl_composite
	public Coordinate dir;
	
	@simpl_composite
	public Coordinate vel;
	
	@simpl_composite
	public Coordinate pos;
	
	
	/**
	 * No-argument constructor, required for ElementState.
	 */
	public Threat()
	{
		super();
	}
}