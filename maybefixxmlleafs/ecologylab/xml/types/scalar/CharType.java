/*
 * Created on Dec 31, 2004 at the Interface Ecology Lab.
 */
package ecologylab.xml.types.scalar;

import java.lang.reflect.Field;

/**
 * Type system entry for char, a built-in primitive.
 * 
 * @author andruid
 */
public class CharType extends ScalarType<Character>
{
/**
 * This constructor should only be called once per session, through
 * a static initializer, typically in TypeRegistry.
 * <p>
 * To get the instance of this type object for use in translations, call
 * <code>TypeRegistry.get("char")</code>.
 * 
 */
	public CharType()
	{
		super(char.class);
	}

	/**
	 * Convert the parameter to char.
	 */
	public char getValue(String valueString)
	{
		return valueString.charAt(0);
	}
	
    /**
     * Parse the String into the (primitive) type, and return a boxed instance.
     * 
     * @param value
     *            String representation of the instance.
     */
    public Character getInstance(String value)
    {
        return getValue(value);
    }

	/**
	 * This is a primitive type, so we set it specially.
	 * 
	 * @see ecologylab.xml.types.scalar.ScalarType#setField(java.lang.Object, java.lang.reflect.Field, java.lang.String)
	 */
	public boolean setField(Object object, Field field, String value) 
	{
		boolean result	= false;
		try
		{
			field.setChar(object, getValue(value));
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
		  result		= Character.toString(field.getChar(object));
	   } catch (Exception e)
	   {
		  e.printStackTrace();
	   }
	   return result;
	}

/**
 * The default value for this type, as a String.
 * This value is the one that translateToXML(...) wont bother emitting.
 * 
 * In this case, "false".
 */
	public String defaultValue()
	{
	   return " ";
	}
	
    /**
     * Return true if this type may need escaping when emitted as XML.
     * 
     * @return true
     */
    public boolean needsEscaping()
    {
    	return true;
    }

}
