/**
 * 
 */
package ecologylab.serialization.types.scalar;

import java.awt.Color;
import java.lang.reflect.Field;

import ecologylab.serialization.ScalarUnmarshallingContext;

/**
 * For marshalling the name of a field, for transmission to other platforms.
 * 
 * @author andruid
 */
public class FieldType extends ReferenceType<Field>
{

	public FieldType()
	{
		super(Field.class);
	}

	/**
	 * In Java it is not possible to create instances of Field.
	 */
	@Override
	public Field getInstance(String value, String[] formatStrings,
			ScalarUnmarshallingContext scalarUnmarshallingContext)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Used to describe scalar types used for serializing the type system, itself.
	 * They cannot be unmarshalled in Java, only marshalled.
	 * Code may be written to access their String representations in other languages.
	 * 
	 * @return	true, because this type cannot be unmarshalled in Java.
	 */
	public boolean isMarshallOnly()
	{
		return true;
	}

	/**
	 * The string representation for a Field of the type Field, used for marshalling.
	 */
		@Override
		public String marshall(Field instance)
		{
			return instance.getName();
		}

	@Override
	public String getCSharptType()
	{
		return MappingConstants.DOTNET_FIELD;
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
		return MappingConstants.OBJC_FIELD;
	}

}
