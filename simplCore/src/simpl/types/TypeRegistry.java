package simpl.types;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import simpl.descriptions.FieldCategorizer;
import simpl.platformspecifics.SimplPlatformSpecifics;
import simpl.types.ScalarTypeIndexer.InnerIndexingShortcut;

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
public class TypeRegistry
{
	
	public ScalarTypeIndexer scalarTypes;
		
	public TypeRegistry()
	{
		this.scalarTypes = new ScalarTypeIndexer();
	}
	
	
	public static InnerIndexingShortcut by;
	
	static
	{
		init();
	}
	
	
	private static TypeRegistry staticRegistry;
	
	private static boolean init;
	
	/**
	 * 
	 */
	public static void init()
	{
		if (!init)
		{
			staticRegistry = new TypeRegistry();

			new FundamentalTypes();
			
			SimplPlatformSpecifics.get().initializePlatformSpecificTypes();

			init	= true;	
		}
	}
	

	public static void register(ScalarType thatType)
	{
		staticRegistry.scalarTypes.Insert(thatType);
	}
	
	
	/**
	 * Get the Scalar Type corresponding to the Class, by using its name.
	 * 
	 * @param thatClass
	 * @return Type associated with thatClass
	 */
	public static ScalarType getScalarType(Class<?> thatClass)
	{
		return staticRegistry.scalarTypes.get(thatClass);
	}

	public static boolean containsScalarTypeFor(Class<?> collectionElementClass) {
		// TODO Auto-generated method stub
		return staticRegistry.scalarTypes.contains(collectionElementClass);
	}
	
	public static ScalarType getScalarType(String tagName)
	{
		return staticRegistry.scalarTypes.by.scalarTypeName.get(tagName);
	}
}
