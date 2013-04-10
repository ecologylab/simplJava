package simpl.interpretation;

import ecologylab.serialization.primaryScenarioEnum;
import ecologylab.serialization.secondaryScenarioEnum;
import simpl.annotations.dbal.simpl_scalar;

public class myEnumerations {

	public myEnumerations(){}
	
	@simpl_scalar
	public String myString;
	
	@simpl_scalar
	public primaryScenarioEnum primaryEnum;
	
	@simpl_scalar
	public secondaryScenarioEnum secondaryEnum;
	
	@simpl_scalar
	public secondaryScenarioEnum secondaryEnumInts;
	
}
