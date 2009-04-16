package ecologylab.xml.types.element;

import java.util.Map.Entry;

import ecologylab.xml.ElementState;
import ecologylab.xml.types.scalar.ScalarType;

public class StringDoubleEntry extends ElementState
{
	@xml_leaf	protected String	string;
	@xml_leaf	protected double 	value;
	
	public StringDoubleEntry()
	{
	}
	
	StringDoubleEntry(Entry<String,Double>  entry)
	{
		string = entry.getKey();
		value = entry.getValue();
	}
	
	public String getKey()
	{
		return string;
	}

	public Double getValue()
	{
		return value;
	}

	
}
