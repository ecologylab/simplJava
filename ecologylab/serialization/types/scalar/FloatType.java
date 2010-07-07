/*
 * Created on Dec 31, 2004 at the Interface Ecology Lab.
 */
package ecologylab.serialization.types.scalar;

import java.io.IOException;
import java.lang.reflect.Field;

import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.ScalarUnmarshallingContext;

/**
 * Type system entry for float, a built-in primitive.
 * 
 * @author andruid
 */
public class FloatType extends ScalarType<Float>
{
	public static final float		DEFAULT_VALUE					= 0;

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

	public FloatType(Class<Float> thatClass)
	{
		super(thatClass);
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
	 *          String representation of the instance.
	 */
	public Float getInstance(String value, String[] formatStrings,
			ScalarUnmarshallingContext scalarUnmarshallingContext)
	{
		return new Float(getValue(value));
	}

	/**
	 * This is a primitive type, so we set it specially.
	 * 
	 * @see ecologylab.serialization.types.scalar.ScalarType#setField(Object, Field, String)
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
	@Override
	public String toString(Field field, Object context)
	{
		String result = "COULDN'T CONVERT!";
		try
		{
			result = Float.toString(field.getFloat(context));
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
	 * Since FloatType is a floating point value, returns true.
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
		return (Float) field.get(context) == DEFAULT_VALUE;
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
		float value = (Float) f2xo.getField().get(context);

		buffy.append(value);
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
	public void appendValue(Appendable buffy, FieldDescriptor fieldDescriptor, Object context)
			throws IllegalArgumentException, IllegalAccessException, IOException
	{
		float value = (Float) fieldDescriptor.getField().get(context);

		buffy.append(Float.toString(value));
	}

	@Override
	public String getCSharptType()
	{
		return MappingConstants.DOTNET_FLOAT;
	}

	@Override
	public String getDbType()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getObjectiveCType()
	{
		return MappingConstants.OBJC_FLOAT;
	}
}
