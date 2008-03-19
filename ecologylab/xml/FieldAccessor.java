/**
 * 
 */
package ecologylab.xml;

import java.lang.reflect.Field;

import ecologylab.generic.Debug;
import ecologylab.xml.types.scalar.ScalarType;

/**
 * Used to provide convenient access for setting and getting values, using the ecologylab.xml type system.
 * Provides marshalling and unmarshalling from Strings.
 * 
 * @author andruid
 */
public class FieldAccessor extends Debug
{
	final Field				field;
	final ScalarType<?>		scalarType;
	final String			tagName;
	
	public FieldAccessor(Field field, ScalarType<?> scalarType, String tagName)
	{
		this.field		= field;
		this.scalarType	= scalarType;
		this.tagName	= tagName;
	}
	
	/**
	 * 
	 * @return	true if this field represents a ScalarType, not a nested element or collection thereof.
	 */
	public boolean isScalar()
	{
		return scalarType != null;
	}
	
	/**
	 * In the supplied context object, set the *typed* value of the field,
	 * using the valueString passed in. 
	 * Unmarshalling is performed automatically, by the ScalarType already stored in this.
	 * 
	 * @param context			ElementState object to set the Field in this.
	 * 
	 * @param valueString		The value to set, which this method will use with the ScalarType, to create the value that will be set.
	 */
	public void set(ElementState context, String valueString)
	{
		if ((valueString != null) && (context != null))
		{
			if (isScalar())
			{
				scalarType.setField(context, field, valueString);
			}
		}
	}
	
	/**
	 * Get the String representation of the value of the field, in the context object, using the ScalarType.
	 * 
	 * @param context
	 * @return
	 */
	public String getValueString(ElementState context)
	{
		String	result	= null;
		if (context != null)
		{
			if (isScalar())
			{
				result		= scalarType.toString(field, context);
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @return	The Java name of the field.
	 */
	public String getFieldName()
	{
		return field.getName();
	}

	/**
	 * 
	 * @return	The XML tag name of the field.
	 */
	public String getTagName()
	{
		return tagName;
	}

	/**
	 * @return the scalarType of the field
	 */
	public ScalarType<?> getScalarType()
	{
		return scalarType;
	}

	/**
	 * @return the field
	 */
	public Field getField()
	{
		return field;
	}
	
}

