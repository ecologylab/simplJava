/*
 * Created on Dec 31, 2004 at the Interface Ecology Lab.
 */
package ecologylab.types;

import java.lang.reflect.Field;

/**
 * Type system entry for boolean, a built-in primitive.
 * 
 * @author andruid
 */
public class BooleanType extends Type 
{
/**
 * This constructor should only be called once per session, through
 * a static initializer, typically in TypeRegistry.
 * <p>
 * To get the instance of this type object for use in translations, call
 * <code>TypeRegistry.get("boolean")</code>.
 * 
 */
	public BooleanType()
	{
		super(boolean.class);
	}

	/**
	 * Convert the parameter to boolean.
	 */
	public boolean getValue(String valueString)
	{
	   String lcValue= valueString.toLowerCase();
	   return lcValue.equals("t") || lcValue.equals("true") ||
		  lcValue.equals("yes") || (lcValue.equals("1"));
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
			field.setBoolean(object, getValue(value));
			result		= true;
		} catch (Exception e)
		{
            setFieldError(field, value, e);
		}
		return result;
	}
/**
 * The string representation for a Field of this type
 */
	public String toString(Object object, Field field)
	{
	   String result	= "COULDN'T CONVERT!";
	   try
	   {
		  result		= Boolean.toString(field.getBoolean(object));
	   } 
       catch (Exception e)
	   {
		  e.printStackTrace();
	   }
	   return result;
	}

/**
 * The default value for this type, as a String.
 * This value is the one that translateToXML(...) wont bother emitting.
 * 
 * @return		"false"
 */
	public String defaultValue()
	{
	   return "false";
	}
}
