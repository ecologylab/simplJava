package simpl.interpretation.testclasses;

import simpl.annotations.dbal.simpl_composite_as_scalar;
import simpl.annotations.dbal.simpl_scalar;

public class basicCompositeAsScalar {

	@simpl_composite_as_scalar
	@simpl_scalar
	public Integer x; 
	
	@simpl_scalar
	public Integer y;
	
	public basicCompositeAsScalar(){
	}	
}
