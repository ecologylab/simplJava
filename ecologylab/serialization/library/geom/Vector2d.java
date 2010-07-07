/*
 * Created on Nov 14, 2006
 */
package ecologylab.serialization.library.geom;

import java.awt.geom.Point2D;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public class Vector2d extends SpatialVector implements Cloneable
{
	/**
	 * Adds two vectors together and returns a new Vector2d object representing
	 * the sum.
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static Vector2d add(Vector2d v1, Vector2d v2)
	{
		return new Vector2d(v1.getX() + v2.getX(), v1.getY() + v2.getY());
	}

	/**
	 * Multiplies a vector by a scalar value and returns a new Vector2d
	 * representing the result.
	 * 
	 * @param vector
	 * @param scalar
	 * @return
	 */
	public static Vector2d scalarMultiply(Vector2d vector, double scalar)
	{
		return new Vector2d(vector.getX() * scalar, vector.getY() * scalar);
	}

	/**
	 * Determines the dot product of two vector objects.
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double dot(Vector2d v1, Vector2d v2)
	{
		return (v1.getX() * v2.getX()) + (v1.getY() * v2.getY());
	}

	/**
	 * Subtracts v2 from v1 and returns a new Vector2d representing the result.
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static Vector2d sub(SpatialVector v1, SpatialVector v2)
	{
		return new Vector2d(v1.getX() - v2.getX(), v1.getY() - v2.getY());
	}

	protected @simpl_scalar double	x;

	protected @simpl_scalar double	y;

	protected Point2D.Double			point	= null;

	/**
	 * 
	 */
	public Vector2d()
	{
		super();

		// zero();
	}

	public Vector2d(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public Vector2d(Vector2d otherVect)
	{
		x = otherVect.getX();
		y = otherVect.getY();
	}

	public void add(Vector2d v)
	{
		this.x += v.getX();
		this.y += v.getY();

		updatePointIfNotNull();
	}

	public double norm()
	{
		return Math.sqrt(x * x + y * y);
	}

	public void mult(double scalar)
	{
		this.x *= scalar;
		this.y *= scalar;

		updatePointIfNotNull();
	}

	/**
	 * Rotates this vector around the origin by the specified angle in degrees.
	 * 
	 * @param angle -
	 *           in radians
	 */
	public void rotate(double angle)
	{
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);

		double x1 = this.x;
		double y1 = this.y;

		this.x = (x1 * cos) - (y1 * sin);
		this.y = (y1 * cos) + (x1 * sin);

		updatePointIfNotNull();
	}

	/**
	 * Rotates this vector so that it is aligned to the specified angle in
	 * radians.
	 * 
	 * @param angle -
	 *           in radians
	 */
	public void rotateTo(double angle)
	{
		// TODO gotta make this more efficient!
		this.rotate(angle - this.toRadians());
	}

	public void sub(Vector2d v)
	{
		this.x -= v.getX();
		this.y -= v.getY();

		updatePointIfNotNull();
	}

	/**
	 * Converts the vector into a radian angle. If the result would be NaN,
	 * returns 0.
	 * 
	 * @return
	 */
	public double toRadians()
	{
		double result = Math.atan2(y, x);

		if (Double.isNaN(result))
			result = 0;

		return result;
	}

	public Vector2d unitVector()
	{
		double mag = this.norm();

		return new Vector2d(this.x / mag, this.y / mag);
	}

	public void unitize()
	{
		double mag = this.norm();

		this.set(this.x / mag, this.y / mag);
	}

	/**
	 * @see java.lang.Object#clone()
	 */
	@Override public Vector2d clone()
	{
		return new Vector2d(this);
	}

	public void set(Vector2d pos)
	{
		this.set(pos.getX(), pos.getY());
	}

	@Override public Point2D toPoint()
	{
		if (this.point == null)
			point = new Point2D.Double(x, y);

		return point;
	}

	/**
	 * @see ecologylab.generic.Debug#toString()
	 */
	@Override public String toString()
	{
		return "(" + x + ", " + y + ")";
	}

	/**
	 * Adjusts the magnitude of this vector to match mag.
	 * 
	 * @param mag
	 */
	public void setNorm(double mag)
	{
		this.unitize();
		this.mult(mag);
	}

	public void zero()
	{
		x = 0;
		y = 0;
	}

	public void set(double x, double y)
	{
		this.x = x;
		this.y = y;

		updatePointIfNotNull();
	}

	/**
	 * @param x
	 */
	private void updatePointIfNotNull()
	{
		if (this.point != null)
		{
			synchronized (point)
			{
				point.setLocation(x, y);
			}
		}
	}

	/**
	 * @param y
	 *           the y to set
	 */
	public void setX(double x)
	{
		this.x = x;

		updatePointIfNotNull();
	}

	
	/**
	 * @param y
	 *           the y to set
	 */
	public void setY(double y)
	{
		this.y = y;

		updatePointIfNotNull();
	}

	/**
	 * @return the x
	 */
	@Override public double getX()
	{
		return x;
	}

	/**
	 * @return the y
	 */
	@Override public double getY()
	{
		return y;
	}
}