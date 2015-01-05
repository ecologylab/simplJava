/**
 * 
 */
package ecologylab.serialization.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import ecologylab.serialization.GenericTypeVar;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;

/**
 * Basic cross-platform unit for managing Collection and Map types in S.IM.PL Serialization.
 * 
 * @author andruid
 */
@simpl_inherit
public class CollectionType<T> extends SimplType
implements CrossLanguageTypeConstants
{
	@simpl_scalar
	private boolean isMap;
	
	/**
	 * Represent the generic type vars that a Collection or Map type is defined with.
	 */
	@simpl_collection("generic_type_var")
	private ArrayList<GenericTypeVar> genericTypeVars;

	/**
	 * Default constructor to satisfy SIMPL
	 */
	public CollectionType()
	{
	}
	
	public CollectionType(Class javaClass, String cSharpName, String objCName)
	{
		super(javaClass, false, cSharpName, objCName, null);
		
		this.isMap			= Map.class.isAssignableFrom(javaClass);	
	}

	public Collection getCollection()
	{
		return isMap ? null : (Collection) getInstance();
	}
	
	public Map getMap()
	{
		return isMap ? (Map) getInstance() : null;
	}

	public boolean isMap()
	{
		return isMap;
	}

	/**
	 * The name to use when declaring a field in C# cross-compilation.
	 * 
	 * @return	cSharpTypeName, if one was passed in explicitly. otherwise, the cSharpTypeName of the default Collection or Map type.
	 */
	@Override
	public String deriveCSharpTypeName()
	{
		String cSharpTypeName	= super.getCSharpTypeName();
		return cSharpTypeName != null ? cSharpTypeName : TypeRegistry.getDefaultCollectionOrMapType(isMap).getCSharpTypeName();
	}
	
	/**
	 * The name to use when declaring a field in Objective C cross-compilation.
	 * 
	 * @return	objectiveCTypeName, if one was passed in explicitly.  otherwise, the objectiveCTypeName of the default Collection or Map type.
	 */
	@Override
	public String deriveObjectiveCTypeName()
	{
		String objectiveCTypeName	= super.getObjectiveCTypeName();
		return objectiveCTypeName != null ? objectiveCTypeName : TypeRegistry.getDefaultCollectionOrMapType(isMap).getObjectiveCTypeName();
	}

	@Override
	public boolean isScalar()
	{
		return false;
	}
}
