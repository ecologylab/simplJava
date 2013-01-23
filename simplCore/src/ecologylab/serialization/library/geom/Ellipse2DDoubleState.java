package ecologylab.serialization.library.geom;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.geom.Rectangle2D;

import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_other_tags;
import ecologylab.serialization.annotations.simpl_tag;

/**
 * Encapsulates a Ellipse2D.Double for use in translating to/from XML.
 * 
 * ***WARNING!!!***
 * 
 * Performing transformations (such as setFrame()) on the result of shape() will cause this object
 * to become out of synch with its underlying Ellipse2D.Double object. While this will have no bad
 * ramifications for this, it MAY have ramifications for what you INTENDED to do. Essentially,
 * changes to the returned Shape will NOT be reflected in the XML produced by this.
 * 
 * If other transformation methods are required, either notify me, or implement them yourself. :D
 * 
 * Accessor methods (such as contains()) on the result of getEllipse() are fine.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
@simpl_tag("El2DD")
@simpl_other_tags("ellipse2_d_double")
public @simpl_inherit
class Ellipse2DDoubleState extends RectangularShape
{
	protected Ellipse2D.Double	shape	= null;

	private Double							centerPoint;

	public Ellipse2DDoubleState()
	{
		super();
	}

	public Ellipse2DDoubleState(double x, double y, double width, double height)
	{
		super(x, y, width, height);
	}

	public Ellipse2DDoubleState(double xCenter, double yCenter, double radius)
	{
		this(xCenter - radius, yCenter - radius, radius * 2.0, radius * 2.0);
	}

	/**
	 * Returns an Ellipse2D object represented by this.
	 */
	@Override
	public Ellipse2D.Double shape()
	{
		if (shape == null)
		{
			shape = new Ellipse2D.Double(x, y, w, h);
		}
		else if (shape.x != x || shape.y != y || shape.height != h || shape.width != w)
		{
			shape.setFrame(x, y, w, h);
		}

		return shape;
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

	public Point2D.Double centerPoint()
	{
		if (centerPoint == null)
		{
			synchronized (this)
			{
				if (centerPoint == null)
				{
					centerPoint = new Point2D.Double(this.getX() + (this.getWidth() / 2.0), this.getY()
							+ (this.getHeight() / 2.0));
				}
			}
		}

		return centerPoint;
	}
}
