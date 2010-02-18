/*
 * Created on Feb 18, 2005
 */
package ecologylab.tutorials.polymorphic.rogue.game2d.entity;


import ecologylab.tutorials.polymorphic.rogue.game2d.common.CompressedVector;
import ecologylab.tutorials.polymorphic.rogue.game2d.common.EntityType;
import ecologylab.xml.xml_inherit;

/**
 * Movers are Entities capable of movement by certain constrained actions. A Mover has a facing, and
 * it is able to move forward and backward with a maxSpeed from that facing.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public @xml_inherit
class Mover extends Entity implements EntityType
{
	/** Unit vector indicating current facing. */
	protected @xml_nested
	CompressedVector						dir;

	/** Vector indicating current velocity. */
	protected @xml_nested
	CompressedVector						vel;

	/**
	 * No-arguments constructor; required for XML translation.
	 */
	public Mover()
	{
		super();
	}
}