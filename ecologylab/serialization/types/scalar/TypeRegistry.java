package ecologylab.serialization.types.scalar;

import java.lang.reflect.Field;
import java.util.HashMap;

import ecologylab.generic.Debug;
import ecologylab.generic.ReflectionTools;
import ecologylab.serialization.XMLTools;

/**
 * This class implements a registry of instances of Type. Each key in this registry is the name of
 * the Java type that underlies the Type, and not the name of the subclass of Type, itself.
 * <p/>
 * Thus, for example, the key for the type translated by IntType is "int", not "IntType". It must be
 * this way, because automatic translation is performed based on Field declarations, and the Field
 * declarations do not know about these Types, only about the underlying Java types.
 */
public class TypeRegistry extends Debug
{
	/**
	 * Maps Strings that represent classes to integers. These integers, some of which are defined in
	 * the interface {@link BuiltinTypeIndices Types}. Type to integer mappings that are defined in
	 * this class use positive integers. All other extended types should use negative integers.
	 */
	private static final HashMap<String, ScalarType>	allTypes		= new HashMap<String, ScalarType>(
																																		32);

	static Class[]																		BASIC_TYPES	=
																																{ StringType.class,
			StringBuilderType.class, IntType.class, ReferenceIntegerType.class, BooleanType.class,
			ReferenceBooleanType.class, FloatType.class, ReferenceFloatType.class, DoubleType.class,
			ReferenceDoubleType.class, LongType.class, ReferenceLongType.class, ShortType.class,
			ByteType.class, CharType.class, ColorType.class, URLType.class, ParsedURLType.class,
			FileType.class, UUIDType.class, RectangleType.class,

			DateType.class,

			ScalarTypeType.class,

			PatternType.class, EnumeratedType.class,

			ClassType.class, FieldType.class,											
			
			//This scalar type is not used anywhere as of now. see class commemts.
			CompositeAsScalarType.class
																																};

	static
	{
		register(BASIC_TYPES);
	}

	/**
	 * Enter this type in the registry, which is a map in which the Type's Class object's fully
	 * qualified named is used as a key.
	 */
	public static boolean register(Class<? extends ScalarType> typeClass)
	{
		ScalarType type = (ScalarType) ReflectionTools.getInstance(typeClass);
		if (type == null)
		{
			error(typeClass, "Can't register this Type, because we can't instantiate it!");
			return false;
		}
		return register(type);
	}

	/**
	 * Enter this type in the registry, which is a map in which the Type's Class object's fully
	 * qualified named is used as a key.
	 */
	static boolean register(ScalarType type)
	{
		String typeName = type.getTypeClass().getName();
		String simpleName = type.getClass().getSimpleName();
		register(simpleName, type);
		return register(typeName, type);
	}

	private static boolean register(String typeName, ScalarType type)
	{
		boolean typeContained;

		synchronized (allTypes)
		{
			typeContained = allTypes.containsKey(typeName);
			if (typeContained)
			{
				Debug.warning(TypeRegistry.class, "register(): Redefining scalar type: " + typeName);
			}
			allTypes.put(typeName, type);
		}
		return typeContained;
	}

	/**
	 * Register a batch of Types.
	 * 
	 * @param thoseTypeClasses
	 */
	public static void register(Class<? extends ScalarType> thoseTypeClasses[])
	{
		int size = thoseTypeClasses.length;
		for (int i = 0; i < size; i++)
			register(thoseTypeClasses[i]);
	}

	/**
	 * Get the Type corresponding to the Field, by using the Field's Class.
	 * 
	 * @param field
	 * @return Type associated with the class of the specified Field
	 */
	public static ScalarType getType(Field field)
	{
		return getType(field.getType(), field);
	}

	/**
	 * Get the Scalar Type corresponding to the Class, by using its name.
	 * 
	 * @param thatClass
	 * @return Type associated with thatClass
	 */
	public static <U> ScalarType<U> getType(Class<U> thatClass)
	{
		return getType(thatClass, null);
	}

	public static <U> ScalarType<U> getType(Class<U> thatClass, Field field)
	{
		if (field == null)
		{
			return (XMLTools.isEnum(thatClass)) ? getType(Enum.class.getName()) : getType(thatClass
					.getName());
		}
		else if (XMLTools.isComposite(thatClass))
		{
			return getType(CompositeAsScalarType.class.getName());
		}
		else
		{	
			return (XMLTools.isEnum(thatClass)) ? getType(Enum.class.getName()) : getType(thatClass
					.getName());
		}
	}

	/**
	 * Get the Scalar Type corresponding to the Class name.
	 * 
	 * @param className
	 * @return Type associated with class of the specified name
	 */
	public static final ScalarType getType(String className)
	{
		return allTypes.get(className);
	}

	/**
	 * Check to see if we have a Type corresponding to the Class, by using its name.
	 * 
	 * @param thatClass
	 * @return true if thatClass is in this TypeRegistry
	 */
	public static boolean contains(Class thatClass)
	{
		return contains(thatClass.getName());
	}

	/**
	 * Check to see if we have a Type corresponding to the Class name.
	 * 
	 * @param className
	 * @return true if a class with this name is in this TypeRegistry
	 */
	public static boolean contains(String className)
	{
		return allTypes.containsKey(className);
	}
}
