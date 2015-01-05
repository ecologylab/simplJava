/**
 * 
 */
package ecologylab.tutorials.game;

import ecologylab.serialization.annotations.*;

/**
 * A special predator that moves from goal to goal when not chasing the seekers.
 * 
 * The purpose of this predator is to help give the predator flocks some intelligence and get them
 * to move around a bit instead of clumping too much due to their flocking behavior.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 * 
 */
@simpl_inherit
@simpl_tag("pt")
public class PatrollingThreat extends Threat
{
	/** Constructor stub for XML translation. */
	public PatrollingThreat()
	{
	}
}
