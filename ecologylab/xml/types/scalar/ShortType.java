/*
 * Created on Dec 31, 2004 at the Interface Ecology Lab.
 */
package ecologylab.xml.types.scalar;

import java.lang.reflect.Field;

/**
 * Type system entry for short, a built-in primitive.
 * 
 * @author andruid
 */
public class ShortType extends ScalarType<Short>
{
	public static final short	DEFAULT_VALUE			= 0;
	public static final String	DEFAULT_VALUE_STRING	= "0";

/**
 * This constructor should only be called once per session, through
 * a static initializer, typically in TypeRegistry.
 * <p>
 * To get the instance of this type object for use in translations, call
 * <code>TypeRegistry.get("short")</code>.
 * 
 */
	public ShortType()
	{
		super(short.class);
	}

	/**
	 * Convert the parameter to short.
	 */
	public short getValue(String valueString)
	{
		return Short.parseShort(valueString);
	}
	
    /**
     * Parse the String into the (primitive) type, and return a boxed instance.
     * 
     * @param value
     *            String representation of the instance.
     */
    public Short getInstance(String value)
    {
        return new Short(value);
    }

	/**
	 * This is a primitive type, so we set it specially.
	 * 
	 * @see ecologylab.xml.types.scalar.ScalarType#setField(Object, Field, String)
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
		  result		= Short.toString(field.getShort(object));
	   } catch (Exception e)
	   {
		  e.printStackTrace();
	   }
	   return result;
	}

    /**
     * Copy a string representation for a Field of this type into the StringBuilder, unless
     * the value of the Field in the Object turns out to be the default value for this ScalarType,
     * in which case, do nothing.
     */
	@Override public void copyValue(StringBuilder buffy, Object object, Field field)
    {
        try
        {
            short i	= field.getShort(object);
            if (i != DEFAULT_VALUE)
            	buffy.append(i);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

/**
 * The default value for this type, as a String.
 * This value is the one that translateToXML(...) wont bother emitting.
 * 
 * In this case, "0".
 */
	protected String defaultValueString()
	{
	   return DEFAULT_VALUE_STRING;
	}
}
