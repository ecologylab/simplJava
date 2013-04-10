/**
 * 
 */
package ecologylab.serialization.library.geom;

import simpl.annotations.dbal.simpl_scalar;

/**
 * @author andruid
 *
 */
public class PointInt
{
	@simpl_scalar
	int								x;
	@simpl_scalar
	int								y;
	
	public PointInt()
	{
		
	}
	public PointInt(int x, int y)
	{
		this.x	= x;
		this.y  = y;
	}
	
	public int getX()
	{
		return x;
	}
	public void setX(int x)
	{
		this.x = x;
	}
	public int getY()
	{
		return y;
	}
	public void setY(int y)
	{
		this.y = y;
	}
}
