package simpl.descriptions.testclasses;

import simpl.annotations.dbal.simpl_scalar;
import simpl.annotations.dbal.simpl_tag;

@myRandomAnnotation
@simpl_tag("my_spiffy_tag")
public enum annotatedEnumeration {

	@myRandomAnnotation
	firstValue(3),
	secondValue(5),
	thirdValue(7);

	@simpl_scalar
	@myRandomAnnotation
	private Integer myValue;
	
	private annotatedEnumeration(Integer aValue)
	{
		this.myValue = aValue;
	}	
}
