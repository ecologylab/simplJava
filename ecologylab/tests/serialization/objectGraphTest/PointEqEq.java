package ecologylab.tests.serialization.objectGraphTest;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.simpl_use_equals_equals;

@simpl_use_equals_equals
public class PointEqEq extends ElementState {
	@simpl_scalar
	public int x;
	@simpl_scalar
	public int y;

	@Deprecated
	public PointEqEq() {}
	public PointEqEq(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int hashCode() {
		return x+y;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof PointEqEq)) {
			return false;
		}
		PointEqEq o = (PointEqEq)other;
		return x == o.x && y == o.y;
	}
}