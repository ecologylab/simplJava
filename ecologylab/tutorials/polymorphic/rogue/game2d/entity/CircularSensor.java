/*
 * Created on Feb 24, 2005
 */
package ecologylab.tutorials.polymorphic.rogue.game2d.entity;

import java.awt.geom.Ellipse2D;

/**
 * PredatorSensor is a shape that is associated with a Predator indicating what it can "see" in the
 * virtual world. Whether or not a Predator can sense another Entity depends on whether or not that
 * Entity is contain()'ed within the PredatorSensor object associated with the Predator.
 * 
 * This implementation actually just checks distance (since we're actually always using a circle)
 * rather than contains.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public class CircularSensor extends Ellipse2D.Double implements SenseZone
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CircularSensor(double x, double y, double facing)
	{
		super(x - 75, y - 75, 150, 150);
	}
}