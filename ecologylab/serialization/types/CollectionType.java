/**
 * 
 */
package ecologylab.serialization.types;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ecologylab.collections.Scope;
import ecologylab.generic.HashMapArrayList;
import ecologylab.generic.ReflectionTools;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.types.scalar.MappingConstants;

/**
 * Basic cross-platform unit for managing Collection and Map types in S.IM.PL Serialization.
 * 
 * @author andruid
 */
public class CollectionType extends ElementState
implements MappingConstants
{
	/**
	 * This is a platform-independent identifier that S.IM.PL uses for the CollectionType.
	 */
	@simpl_scalar
	String			name;
	
	@simpl_scalar
	String			javaName;
	
	String			javaSimpleName;
	
	Class				javaClass;
	
	@simpl_scalar
	boolean			isMap;
	
	@simpl_scalar
	String			cSharpName;
	
	@simpl_scalar
	String			objCName;

	private static final  HashMap<String, CollectionType>	mapByName 			= new HashMap<String, CollectionType>();
	
	private static final  HashMap<String, CollectionType>	mapByClassName	= new HashMap<String, CollectionType>();
	
	public static final CollectionType	ARRAYLIST_TYPE	= new CollectionType(JAVA_ARRAYLIST, ArrayList.class, DOTNET_ARRAYLIST, OBJC_ARRAYLIST, false);

	public static final CollectionType	HASHMAP_TYPE	= new CollectionType(JAVA_HASHMAP, HashMap.class, DOTNET_HASHMAP, OBJC_HASHMAP, true);

	public static final CollectionType	HASHMAPARRAYLIST_TYPE	= new CollectionType(JAVA_HASHMAPARRAYLIST, HashMapArrayList.class, DOTNET_HASHMAPARRAYLIST, OBJC_HASHMAPARRAYLIST, true);

	public static final CollectionType	SCOPE_TYPE	= new CollectionType(JAVA_SCOPE, Scope.class, DOTNET_SCOPE, OBJC_SCOPE, true);
	
	/**
	 * 
	 */
	public CollectionType()
	{
	}
	
	public CollectionType(String name, Class javaClass, String cSharpName, String objCName, boolean isMap)
	{
		this.name				= name;
		this.javaClass	= javaClass;
		this.javaName		= javaClass.getName();
		this.javaSimpleName	= javaClass.getSimpleName();
		
		this.cSharpName	= cSharpName;
		this.objCName		= objCName;
		this.isMap			= isMap;
		
		mapByName.put(name, this);
		mapByClassName.put(javaName, this);
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
	
	public static CollectionType getTypeByName(String javaName)
	{
		return mapByName.get(javaName);
	}
	/**
	 * Lookup a collection type using the Java class or its full unqualifiedName.
	 * 
	 * @param javaField	Declaring class of this field is key for lookup
	 * 
	 * @return
	 */
	public static CollectionType getType(Field javaField)
	{
		return getType(javaField.getType());
	}
	/**
	 * Lookup a collection type using the Java class or its full unqualifiedName.
	 * @param javaClass
	 * @return
	 */
	public static CollectionType getType(Class javaClass)
	{
		return getType(javaClass.getName());
	}
	/**
	 * Lookup a collection type using the Java class or its full unqualifiedName.
	 * 
	 * @param javaClassName
	 * @return
	 */
	public static CollectionType getType(String javaClassName)
	{
		return mapByClassName.get(javaClassName);
	}
	
	
	public String getName()
	{
		return name;
	}

	public String getJavaName()
	{
		return javaName;
	}

	public String getJavaSimpleName()
	{
		return javaSimpleName;
	}

	public Class getJavaClass()
	{
		return javaClass;
	}

	public boolean isMap()
	{
		return isMap;
	}

	public String getcSharpName()
	{
		return cSharpName;
	}

	public String getObjCName()
	{
		return objCName;
	}

}
