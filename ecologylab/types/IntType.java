/*
 * Created on Dec 31, 2004 at the Interface Ecology Lab.
 */
package ecologylab.types;

import java.lang.reflect.Field;

/**
 * Type system entry for int, a built-in primitive.
 * 
 * @author andruid
 */
public class IntType extends Type 
{
	public IntType()
	{
		super("int", true);
	}

	/**
	 * Convert the parameter to int.
	 */
	public int getValue(String valueString)
	{
		return Integer.parseInt(valueString);
	}
	
	/**
	 * This is a primitive type, so we set it specially.
	 * 
	 * @see ecologylab.types.Type#setField(java.lang.reflect.Field, java.lang.String)
	 */
	public boolean setField(Object object, Field field, String value) 
	{
		boolean result	= false;
		int converted	= Integer.MIN_VALUE;
		try
		{
		   converted	= getValue(value);
		   field.setInt(object, converted);
		   result		= true;
		} catch (Exception e)
		{
		   debug("Got " + e + " while setting field " +
				 field + " to " + value+"->"+converted);
		}
		return result;
	}
}
