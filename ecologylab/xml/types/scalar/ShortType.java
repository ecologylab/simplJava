/*
 * Created on Dec 31, 2004 at the Interface Ecology Lab.
 */
package ecologylab.xml.types.scalar;

import java.io.IOException;
import java.lang.reflect.Field;

import ecologylab.xml.FieldToXMLOptimizations;

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
    public Short getInstance(String value, String[] formatStrings)
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
	@Override
	public String toString(Field field, Object context)
	{
	   String result	= "COULDN'T CONVERT!";
	   try
	   {
		  result		= Short.toString(field.getShort(context));
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
 * In this case, "0".
 */
	protected String defaultValueString()
	{
	   return DEFAULT_VALUE_STRING;
	}

	/**
	 * True if the value in the Field object matches the default value for this type.
	 * 
	 * @param field
	 * @return
	 */
    @Override public boolean isDefaultValue(Field field, Object context) 
    throws IllegalArgumentException, IllegalAccessException
    {
    	return field.getShort(context) == DEFAULT_VALUE;
    }

    /**
     * Get the value from the Field, in the context.
     * Append its value to the buffy.
     * 
     * @param buffy
     * @param field
     * @param context
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    @Override
    public void appendValue(StringBuilder buffy, FieldToXMLOptimizations f2xo, Object context) 
    throws IllegalArgumentException, IllegalAccessException
    {
        short value = f2xo.getField().getShort(context);
           
		buffy.append(value);
    }
    /**
     * Get the value from the Field, in the context.
     * Append its value to the buffy.
     * 
     * @param buffy
     * @param field
     * @param context
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    @Override
    public void appendValue(Appendable buffy, FieldToXMLOptimizations f2xo, Object context) 
    throws IllegalArgumentException, IllegalAccessException, IOException
    {
        short value = f2xo.getField().getShort(context);
           
		buffy.append(Short.toString(value));
    }
}
