/*
 * Created on Dec 31, 2004 at the Interface Ecology Lab.
 */
package ecologylab.types;

import java.lang.reflect.Field;

/**
 * Type system entry for float, a built-in primitive.
 * 
 * @author andruid
 */
public class FloatType extends Type 
{
	public FloatType()
	{
		super("float", true);
	}

	/**
	 * Convert the parameter to float.
	 */
	public float getValue(String valueString)
	{
		return Float.parseFloat(valueString);
	}
	
	/**
	 * This is a primitive type, so we set it specially.
	 * 
	 * @see ecologylab.types.Type#setField(java.lang.reflect.Field, java.lang.String)
	 */
	public boolean setField(Object object, Field field, String value) 
	{
		boolean result	= false;
		try
		{
			field.setFloat(object, getValue(value));
			result		= true;
		} catch (Exception e)
		{
			debug(errorString(field) + "to " + value);
		}
		return result;
	}
}
