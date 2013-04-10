/*
 * Created on Aug 29, 2006
 */
package ecologylab.serialization.library.geom;

import java.awt.Shape;

import simpl.annotations.dbal.simpl_inherit;




/**
 * Subclass that adds AWT.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
@simpl_inherit
public abstract class RectangularShape extends Rectangular implements Shape
{
	public RectangularShape()
	{
		super();
	}

	public RectangularShape(double x, double y, double width, double height)
	{
		super(x, y, width, height);
	}

	public abstract java.awt.geom.RectangularShape shape();
}
