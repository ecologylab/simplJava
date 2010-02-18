/*
 * Created on Nov 14, 2006
 */
package ecologylab.tutorials.polymorphic.rogue.game2d.common;

import java.awt.geom.Point2D;

import ecologylab.xml.library.geom.SpatialVector;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public class CompressedVector extends SpatialVector implements Cloneable
{
	protected @xml_attribute
	@xml_format("#.##")
	double										x;

	protected @xml_attribute
	@xml_format("#.##")
	double										y;

	protected Point2D.Double	point	= null;

	/**
	 * 
	 */
	public CompressedVector()
	{
		super();
	}

	public CompressedVector(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public CompressedVector(CompressedVector otherVect)
	{
		x = otherVect.getX();
		y = otherVect.getY();
	}

	@Override
	public double getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Point2D toPoint() {
		// TODO Auto-generated method stub
		return null;
	}
}