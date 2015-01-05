/*
 * Created on Nov 14, 2006
 */
package ecologylab.serialization.library.geom;

import java.awt.geom.Point2D;

import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public @simpl_tag("vect2df") class Vector2dFloat extends SpatialVector implements
		Cloneable
{
	/**
	 * Adds two vectors together and returns a new Vector2d object representing
	 * the sum.
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static Vector2dFloat add(Vector2dFloat v1, Vector2dFloat v2)
	{
		return new Vector2dFloat(v1.x + v2.x, v1.y + v2.y);
	}

	/**
	 * Multiplies a vector by a scalar value and returns a new Vector2d
	 * representing the result.
	 * 
	 * @param vector
	 * @param scalar
	 * @return
	 */
	public static Vector2dFloat scalarMultiply(Vector2dFloat vector, float scalar)
	{
		return new Vector2dFloat(vector.x * scalar, vector.y * scalar);
	}

	/**
	 * Determines the dot product of two vector objects.
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static float dot(Vector2dFloat v1, Vector2dFloat v2)
	{
		return (v1.x * v2.x) + (v1.y * v2.y);
	}

	/**
	 * Subtracts v2 from v1 and returns a new Vector2d representing the result.
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static Vector2dFloat sub(Vector2dFloat v1, Vector2dFloat v2)
	{
		return new Vector2dFloat(v1.x - v2.x, v1.y - v2.y);
	}

	protected @simpl_scalar float	x;

	protected @simpl_scalar float	y;

	protected Point2D.Float				point	= null;

	/**
	 * 
	 */
	public Vector2dFloat()
	{
		super();
	}

	public Vector2dFloat(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	public Vector2dFloat(Vector2dFloat otherVect)
	{
		x = otherVect.x;
		y = otherVect.y;
	}

	public void add(Vector2dFloat v)
	{
		this.x += v.getX();
		this.y += v.getY();

		updatePointIfNotNull();
	}

	public double norm()
	{
		return Math.sqrt(x * x + y * y);
	}

	public void mult(float scalar)
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

		float x1 = this.x;
		float y1 = this.y;

		this.x = (float) ((x1 * cos) - (y1 * sin));
		this.y = (float) ((y1 * cos) + (x1 * sin));

		updatePointIfNotNull();
	}

	/**
	 * Rotates this vector so that it is aligned to the specified angle in
	 * radians.
	 * 
	 * @param angle -
	 *           in radians
	 */
	public void rotateTo(float angle)
	{
		// TODO gotta make this more efficient!
		this.rotate(angle - this.toRadians());
	}

	public void sub(Vector2dFloat v)
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

	public Vector2dFloat unitVector()
	{
		double mag = this.norm();

		return new Vector2dFloat((float) (this.x / mag), (float) (this.y / mag));
	}

	public void unitize()
	{
		double mag = this.norm();

		this.set((float) (this.x / mag), (float) (this.y / mag));
	}

	/**
	 * @see java.lang.Object#clone()
	 */
	@Override public Vector2dFloat clone()
	{
		return new Vector2dFloat(this);
	}

	public void set(Vector2dFloat pos)
	{
		this.set(pos.x, pos.y);
	}

	@Override public Point2D toPoint()
	{
		if (this.point == null)
			point = new Point2D.Float(x, y);

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
	public void setNorm(float mag)
	{
		this.unitize();
		this.mult(mag);
	}

	public void zero()
	{
		x = 0;
		y = 0;
	}

	public void set(float x, float y)
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
	public void setY(float y)
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