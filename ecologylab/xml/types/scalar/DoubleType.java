/*
 * Created on Dec 31, 2004 at the Interface Ecology Lab.
 */
package ecologylab.xml.types.scalar;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Type system entry for double, a built-in primitive.
 * 
 * @author andruid
 */
public class DoubleType extends ScalarType<Double>
{
	public static final double	DEFAULT_VALUE			= 0;
	public static final String	DEFAULT_VALUE_STRING	= "0.0";

    /**
     * This constructor should only be called once per session, through a static initializer,
     * typically in TypeRegistry.
     * <p>
     * To get the instance of this type object for use in translations, call
     * <code>TypeRegistry.get("double")</code>.
     * 
     */
	public DoubleType()
    {
        super(double.class);
    }

    /**
     * Convert the parameter to double.
     */
    public double getValue(String valueString)
    {
        return Double.parseDouble(valueString);
    }

    /**
     * Parse the String into the (primitive) type, and return a boxed instance.
     * 
     * @param value
     *            String representation of the instance.
     */
    public Double getInstance(String value)
    {
        return new Double(value);
    }

    /**
     * This is a primitive type, so we set it specially.
     * 
     * @see ecologylab.xml.types.scalar.ScalarType#setField(Object, Field, String)
     */
    public boolean setField(Object object, Field field, String value)
    {
        boolean result = false;
        try
        {
            field.setDouble(object, getValue(value));
            result = true;
        }
        catch (Exception e)
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
        String result = "COULDN'T CONVERT!";
        try
        {
            result = Double.toString(field.getDouble(object));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * The default value for this type, as a String. This value is the one that translateToXML(...)
     * wont bother emitting.
     * 
     * @return "0"
     */
    protected String defaultValueString()
    {
	   return DEFAULT_VALUE_STRING;
    }

    /**
     * Since DoubleType is a floating point value, returns true.
     * @return true
     */
    @Override 
    public boolean isFloatingPoint()
    {
        return true;
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
    	return field.getDouble(context) == DEFAULT_VALUE;
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
    public void appendValue(StringBuilder buffy, Field field, Object context, boolean needsEscaping) 
    throws IllegalArgumentException, IllegalAccessException
    {
        double value = field.getDouble(context);
           
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
    public void appendValue(Appendable buffy, Field field, Object context, boolean needsEscaping) 
    throws IllegalArgumentException, IllegalAccessException, IOException
    {
        double value = field.getDouble(context);
           
		buffy.append(Double.toString(value));
    }
}
