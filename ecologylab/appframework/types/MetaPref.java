/**
 * 
 */
package ecologylab.appframework.types;

import ecologylab.xml.ElementState;

/**
 * Metadata about a Preference.
 * Defines information to enable editing the Preference.
 * 
 * @author andruid
 *
 */
public class MetaPref /* <T> */ extends ElementState
{
	/**
	 * Unique identifier for Preference name with convenient lookup in automatically generated HashMap.
	 */
	@xml_attribute 	String		id;
	
	/**
	 * This is the short text that appears in the Swing panel for editing the value.
	 */
	@xml_attribute 	String		description;
	
	/**
	 * This is longer text about that describes what the Preference does and more about the implications
	 * of its value.
	 */
	@xml_attribute 	String		helpText;
	
	/**
	 * Categories enable tabbed panes of preferences to be edited.
	 */
	@xml_attribute 	String		category;
	
//	@xml_nested		RangeState<T>	range;
	/**
	 * 
	 */
	public MetaPref()
	{
		super();
	}
/*
	public boolean isWithinRange(T newValue)
	{
		return (range == null) ? true :  range.isWithinRange(newValue);
	}
	*/
}
