package ecologylab.serialization.library.geom;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_inherit;

/**
 * Encapsulates a Polygon for use in translating to/from XML.
 * 
 * ***WARNING!!!***
 * 
 * Performing transformations (such as setFrame()) on the result of shape() will cause this object
 * to become out of synch with its underlying Rectangle2D. DO NOT DO THIS!
 * 
 * If other transformation methods are required, either notify me, or implement them yourself. :D
 * 
 * Accessor methods (such as contains()) on the result of getRect() are fine.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 * @author Alan Blevins (alan.blevins@gmail.com)
 */
public @simpl_inherit
class PolygonState extends ElementState implements Shape
{
	private Polygon												shape							= null;

	@simpl_collection("point2_d_double")
	private ArrayList<Point2DDoubleState>	polygonVerticies	= new ArrayList<Point2DDoubleState>();

	public PolygonState()
	{
		super();
	}

	public PolygonState(List<Point2DDoubleState> verticies)
	{
		super();

		definePolygon(verticies);
	}

	public void definePolygon(List<Point2DDoubleState> verticies)
	{
		polygonVerticies.clear();
		polygonVerticies.addAll(verticies);

		shape = null;
	}

	/**
	 * Returns a Polygon object represented by this.
	 */
	public Polygon shape()
	{
		if (shape == null)
		{
			shape = new Polygon();
			for (Point2DDoubleState vert : polygonVerticies)
			{
				shape.addPoint((int) vert.x, (int) vert.y);
			}
		}
		return shape;
	}

	public void invalidateShape()
	{
		shape = null;
	}

	public int numVerticies()
	{
		return polygonVerticies.size();
	}

	public Point2DDoubleState getVertex(int index)
	{
		return polygonVerticies.get(index);
	}

	public boolean contains(SpatialVector v)
	{
		return shape().contains(v.getX(), v.getY());
	}

	@Override
	public boolean contains(Point2D p)
	{
		return shape().contains(p);
	}

	@Override
	public boolean contains(Rectangle2D r)
	{
		return shape().contains(r);
	}

	@Override
	public boolean contains(double x, double y)
	{
		return shape().contains(x, y);
	}

	@Override
	public boolean contains(double x, double y, double w, double h)
	{
		return shape().contains(x, y, w, h);
	}

	@Override
	public Rectangle getBounds()
	{
		return shape().getBounds();
	}

	@Override
	public Rectangle2D getBounds2D()
	{
		return shape().getBounds2D();
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at)
	{
		return shape().getPathIterator(at);
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at, double flatness)
	{
		return shape().getPathIterator(at, flatness);
	}

	@Override
	public boolean intersects(Rectangle2D r)
	{
		return shape().intersects(r);
	}

	@Override
	public boolean intersects(double x, double y, double w, double h)
	{
		return shape().intersects(x, y, w, h);
	}

	/**
	 * Returns the list of polygon vertices. Modify it at your own risk.
	 * 
	 * @return polygonVerticies
	 */
	public ArrayList<Point2DDoubleState> getPolygonVerticies()
	{
		return polygonVerticies;
	}

}
