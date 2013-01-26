package simpl.types;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import simpl.platformspecifics.SimplPlatformSpecifics;
import simpl.tools.XMLTools;
import simpl.types.scalar.CompositeAsScalarType;

import ecologylab.generic.Debug;

/**
 * This class implements registries of instances of ScalarType and CollectionType. 
 * <p/>
 * Thus, for example, the key for the type translated by IntType is "int", not "IntType". 
 * (But for the type translated by IntegerType, it is Integer :-)
 * It must be
 * this way, because automatic translation is performed based on Field declarations, and the Field
 * declarations do not know about these Types, only about the underlying Java types.
 */
public class TypeRegistry<ST extends SimplType> extends Debug
implements CrossLanguageTypeConstants
{
	/**
	 * This is now a doubleton class, like a singleton, but there are 2.
	 * One instance for ScalarType, one instance for CollectionType.
	 */
	private static TypeRegistry<ScalarType>					scalarRegistry;
	
	private static TypeRegistry<CollectionType>			collectionRegistry;

	/**
	 * These are by Java full name.
	 */
	private final 	HashMap<String, ST>	typesByJavaName 			= new HashMap<String, ST>();
	
	private final 	HashMap<String, ST>	typesByCrossPlatformName 			= new HashMap<String, ST>();
	
	private final		HashMap<String, ST>	typesBySimpleName			= new HashMap<String, ST>();
	
	private final 	HashMap<String, ST>	typesByCSharpName 		= new HashMap<String, ST>();
	
	private final		HashMap<String, ST>	typesByObjectiveCName	= new HashMap<String, ST>();
	
	private final		HashMap<String, ST>	typesByDbName					= new HashMap<String, ST>();
	
	private CollectionType defaultCollectionType, defaultMapType;
	
	static
	{
		init();
	}
	private static boolean init;
	
	/**
	 * 
	 */
	public static void init()
	{
		if (!init)
		{
			init	= true;
			
			new FundamentalTypes();
			
			SimplPlatformSpecifics.get().initializePlatformSpecificTypes();
		}
	}

	public TypeRegistry()
	{
		
	}

	private static TypeRegistry<ScalarType> scalarRegistry()
	{
		TypeRegistry<ScalarType> result	= scalarRegistry;
		if (result == null)
		{
			synchronized (TypeRegistry.class)
			{
				result			= scalarRegistry;
				if (result == null)
				{
					result		= new TypeRegistry<ScalarType>();
					scalarRegistry	= result;
				}
			}
		}
		return result;
	}

	private static TypeRegistry<CollectionType> collectionRegistry()
	{
		TypeRegistry<CollectionType> result	= collectionRegistry;
		if (result == null)
		{
			synchronized (TypeRegistry.class)
			{
				result			= collectionRegistry;
				if (result == null)
				{
					result		= new TypeRegistry<CollectionType>();
					collectionRegistry	= result;
				}
			}
		}
		return result;
	}
	static boolean registerSimplType(SimplType type)
	{
		TypeRegistry registry	= CollectionType.class.isAssignableFrom(type.getClass()) ? collectionRegistry() : scalarRegistry();
		
		return registry.registerType(type);
	}
	/**
	 * Enter this type in the registry, which is a map in which the Type's Class object's fully
	 * qualified named is used as a key.
	 */
	static boolean registerScalarType(ScalarType type)
	{
		return scalarRegistry().registerType(type);
	}

	private synchronized boolean registerTypeIfNew(ST type)
	{
		String javaTypeName = type.getJavaTypeName();
		return typesByJavaName.containsKey(javaTypeName) ? false : registerType(type);
	}
	private synchronized boolean registerType(ST type)
	{
		String javaTypeName = type.getJavaTypeName();
		typesByJavaName.put(javaTypeName, type);
		
		String crossPlatformName	= type.getName();
		typesByCrossPlatformName.put(crossPlatformName, type);
		
		String cSharpTypeName 		= type.getCSharpTypeName();
		if (cSharpTypeName != null)
			typesByCSharpName.put(cSharpTypeName, type);
		
		String objectiveCTypeName = type.getObjectiveCTypeName();
		if (objectiveCTypeName != null)
			typesByObjectiveCName.put(objectiveCTypeName, type);
		
		String dbTypeName 				= type.getDbTypeName();
		if (dbTypeName != null)
			typesByDbName.put(dbTypeName, type);
		
		String simpleName 	= type.getSimpleName();
		ST previous					= typesBySimpleName.put(simpleName, type);
		boolean definingNewType 		= previous != null && !previous.equals(type);
		if (definingNewType)
		{
			warning("registerType(): Redefining type: " + simpleName);
		}
		return definingNewType;
	}
	/**
	 * Get the Scalar Type corresponding to the Class, by using its name.
	 * 
	 * @param thatClass
	 * @return Type associated with thatClass
	 */
	public static <U> ScalarType<U> getScalarType(Class<U> thatClass)
	{
		if (XMLTools.isEnum(thatClass))
		{
			return scalarRegistry().getTypeByClass(Enum.class);
		}
		else 
		{
			ScalarType<U> result	= scalarRegistry().getTypeByClass(thatClass);
			if (result == null && XMLTools.isComposite(thatClass))
				result							= scalarRegistry().getTypeByClass(CompositeAsScalarType.class);
			
			return result;
		}
	}

	/**
	 * Check to see if we have a Type corresponding to the Class, by using its name.
	 * 
	 * @param thatClass
	 * @return true if thatClass is in this TypeRegistry
	 */
	public static boolean containsScalarType(Class thatClass)
	{
		return scalarRegistry().contains(thatClass);
	}
	
	public static ScalarType getScalarTypeByName(String name)
	{
		return scalarRegistry().getTypeByJavaName(name);
	}
	public static ScalarType getScalarTypeBySimpleName(String simpleName)
	{
		return scalarRegistry().getTypeBySimpleName(simpleName);
	}
	
	ST getTypeBySimpleName(String simpleName)
	{
		return typesBySimpleName.get(simpleName);
	}
	
	boolean contains(Class javaClass)
	{
		return containsByJavaName(javaClass.getName());
	}
	boolean containsByJavaName(String javaName)
	{
		return typesByJavaName.containsKey(javaName);
	}
	ST getTypeByClass(Class<?> javaClass)
	{
		return getTypeByJavaName(javaClass.getName());
	}
	
	ST getTypeByJavaName(String javaName)
	{
		return typesByJavaName.get(javaName);
	}
	
	ST getTypeByCSharpName(String cSharpName)
	{
		return typesByCSharpName.get(cSharpName);
	}
	
	ST getTypeByObjectiveCName(String objectiveCName)
	{
		return typesByObjectiveCName.get(objectiveCName);
	}
	
	ST getTypeByDbName(String dbName)
	{
		return typesByDbName.get(dbName);
	}
	/**
	 * This method is only called by the constructor of CollectionType.
	 * 
	 * @param collectionType
	 */
	static void registerCollectionType(CollectionType collectionType)
	{
		collectionRegistry().registerType(collectionType);
//		TypeRegistry registrySingleton = collectionRegistry();
//		registrySingleton.typesByJavaName.put(collectionType.getName(), collectionType);
//		registrySingleton.typesBySimpleName.put(collectionType.getJavaTypeName(), collectionType);
	}

	/**
	 * Get by unique, cross-platform name.
	 * 
	 * @param crossPlatformName
	 * @return
	 */
	public static CollectionType getCollectionTypeByCrossPlatformName(String crossPlatformName)
	{
		return collectionRegistry().typesByCrossPlatformName.get(crossPlatformName);
	}

	public static CollectionType getCollectionTypeByCSharpName(String cSharpName)
	{
		return collectionRegistry().typesByCSharpName.get(cSharpName);
	}

	public static CollectionType getCollectionTypeByObjectiveCName(String objectiveCName)
	{
		return collectionRegistry().typesByObjectiveCName.get(objectiveCName);
	}

	public static CollectionType getCollectionTypeBySimpleName(String simpleName)
	{
		return collectionRegistry().typesBySimpleName.get(simpleName);
	}

	/**
	 * Lookup a collection type using the Java class or its full unqualifiedName.
	 * 
	 * @param javaField	Declaring class of this field is key for lookup
	 * 
	 * @return
	 */
	public static CollectionType getCollectionType(Field javaField)
	{
		return getCollectionType(javaField.getType());
	}

	/**
	 * Lookup a collection type using the Java class or its full unqualifiedName.
	 * If it does not exist, construct a new CollectionType, but with no capabilities for Cross-Language Code Generation.
	 * 
	 * @param javaClass
	 * @return
	 */
	public static CollectionType getCollectionType(Class javaClass)
	{
		String javaClassName 	= javaClass.getName();
		CollectionType result = getCollectionTypeByJavaName(javaClassName);
		if (result == null)
		{
			if (javaClass.isInterface() || Modifier.isAbstract(javaClass.getModifiers()))
			{
				return Map.class.isAssignableFrom(javaClass) ? collectionRegistry().defaultMapType : collectionRegistry().defaultCollectionType;
			}
			else
			{
				String crossPlatformName	= SimplType.deriveCrossPlatformName(javaClass, false);
				collectionRegistry().warning("No CollectionType was pre-defined for " + crossPlatformName + ", so constructing one on the fly.\nCross-language code for fields defined with this type cannot be generated.");
				result										= new CollectionType(javaClass, null, null);
			}
		}
		return result;
	}
	/**
	 * Lookup a collection type using the Java class or its full unqualifiedName.
	 * 
	 * @param javaClassName
	 * @return
	 */
	public static CollectionType getCollectionTypeByJavaName(String javaClassName)
	{
		return collectionRegistry().typesByJavaName.get(javaClassName);
	}

	public static TypeRegistry typeRegistry()
	{
		return scalarRegistry;
	}
	
	public static void setDefaultCollectionType(CollectionType ct)
	{
		collectionRegistry().defaultCollectionType	= ct;
	}
	
	public static void setDefaultMapType(CollectionType ct)
	{
		collectionRegistry().defaultMapType	= ct;
	}
	
	static CollectionType getDefaultCollectionType()
	{
		return collectionRegistry().defaultCollectionType;
	}

	static CollectionType getDefaultMapType()
	{
		return collectionRegistry().defaultMapType;
	}

	static CollectionType getDefaultCollectionOrMapType(boolean isMap)
	{
		return isMap ? getDefaultMapType() : getDefaultCollectionType();
	}


}
