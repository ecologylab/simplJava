/*
 * Created on Dec 31, 2004 at the Interface Ecology Lab.
 */
package ecologylab.types;

import java.lang.reflect.Field;

/**
 * Type system entry for byte, a built-in primitive.
 * 
 * @author andruid
 */
public class ByteType extends Type 
{
	public ByteType()
	{
		super("byte", true);
	}

	/**
	 * Convert the parameter to byte.
	 */
	public byte getValue(String valueString)
	{
		return Byte.parseByte(valueString);
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
			field.setByte(object, getValue(value));
			result		= true;
		} catch (Exception e)
		{
			debug(errorString(field) + "to " + value);
		}
		return result;
	}
}
