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
 * Type system entry for char, a built-in primitive.
 * 
 * @author andruid
 */
@simpl_inherit
public class CharType extends ScalarType<Character> implements CrossLanguageTypeConstants
{
	public static final char		DEFAULT_VALUE					= ' ';

	public static final String	DEFAULT_VALUE_STRING	= " ";

	/**
	 * This constructor should only be called once per session, through a static initializer,
	 * typically in TypeRegistry.
	 * <p>
	 * To get the instance of this type object for use in translations, call
	 * <code>TypeRegistry.get("char")</code>.
	 * 
	 */
	public CharType()
	{
		super(char.class, DOTNET_CHAR, OBJC_CHAR, null);
	}

	/**
	 * Convert the parameter to char.
	 */
	public char getValue(String valueString)
	{
		return valueString.charAt(0);
	}

	/**
	 * Parse the String into the (primitive) type, and return a boxed instance.
	 * 
	 * @param value
	 *          String representation of the instance.
	 */
	public Character getInstance(String value, String[] formatStrings,
			ScalarUnmarshallingContext scalarUnmarshallingContext)
	{
		return getValue(value);
	}

	/**
	 * This is a primitive type, so we set it specially.
	 * 
	 * @see ecologylab.serialization.types.ScalarType#setField(java.lang.Object,
	 *      java.lang.reflect.Field, java.lang.String)
	 */
	public boolean setField(Object object, Field field, String value)
	{
		boolean result = false;
		try
		{
			field.setChar(object, getValue(value));
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
	public String toString(Field field, Object object)
	{
		String result = "COULDN'T CONVERT!";
		try
		{
			result = Character.toString(field.getChar(object));
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
	 * In this case, "false".
	 */
	public String defaultValueString()
	{
		return DEFAULT_VALUE_STRING;
	}

	@Override
	public Character defaultValue()
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
		return field.getChar(context) == DEFAULT_VALUE;
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
		char value = f2xo.getField().getChar(context);

		buffy.append(value);
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
			TranslationContext serializationContext, FORMAT format) throws IllegalArgumentException,
			IllegalAccessException, IOException
	{
		char value = fieldDescriptor.getField().getChar(context);

		buffy.append(Character.toString(value));
	}
}
