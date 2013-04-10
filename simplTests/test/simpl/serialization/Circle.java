package simpl.serialization;

import simpl.annotations.dbal.simpl_composite;
import simpl.annotations.dbal.simpl_scalar;

public class Circle {
	
	@simpl_scalar
	public double radius;
	
	@simpl_composite
	public Point center;
	
	public Circle()
	{
	}
}
