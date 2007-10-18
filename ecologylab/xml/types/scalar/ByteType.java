/*
 * Created on Dec 31, 2004 at the Interface Ecology Lab.
 */
package ecologylab.xml.types.scalar;

import java.lang.reflect.Field;

/**
 * Type system entry for byte, a built-in primitive.
 * 
 * @author andruid
 */
public class ByteType extends ScalarType<Byte>
{
	public static final byte	DEFAULT_VALUE			= 0;
	public static final String	DEFAULT_VALUE_STRING	= "0";

/**
 * This constructor should only be called once per session, through
 * a static initializer, typically in TypeRegistry.
 * <p>
 * To get the instance of this type object for use in translations, call
 * <code>TypeRegistry.get("byte")</code>.
 * 
 */
	public ByteType()
	{
		super(byte.class);
	}

	/**
	 * Convert the parameter to byte.
	 */
	public byte getValue(String valueString)
	{
		return Byte.parseByte(valueString);
	}
	
    /**
     * Parse the String into the (primitive) type, and return a boxed instance.
     * 
     * @param value
     *            String representation of the instance.
     */
    public Byte getInstance(String value)
    {
        return new Byte(value);
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
			field.setByte(object, getValue(value));
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
		  result		= Byte.toString(field.getByte(object));
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
            byte b	= field.getByte(object);
            if (b != DEFAULT_VALUE)
            	buffy.append(b);
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
 * In this case, "false".
 */
	protected String defaultValueString()
	{
	   return DEFAULT_VALUE_STRING;
	}
	
}
