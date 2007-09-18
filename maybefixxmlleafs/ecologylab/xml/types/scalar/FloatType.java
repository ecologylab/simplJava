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
     * The default value for this type, as a String. This value is the one that translateToXML(...)
     * wont bother emitting.
     * 
     * @return "0"
     */
    public String defaultValue()
    {
        return "0.0";
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
