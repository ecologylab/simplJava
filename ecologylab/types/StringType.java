/*
 * Created on Dec 31, 2004 at the Interface Ecology Lab.
 */
package ecologylab.types;

import java.lang.reflect.Field;

/**
 * Type system entry for {@link java.lang.String String}. A very simple case.
 * 
 * @author andruid
 */
public class StringType extends Type
{
/**
 * This constructor should only be called once per session, through
 * a static initializer, typically in TypeRegistry.
 * <p>
 * To get the instance of this type object for use in translations, call
 * <code>TypeRegistry.get("java.lang.String")</code>.
 * 
 */
	protected StringType()
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
