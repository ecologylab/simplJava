/*
 * Created on Dec 31, 2004 at the Interface Ecology Lab.
 */
package ecologylab.types;

import java.lang.reflect.Field;

/**
 * Type system entry for short, a built-in primitive.
 * 
 * @author andruid
 */
public class ShortType extends Type 
{
	public ShortType()
	{
		super("short", true);
	}

	/**
	 * Convert the parameter to short.
	 */
	public short getValue(String valueString)
	{
		return Short.parseShort(valueString);
	}
	
	/**
	 * This is a primitive type, so we set it specially.
	 * 
	 * @see ecologylab.types.Type#setField(java.lang.reflect.Field, java.lang.String)
	 */
	public boolean setField(Field field, String value) 
	{
		boolean result	= false;
		try
		{
			field.setShort(this, getValue(value));
			result		= true;
		} catch (Exception e)
		{
			debug(errorString(field) + "to " + value);
		}
		return result;
	}
}
