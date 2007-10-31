/*
 * Created on Dec 31, 2004 at the Interface Ecology Lab.
 */
package ecologylab.xml.types.scalar;

import java.lang.reflect.Field;

/**
 * Type system entry for boolean, a built-in primitive.
 * 
 * @author andruid
 */
public class BooleanType extends ScalarType<Boolean>
{
	public static final boolean	DEFAULT_VALUE			= false;
	public static final String	DEFAULT_VALUE_STRING	= "false";

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
    public Boolean getInstance(String value)
    {
        return new Boolean(getValue(value));
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
        boolean value = field.getBoolean(context);
           
		buffy.append(value);
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
