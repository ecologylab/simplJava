/**
 * 
 */
package ecologylab.serialization.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ecologylab.collections.Scope;
import ecologylab.generic.Describable;
import ecologylab.generic.HashMapArrayList;
import ecologylab.generic.ReflectionTools;

/**
 * Basic cross-platform unit for managing Collection and Map types in S.IM.PL Serialization.
 * 
 * @author andruid
 */
public class CollectionType extends SimplType
implements CrossLanguageTypeConstants, Describable
{
	private Class				javaClass;
	
	@simpl_scalar
	private boolean			isMap;

	/**
	 * 
	 */
	public CollectionType()
	{
	}
	
	public CollectionType(Class javaClass, String cSharpName, String objCName)
	{
		super(deriveCrossPlatformName(javaClass), javaClass.getSimpleName(), javaClass.getName(), cSharpName, objCName);
		
		this.javaClass			= javaClass;
		
		this.isMap			= Map.class.isAssignableFrom(javaClass);
		
		TypeRegistry.registerCollectionType(this);
	}

	public Object getInstance()
	{
		return ReflectionTools.getInstance(javaClass);
	}
	
	public Collection getCollection()
	{
		return isMap ? null : (Collection) getInstance();
	}
	
	public Map getMap()
	{
		return isMap ? (Map) getInstance() : null;
	}
	
	/**
	 * The full, qualified name of the class that this describes
	 * 
	 * @return	Full Java class name.
	 */
	@Override
	public String getDescription()
	{
		return getJavaTypeName();
	}

	public Class getJavaClass()
	{
		return javaClass;
	}

	public Object getJavaInstance()
	{
		return ReflectionTools.getInstance(javaClass);
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

}
