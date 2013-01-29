package simpl.interpretation;

public class ScalarInterpretation {
	public String fieldName;
	public String fieldValue;
	
	public ScalarInterpretation(String name, String value)
	{
		this.fieldName = name;
		this.fieldValue = value;
	}
	
	public String toString()
	{
		return fieldName + "=["+ fieldValue + "]";
	}
}
