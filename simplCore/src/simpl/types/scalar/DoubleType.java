/*
 * Created on Dec 31, 2004 at the Interface Ecology Lab.
 */
package simpl.types.scalar;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

import simpl.annotations.dbal.simpl_inherit;
import simpl.descriptions.FieldDescriptor;
import simpl.types.CrossLanguageTypeConstants;
import simpl.types.ScalarType;

import ecologylab.generic.text.EfficientDecimalFormat;
import ecologylab.serialization.ScalarUnmarshallingContext;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.formatenums.Format;

/**
 * Type system entry for double, a built-in primitive.
 * 
 * @author andruid
 */
@simpl_inherit
public class DoubleType extends ScalarType<Double> implements CrossLanguageTypeConstants
{
	public static final double																		DEFAULT_VALUE					= 0;

	public static final String																		DEFAULT_VALUE_STRING	= "0.0";

	/** The map of format Strings to their associated decimal formats. */
	private static final HashMap<String, EfficientDecimalFormat>	formatMap							= new HashMap<String, EfficientDecimalFormat>();

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
		this(double.class);
	}

	public DoubleType(Class<Double> thatClass)
	{
		super(thatClass, DOTNET_DOUBLE, OBJC_DOUBLE, null);
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
	 *          String representation of the instance.
	 */
	@Override
	public Double getInstance(String value, String[] formatStrings,
			ScalarUnmarshallingContext scalarUnmarshallingContext)
	{
		return "null".equalsIgnoreCase(value) ? null : (value != null && value.contains("/")) ?  rationalToDouble(value) : new Double(value);
	}
	
	public static double rationalToDouble(String rationalString)
	{
		String[] stringD	= rationalString.split("/", 2);
		double d0 				= Double.parseDouble(stringD[0]);
		double d1					= Double.parseDouble(stringD[1]);
		
		return d0 / d1;
	}

	/**
	 * This is a primitive type, so we set it specially.
	 * 
	 * @see simpl.types.ScalarType#setField(Object, Field, String)
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
			result = Double.toString((Double) field.get(context));
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
	@Override
	public String defaultValueString()
	{
		return DEFAULT_VALUE_STRING;
	}

	/**
	 * Since DoubleType is a floating point value, returns true.
	 * 
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
	@Override
	public boolean isDefaultValue(Field field, Object context) throws IllegalArgumentException,
			IllegalAccessException
	{
		return (Double) field.get(context) == DEFAULT_VALUE;
	}

	/**
	 * Get the value from the Field, in the context. Append its value to the buffy.
	 * 
	 * @param buffy
	 * @param field
	 * @param context
	 * @param formatStrings
	 *          an array of 0 or 1 items containing a pattern String that specifies how the value will
	 *          be emitted. Patterns are specified using {@link java.text.DecimalFormat}.
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @see java.text.DecimalFormat
	 */
	@Override
	public void appendValue(StringBuilder buffy, FieldDescriptor f2xo, Object context)
			throws IllegalArgumentException, IllegalAccessException
	{
		buffy.append(getValueToAppend(f2xo, context));
	}

	/**
	 * Get the value from the Field, in the context. Append its value to the buffy.
	 * 
	 * @param buffy
	 * @param context
	 * @param field
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	@Override
	public void appendValue(Appendable buffy, FieldDescriptor fieldDescriptor, Object context,
			TranslationContext serializationContext, Format format) throws IllegalArgumentException,
			IllegalAccessException, IOException
	{
		buffy.append(getValueToAppend(fieldDescriptor, context));
	}

	public static String getValueToAppend(FieldDescriptor fieldDescriptor, Object context)
			throws IllegalArgumentException, IllegalAccessException
	{
  	if (fieldDescriptor.getField() == null || fieldDescriptor.getField().get(context) == null)
  		return "null";

  	double value = (Double) fieldDescriptor.getField().get(context);
		String[] formatStrings = fieldDescriptor.getFormat();
		StringBuilder res = new StringBuilder();

		if (formatStrings != null)
		{
			EfficientDecimalFormat decFormat = getFormat(formatStrings[0]);

			try
			{
				decFormat.format(value, res);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			res.append(Double.toString(value));
		}
		return res.toString();
	}

	private static EfficientDecimalFormat getFormat(String formatString)
	{
		EfficientDecimalFormat decFormat = formatMap.get(formatString);

		if (decFormat == null)
		{
			synchronized (formatMap)
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
