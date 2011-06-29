package ecologylab.tests.serialization.objectGraphTest;

import ecologylab.serialization.ElementState;

public class Point extends ElementState {
	@simpl_scalar
	public int x;
	@simpl_scalar
	public int y;

	@Deprecated
	public Point() {}
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
}