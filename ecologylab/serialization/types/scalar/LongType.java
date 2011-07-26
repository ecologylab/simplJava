/*
 * Created on Dec 31, 2004 at the Interface Ecology Lab.
 */
package ecologylab.serialization.types.scalar;

import java.io.IOException;
import java.lang.reflect.Field;

import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.ScalarUnmarshallingContext;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.simpl_inherit;
import ecologylab.serialization.types.CrossLanguageTypeConstants;
import ecologylab.serialization.types.ScalarType;

/**
 * Type system entry for long, a built-in primitive.
 * 
 * @author andruid
 */
@simpl_inherit
public class LongType extends ScalarType<Long> implements CrossLanguageTypeConstants
{
	public static final long		DEFAULT_VALUE					= 0;

	public static final String	DEFAULT_VALUE_STRING	= "0";

	public LongType()
	{
		this(long.class);
	}

	public LongType(Class<Long> thatClass)
	{
		super(thatClass, DOTNET_LONG, OBJC_LONG, null);
	}

	/**
	 * Convert the parameter to long.
	 */
	public long getValue(String valueString)
	{
		return Long.parseLong(valueString);
	}

	/**
	 * Parse the String into the (primitive) type, and return a boxed instance.
	 * 
	 * @param value
	 *          String representation of the instance.
	 */
	public Long getInstance(String value, String[] formatStrings,
			ScalarUnmarshallingContext scalarUnmarshallingContext)
	{
		return "null".equalsIgnoreCase(value) ? null : new Long(value);
	}

	/**
	 * This is a primitive type, so we set it specially.
	 * 
	 * @see ecologylab.serialization.types.ScalarType#setField(Object, Field, String)
	 */
	public boolean setField(Object object, Field field, String value)
	{
		boolean result = false;
		try
		{
			field.setLong(object, getValue(value));
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
			result = Long.toString(field.getLong(context));
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
	 * In this case, "0".
	 */
	public String defaultValueString()
	{
		return DEFAULT_VALUE_STRING;
	}

	@Override
	public Long defaultValue()
	{
		return DEFAULT_VALUE;
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
		return (Long) field.get(context) == DEFAULT_VALUE;
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
	 */
	@Override
	public void appendValue(Appendable buffy, FieldDescriptor fieldDescriptor, Object context,
			TranslationContext serializationContext) throws IllegalArgumentException,
			IllegalAccessException, IOException
	{
		buffy.append(getValueToAppend(fieldDescriptor, context));
	}

	public static String getValueToAppend(FieldDescriptor fieldDescriptor, Object context)
			throws IllegalArgumentException, IllegalAccessException
	{
		String result = getNullStringIfNull(fieldDescriptor, context);
		if (result == null)
		{
			result = fieldDescriptor.getField().get(context).toString();
		}
		return result;
	}

}
