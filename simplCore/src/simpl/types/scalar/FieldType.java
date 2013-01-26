/**
 * 
 */
package simpl.types.scalar;

import java.lang.reflect.Field;

import simpl.annotations.dbal.simpl_inherit;
import simpl.types.CrossLanguageTypeConstants;

import ecologylab.serialization.ScalarUnmarshallingContext;
import ecologylab.serialization.TranslationContext;

/**
 * For marshalling the name of a field, for transmission to other platforms.
 * 
 * @author andruid
 */
@simpl_inherit
public class FieldType extends ReferenceType<Field>
implements CrossLanguageTypeConstants
{

	public FieldType()
	{
		super(Field.class, JAVA_FIELD, DOTNET_FIELD, OBJC_FIELD, null);
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
	@Override
	public boolean isMarshallOnly()
	{
		return true;
	}

	/**
	 * The string representation for a Field of the type Field, used for marshalling.
	 */
		@Override
		public String marshall(Field instance, TranslationContext serializationContext)
		{
			return instance.getName();
		}


}
