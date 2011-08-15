/*
 * Created on Aug 29, 2006
 */
package ecologylab.serialization.library.geom;

import java.awt.Shape;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_scalar;

public abstract class RectangularShape extends ElementState implements Shape
{
	/**
	 * Location and dimensions of the ellipse.
	 */
	protected @simpl_scalar double	x	= 0;

	protected @simpl_scalar double	y	= 0;

	protected @simpl_scalar double	w	= 0;

	protected @simpl_scalar double	h	= 0;

	public RectangularShape()
	{
		super();
	}

	public RectangularShape(double x, double y, double width, double height)
	{
		super();

		setFrame(x, y, width, height);
	}

	public abstract java.awt.geom.RectangularShape shape();

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
