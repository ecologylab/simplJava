package ecologylab.xml.library.geom;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import ecologylab.xml.ElementState;
import ecologylab.xml.xml_inherit;

/**
 * Encapsulates a Rectangle2D.Double for use in translating to/from XML.
 * 
 * ***WARNING!!!***
 * 
 * Performing transformations (such as setFrame()) on the result of getRect()
 * will cause this object to become out of synch with its underlying
 * Rectangle2D. DO NOT DO THIS!
 * 
 * If other transformation methods are required, either notify me, or implement
 * them yourself. :D
 * 
 * Accessor methods (such as contains()) on the result of getRect() are fine.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public @xml_inherit class Line2DDoubleState extends ElementState implements
		Shape
{
	@xml_attribute protected double	x1					= 0;

	@xml_attribute protected double	x2					= 0;

	@xml_attribute protected double	y1					= 0;

	@xml_attribute protected double	y2					= 0;

	private Line2D.Double				line				= null;

	private Vector2d						normal			= null;

	public Line2DDoubleState()
	{
		super();
	}

	public Line2DDoubleState(double x1, double y1, double x2, double y2)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;

		this.computeNormal();

		line = new Line2D.Double(x1, y1, x2, y2);
	}

	public void setLine(double x1, double y1, double x2, double y2)
	{
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;

		this.computeNormal();

		line.setLine(x1, y1, x2, y2);
	}

	public boolean contains(Point2D p)
	{
		return line.contains(p);
	}

	public boolean contains(Rectangle2D r)
	{
		return line.contains(r);
	}

	public boolean contains(double x, double y)
	{
		return line.contains(x, y);
	}

	public boolean contains(double x, double y, double w, double h)
	{
		return line.contains(x, y, w, h);
	}

	public Rectangle getBounds()
	{
		return line.getBounds();
	}

	public Rectangle2D getBounds2D()
	{
		return line.getBounds2D();
	}

	public PathIterator getPathIterator(AffineTransform at)
	{
		return line.getPathIterator(at);
	}

	public PathIterator getPathIterator(AffineTransform at, double flatness)
	{
		return line.getPathIterator(at, flatness);
	}

	public boolean intersects(Rectangle2D r)
	{
		return line.intersects(r);
	}

	public boolean intersects(double x, double y, double w, double h)
	{
		return line.intersects(x, y, w, h);
	}

	/**
	 * Returns the normal to the plane that runs parallel to the Z-axis and
	 * through this. Computed as if there is a second vector (0, 0, 1) to cross
	 * with this + 0 on the z axis.
	 * 
	 * @return the normal to this.
	 */
	public Vector2d getNormal()
	{
		return normal;
	}

	private void computeNormal()
	{
		double x = x2 - x1;
		double y = y2 - y1;
		double z = 0;

		double pX = 0;
		double pY = 0;
		double pZ = 1;

		this.normal = new Vector2d(y * pZ - z * pY, z * pX - x * pZ);
	}

	/**
	 * @return the line
	 */
	public Line2D.Double line()
	{
		return line;
	}
}
