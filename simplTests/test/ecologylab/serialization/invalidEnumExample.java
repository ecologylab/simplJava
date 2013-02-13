package ecologylab.serialization;


public enum invalidEnumExample {

	firstValue(3),
	secondValue(5),
	thirdValue(7);

	private Integer myValue;
	
	private invalidEnumExample(Integer aValue)
	{
		this.myValue = aValue;
	}	
}
