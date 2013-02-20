package simpl.interpretation;

import simpl.annotations.dbal.simpl_composite;
import simpl.annotations.dbal.simpl_scalar;

public class nonCycleRef {

	public nonCycleRef(){}

	@simpl_composite
	public myScalars left;
	
	@simpl_composite
	public myScalars right;
	
	@simpl_scalar
	public String myString; 

}
