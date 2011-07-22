package ecologylab.serialization.types;

import java.util.ArrayList;
import java.util.HashMap;

import ecologylab.collections.Scope;
import ecologylab.generic.HashMapArrayList;
import ecologylab.serialization.types.scalar.BooleanType;
import ecologylab.serialization.types.scalar.ByteType;
import ecologylab.serialization.types.scalar.CharType;
import ecologylab.serialization.types.scalar.ClassType;
import ecologylab.serialization.types.scalar.CollectionTypeType;
import ecologylab.serialization.types.scalar.ColorType;
import ecologylab.serialization.types.scalar.CompositeAsScalarType;
import ecologylab.serialization.types.scalar.DateType;
import ecologylab.serialization.types.scalar.DoubleType;
import ecologylab.serialization.types.scalar.EnumeratedType;
import ecologylab.serialization.types.scalar.FieldType;
import ecologylab.serialization.types.scalar.FileType;
import ecologylab.serialization.types.scalar.FloatType;
import ecologylab.serialization.types.scalar.IntType;
import ecologylab.serialization.types.scalar.LongType;
import ecologylab.serialization.types.scalar.ParsedURLType;
import ecologylab.serialization.types.scalar.PatternType;
import ecologylab.serialization.types.scalar.RectangleType;
import ecologylab.serialization.types.scalar.ReferenceBooleanType;
import ecologylab.serialization.types.scalar.ReferenceDoubleType;
import ecologylab.serialization.types.scalar.ReferenceFloatType;
import ecologylab.serialization.types.scalar.ReferenceIntegerType;
import ecologylab.serialization.types.scalar.ReferenceLongType;
import ecologylab.serialization.types.scalar.ScalarTypeType;
import ecologylab.serialization.types.scalar.ShortType;
import ecologylab.serialization.types.scalar.StringBuilderType;
import ecologylab.serialization.types.scalar.StringType;
import ecologylab.serialization.types.scalar.URLType;
import ecologylab.serialization.types.scalar.UUIDType;

public class FundamentalTypes
implements CrossLanguageTypeConstants
{
	public static final CollectionType ARRAYLIST_TYPE				= 
		new CollectionType(ArrayList.class, DOTNET_ARRAYLIST, OBJC_ARRAYLIST);

	public static final CollectionType HASHMAP_TYPE					= 
		new CollectionType(HashMap.class, DOTNET_HASHMAP, OBJC_HASHMAP);

	public static final CollectionType HASHMAPARRAYLIST_TYPE	= 
		new CollectionType( HashMapArrayList.class, DOTNET_HASHMAPARRAYLIST, OBJC_HASHMAPARRAYLIST);

	public static final CollectionType SCOPE_TYPE						= 
		new CollectionType(Scope.class, DOTNET_SCOPE, OBJC_SCOPE);
	
	static
	{
		TypeRegistry.setDefaultCollectionType(ARRAYLIST_TYPE);
		TypeRegistry.setDefaultMapType(HASHMAP_TYPE);
	}
	
	public static final ScalarType STRING_TYPE 					= new StringType();
	
	public static final ScalarType STRING_BUILDER_TYPE	= new StringBuilderType();
	
	public static final ScalarType INT_TYPE 						= new IntType();
	
	public static final ScalarType REFERENCE_INTEGER_TYPE 	= new ReferenceIntegerType();
	
	public static final ScalarType LONG_TYPE 						= new LongType();
	
	public static final ScalarType REFERENCE_LONG_TYPE 	= new ReferenceLongType();
	
	public static final ScalarType BOOLEAN_TYPE 				= new BooleanType();
	
	public static final ScalarType REFERENCE_BOOLEAN_TYPE 	= new ReferenceBooleanType();
	
	public static final ScalarType FLOAT_TYPE 					= new FloatType();
	
	public static final ScalarType REFERENCE_FLOAT_TYPE = new ReferenceFloatType();
	
	public static final ScalarType DOUBLE_TYPE 					= new DoubleType();
	
	public static final ScalarType REFERENCE_DOUBLE_TYPE 		= new ReferenceDoubleType();

	
	public static final ScalarType SHORT_TYPE 					= new ShortType();
	
	public static final ScalarType BYTE_TYPE 						= new ByteType();
	
	public static final ScalarType CHAR_TYPE 						= new CharType();
	
	
	public static final ScalarType URL_TYPE 						= new URLType();
	
	public static final ScalarType PARSED_URL_TYPE 			= new ParsedURLType();
	
	public static final ScalarType FILE_TYPE 						= new FileType();
	
	public static final ScalarType UUID_TYPE 						= new UUIDType();
	
	public static final ScalarType DATE_TYPE 						= new DateType();
	
	public static final ScalarType SCALAR_TYPE_TYPE 		= new ScalarTypeType();
	
	public static final ScalarType COLLECTION_TYPE_TYPE = new CollectionTypeType();
	
	public static final ScalarType PATTERN_TYPE 				= new PatternType();
	
	public static final ScalarType ENUMERATED_TYPE 			= new EnumeratedType();
	
	public static final ScalarType CLASS_TYPE 					= new ClassType();
	
	public static final ScalarType FIELD_TYPE 					= new FieldType();
	
	public static final ScalarType COMPOSITE_AS_SCALAR_TYPE = new CompositeAsScalarType();
	
	public static final ScalarType COLOR_TYPE 					= new ColorType();
	
	public static final ScalarType RECTANGLE_TYPE 			= new RectangleType();

}