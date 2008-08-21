/*
 * Created on Dec 31, 2004 at the Interface Ecology Lab.
 */
package ecologylab.xml.types.scalar;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.HashMap;

import ecologylab.generic.text.EfficientDecimalFormat;
import ecologylab.xml.FieldToXMLOptimizations;
import ecologylab.xml.ScalarUnmarshallingContext;

/**
 * Type system entry for double, a built-in primitive.
 * 
 * @author andruid
 */
public class DoubleType extends ScalarType<Double>
{
	public static final double													DEFAULT_VALUE			= 0;

	public static final String													DEFAULT_VALUE_STRING	= "0.0";

	/** The map of format Strings to their associated decimal formats. */
	private static final HashMap<String, EfficientDecimalFormat>	formatMap				= new HashMap<String, EfficientDecimalFormat>();

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
    @Override public Double getInstance(String value, String[] formatStrings, ScalarUnmarshallingContext scalarUnmarshallingContext)
    {
        return new Double(value);
    }

    /**
     * This is a primitive type, so we set it specially.
     * 
     * @see ecologylab.xml.types.scalar.ScalarType#setField(Object, Field, String)
     */
    @Override
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
	@Override
    public String toString(Field field, Object context)
    {
        String result = "COULDN'T CONVERT!";
        try
        {
            result = Double.toString(field.getDouble(context));
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
    @Override protected String defaultValueString()
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
		 * Get the value from the Field, in the context. Append its value to the
		 * buffy.
		 * 
		 * @param buffy
		 * @param field
		 * @param context
		 * @param formatStrings an array of 0 or 1 items containing a pattern String that specifies how the value will be emitted. Patterns are specified using {@link java.text.DecimalFormat}.
		 * @throws IllegalAccessException
		 * @throws IllegalArgumentException
		 * @see java.text.DecimalFormat
		 */
	@Override public void appendValue(StringBuilder buffy, FieldToXMLOptimizations f2xo, Object context)
			throws IllegalArgumentException, IllegalAccessException
	{
		double value = f2xo.getField().getDouble(context);
		String[] formatStrings = f2xo.getFormat();
		
		if (formatStrings != null)
		{
			EfficientDecimalFormat decFormat = getFormat(formatStrings[0]);
			
			try
			{
				decFormat.format(value, buffy);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			buffy.append(value);
		}
	}

	/**
	 * Get the value from the Field, in the context. Append its value to the buffy.
	 * 
	 * @param buffy
	 * @param field
	 * @param context
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@Override public void appendValue(Appendable buffy, FieldToXMLOptimizations f2xo, Object context)
			throws IllegalArgumentException, IllegalAccessException, IOException
	{
		double value = f2xo.getField().getDouble(context);
		String[] formatStrings = f2xo.getFormat();
		
		if (formatStrings != null)
		{
			EfficientDecimalFormat decFormat = getFormat(formatStrings[0]);
			
			decFormat.format(value, buffy);
		}
		else
		{
			buffy.append(Double.toString(value));
		}
	}
	
	private static EfficientDecimalFormat getFormat(String formatString)
	{
		EfficientDecimalFormat decFormat = formatMap.get(formatString);
		
		if (decFormat == null)
		{
			synchronized(formatMap)
			{
				decFormat = formatMap.get(formatString);
				
				if (decFormat == null)
				{
					decFormat = new EfficientDecimalFormat(formatString);
					formatMap.put(formatString, decFormat);
				}
			}
		}
		
		return decFormat;
	}
}
