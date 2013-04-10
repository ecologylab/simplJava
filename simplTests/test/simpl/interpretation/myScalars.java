package simpl.interpretation;

import simpl.annotations.dbal.simpl_scalar;

final class myScalars{
	
	public myScalars(){}
	
	@simpl_scalar
	public String aField;
	@simpl_scalar
	public Integer aInteger;
	@simpl_scalar
	public Double aDouble;
}