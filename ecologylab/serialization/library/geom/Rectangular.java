/**
 * 
 */
package ecologylab.serialization.library.geom;

import ecologylab.serialization.ElementState;

/**
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public abstract class Rectangular extends ElementState
{
	/**
	 * Location and dimensions of the ellipse.
	 */
	@simpl_scalar
	protected double	x	= 0;

	@simpl_scalar
	protected double	y	= 0;

	@simpl_scalar
	protected double	w	= 0;

	@simpl_scalar
	protected double	h	= 0;

	public Rectangular()
	{
		super();
	}

	public Rectangular(double x, double y, double width, double height)
	{
		super();

		setFrame(x, y, width, height);
	}

	public void setFrame(double x, double y, double w, double h)
	{
		this.x = x;
		this.y = y;
		// if we have a negative width or height, we assume the rectangle should
		// just be translated to ensure that w and h are always positive
		if (w < 0)
		{
			x += w;
			w *= -1;
		}

		this.w = w;

		if (h < 0)
		{
			y += h;
			h *= -1;
		}

		this.h = h;
	}

	/**
	 * @return the h
	 */
	public double getHeight()
	{
		return h;
	}

	/**
	 * @return the w
	 */
	public double getWidth()
	{
		return w;
	}

	/**
	 * @return the x
	 */
	public double getX()
	{
		return x;
	}

	/**
	 * @return the y
	 */
	public double getY()
	{
		return y;
	}
}
