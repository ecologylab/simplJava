/*
 * Created on Dec 31, 2004 at the Interface Ecology Lab.
 */
package ecologylab.types;

/**
 * Type system entry for Strings. A very simple case.
 * 
 * @author andruid
 */
public class StringType extends Type
{
	public StringType()
	{
		super("java.lang.String", /*TYPE_STRING, */ false);
	}

	/**
	 * Just return the value itself. A transparent pass-through.
	 * 
	 * @see ecologylab.types.Type#getInstance(java.lang.String)
	 */
	public Object getInstance(String value)
	{
		return value;
	}
}
