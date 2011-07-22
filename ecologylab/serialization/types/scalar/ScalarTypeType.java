/**
 * 
 */
package ecologylab.serialization.types.scalar;

import java.io.IOException;

import ecologylab.serialization.ScalarUnmarshallingContext;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.types.CrossLanguageTypeConstants;
import ecologylab.serialization.types.ScalarType;
import ecologylab.serialization.types.TypeRegistry;

/**
 * For storing actual ScalarType values as meta-metadata.
 * 
 * @author andruid
 *
 */
public class ScalarTypeType extends ReferenceType<ScalarType>
implements CrossLanguageTypeConstants
{
	/**
	 * @param javaClass
	 */
	public ScalarTypeType()
	{
		super(ScalarType.class, JAVA_SCALAR_TYPE, DOTNET_SCALAR_TYPE, OBJC_SCALAR_TYPE, null);
	}

	/**
	 * Capitalize the value if  it wasn't.
	 * Append "Type".
	 * Use this to call TypeRegistry.getType().
	 */
	@Override
	public ScalarType getInstance(String value, String[] formatStrings, ScalarUnmarshallingContext scalarUnmarshallingContext)
	{
		ScalarType result	= null;
		int length			= value.length();
		if ((value != null) && (length > 0))
		{
			char firstChar	= value.charAt(0);
			StringBuilder buffy	= new StringBuilder(length + 4);	// includes room for "Type"
			if (Character.isLowerCase(firstChar))
			{
				buffy.append(Character.toUpperCase(firstChar));
				if (length > 1)
					buffy.append(value, 1, length - 1);
			}
			else
				buffy.append(value);
//			buffy.append("Type");	//TODO -- verify that this change is correct -- andruid & yin 7/21/2011
			
			result	= TypeRegistry.getScalarTypeBySimpleName(buffy.toString());
		}
		return result;			
	}

	@Override
	public String marshall(ScalarType instance, TranslationContext serializationContext)
	{
		return instance.getJavaClass().getSimpleName();
	}
}
