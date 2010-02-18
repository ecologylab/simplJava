/*
 * Created on Mar 23, 2005
 */
package ecologylab.tutorials.polymorphic.rogue.game2d.entity;

import ecologylab.xml.xml_inherit;

/**
 * A Targetter is a Mover that is able to track a target (i.e. another Entity). Targetter allows
 * Predators to have a Seeker to chase down (for their limited AI), and allows Seekers to track how
 * close the nearest Predator is to them.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public abstract @xml_inherit
class Targetter extends Mover
{
	/**
	 * Higher desirability means that the target is more likely to be chosen over other possible
	 * targets.
	 */
	@xml_attribute
	protected double				tVal					= -1;

	/**
	 * No arguments constructor; required for XML translation.
	 */
	public Targetter()
	{
		super();
	}
}