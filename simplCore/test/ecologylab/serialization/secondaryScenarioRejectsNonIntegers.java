package ecologylab.serialization;

import ecologylab.serialization.annotations.simpl_scalar;

public enum secondaryScenarioRejectsNonIntegers {

	firstValue("3"),
	secondValue("5"),
	thirdValue("7");

	@simpl_scalar
	private String myValue;
	
	private secondaryScenarioRejectsNonIntegers(String aValue)
	{
		this.myValue = aValue;
	}	
}