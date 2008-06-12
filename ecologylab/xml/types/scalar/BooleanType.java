/*
 * Created on Dec 31, 2004 at the Interface Ecology Lab.
 */
package ecologylab.xml.types.scalar;

import java.io.IOException;
import java.lang.reflect.Field;

import ecologylab.xml.FieldToXMLOptimizations;

/**
 * Type system entry for boolean, a built-in primitive.
 * 
 * @author andruid
 */
public class BooleanType extends ScalarType<Boolean>
{
	public static final boolean	DEFAULT_VALUE			= false;

	public static final String		DEFAULT_VALUE_STRING	= "false";

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
     * If <code>this</code> is a reference type, build an appropriate Object, given a String
     * representation. If it is a primitive type, return a boxed value.
     * 
     * @param value
     *            String representation of the instance.
     */
    @Override public Boolean getInstance(String value, String[] formatStrings)
    {
        return new Boolean(getValue(value));
    }

	/**
	 * This is a primitive type, so we set it specially.
	 * 
	 * @see ecologylab.xml.types.scalar.ScalarType#setField(java.lang.Object, java.lang.reflect.Field, java.lang.String)
	 */
	@Override public boolean setField(Object object, Field field, String value) 
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
	@Override
	public String toString(Field field, Object object)
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
        boolean value = f2xo.getField().getBoolean(context);
           
		buffy.append(value);
    }
    static final String TRUE	= "true";
    static final String FALSE	= "false";
    
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
        boolean value = f2xo.getField().getBoolean(context);
           
		buffy.append(value ? TRUE : FALSE);
    }
    

/**
 * The default value for this type, as a String.
 * This value is the one that translateToXML(...) wont bother emitting.
 * 
 * @return		"false"
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
	@Override
	public boolean isDefaultValue(Field field, Object context) 
    throws IllegalArgumentException, IllegalAccessException
    {
    	return field.getBoolean(context) == DEFAULT_VALUE;
    }

}
