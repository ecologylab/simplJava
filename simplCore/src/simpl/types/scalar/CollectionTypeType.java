/**
 * 
 */
package simpl.types.scalar;

import simpl.annotations.dbal.simpl_inherit;
import simpl.core.ScalarUnmarshallingContext;
import simpl.core.TranslationContext;
import simpl.types.CollectionType;
import simpl.types.TypeRegistry;

/**
 * @author andruid
 *
 */
@simpl_inherit
public class CollectionTypeType extends ReferenceType<CollectionType>
{
	public CollectionTypeType()
	{
		super(CollectionType.class, JAVA_SCALAR_TYPE, DOTNET_SCALAR_TYPE, OBJC_SCALAR_TYPE, null);
	}

	/**
	 * Capitalize the value if  it wasn't.
	 * Append "Type".
	 * Use this to call TypeRegistry.getType().
	 */
	@Override
	public CollectionType getInstance(String value, String[] formatStrings, ScalarUnmarshallingContext scalarUnmarshallingContext)
	{
		CollectionType result	= TypeRegistry.getCollectionTypeByCrossPlatformName(value);
		
		return result;			
	}

	@Override
	public String marshall(CollectionType instance, TranslationContext serializationContext)
	{
		String crossPlatformName = instance.getName();
		return crossPlatformName;
	}

}
