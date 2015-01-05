package ecologylab.serialization;

import ecologylab.serialization.annotations.simpl_scalar;

public enum secondaryScenarioEnum {

	firstValue(3),
	secondValue(5),
	thirdValue(7);

	@simpl_scalar
	private Integer myValue;
	
	private secondaryScenarioEnum(Integer aValue)
	{
		this.myValue = aValue;
	}	
}