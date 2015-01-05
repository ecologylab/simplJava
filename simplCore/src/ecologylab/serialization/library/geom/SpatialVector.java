package ecologylab.serialization.library.geom;

import java.awt.geom.Point2D;

import ecologylab.serialization.ElementState;

/**
 * The base class for all the Vector classes, so that, even though they do not
 * extend each other, they do extend a common class (thus, they can be used with
 * generics interchangably).
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public abstract class SpatialVector extends ElementState
{
	public SpatialVector()
	{

	}

	public abstract double getX();
	public abstract double getY();
	public abstract Point2D toPoint();
}
