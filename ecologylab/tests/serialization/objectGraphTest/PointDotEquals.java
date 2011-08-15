package ecologylab.tests.serialization.objectGraphTest;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_scalar;

public class PointDotEquals extends ElementState {
	@simpl_scalar
	public int x;
	@simpl_scalar
	public int y;

	@Deprecated
	public PointDotEquals() {}
	public PointDotEquals(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int hashCode() {
		return x+y;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof PointDotEquals)) {
			return false;
		}
		PointDotEquals o = (PointDotEquals)other;
		return x == o.x && y == o.y;
//		return (x + y) == (o.x + o.y);
	}
	
	@Override
	public String toString()
	{
		return "["+x+","+y+"]";
	}
}