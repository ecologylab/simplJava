package simpl.serialization;

import simpl.annotations.dbal.simpl_scalar;

public class Point {

	@simpl_scalar
	public int x;
	
	@simpl_scalar
	public Integer y;
	
	public Point(){
	}
}
