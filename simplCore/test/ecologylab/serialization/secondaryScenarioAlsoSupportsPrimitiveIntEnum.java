package ecologylab.serialization;

import ecologylab.serialization.annotations.simpl_scalar;

public enum secondaryScenarioAlsoSupportsPrimitiveIntEnum {

	firstValue(3),
	secondValue(5),
	thirdValue(7);

	@simpl_scalar
	private int myValue;
	
	private secondaryScenarioAlsoSupportsPrimitiveIntEnum(int aValue)
	{
		this.myValue = aValue;
	}	
}