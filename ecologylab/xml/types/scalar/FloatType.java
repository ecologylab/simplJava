/*
 * Created on Dec 31, 2004 at the Interface Ecology Lab.
 */
package ecologylab.xml.types.scalar;

import java.lang.reflect.Field;

/**
 * Type system entry for float, a built-in primitive.
 * 
 * @author andruid
 */
public class FloatType extends ScalarType<Float>
{
	public static final float	DEFAULT_VALUE			= 0;
	public static final String	DEFAULT_VALUE_STRING	= "0.0";

    /**
     * This constructor should only be called once per session, through a static initializer,
     * typically in TypeRegistry.
     * <p>
     * To get the instance of this type object for use in translations, call
     * <code>TypeRegistry.get("float")</code>.
     * 
     */
	public FloatType()
    {
        super(float.class);
    }

    /**
     * Convert the parameter to float.
     */
    public float getValue(String valueString)
    {
        return Float.parseFloat(valueString);
    }

    /**
     * If <code>this</code> is a reference type, build an appropriate Object, given a String
     * representation. If it is a primitive type, return a boxed value.
     * 
     * @param value
     *            String representation of the instance.
     */
    public Float getInstance(String value)
    {
        return new Float(getValue(value));
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
            field.setFloat(object, getValue(value));
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
            result = Float.toString(field.getFloat(object));
        }
        catch (Exception e)
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
            float f	= field.getFloat(object);
            if (f != DEFAULT_VALUE)
            	buffy.append(f);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
     * Since FloatType is a floating point value, returns true.
     * @return true
     */
    public boolean isFloatingPoint()
    {
        return true;
    }
}
