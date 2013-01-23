package ecologylab.serialization.types;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Pattern;

import ecologylab.collections.Scope;
import ecologylab.generic.HashMapArrayList;
import ecologylab.net.ParsedURL;
import ecologylab.serialization.types.scalar.BinaryDataType;
import ecologylab.serialization.types.scalar.BooleanType;
import ecologylab.serialization.types.scalar.ByteType;
import ecologylab.serialization.types.scalar.CharType;
import ecologylab.serialization.types.scalar.ClassType;
import ecologylab.serialization.types.scalar.CollectionTypeType;
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
	public static final CollectionType<ArrayList> ARRAYLIST_TYPE				= 
		new CollectionType<ArrayList>(ArrayList.class, DOTNET_ARRAYLIST, OBJC_ARRAYLIST);

	public static final CollectionType<HashMap> HASHMAP_TYPE					= 
		new CollectionType<HashMap>(HashMap.class, DOTNET_HASHMAP, OBJC_HASHMAP);

	public static final CollectionType<HashMapArrayList> HASHMAPARRAYLIST_TYPE	= 
		new CollectionType<HashMapArrayList>( HashMapArrayList.class, DOTNET_HASHMAPARRAYLIST, OBJC_HASHMAPARRAYLIST);

	public static final CollectionType<Scope> SCOPE_TYPE						= 
		new CollectionType<Scope>(Scope.class, DOTNET_SCOPE, OBJC_SCOPE);
	
	static
	{
		TypeRegistry.setDefaultCollectionType(ARRAYLIST_TYPE);
		TypeRegistry.setDefaultMapType(HASHMAP_TYPE);
	}
	
	public static final ScalarType<String> STRING_TYPE 					= new StringType();
	
	public static final ScalarType<StringBuilder> STRING_BUILDER_TYPE	= new StringBuilderType();
	
	public static final ScalarType<Integer> INT_TYPE 						= new IntType();
	
	public static final ScalarType<Integer> REFERENCE_INTEGER_TYPE 	= new ReferenceIntegerType();
	
	public static final ScalarType<Long> LONG_TYPE 						= new LongType();
	
	public static final ScalarType<Long> REFERENCE_LONG_TYPE 	= new ReferenceLongType();
	
	public static final ScalarType<Boolean> BOOLEAN_TYPE 				= new BooleanType();
	
	public static final ScalarType<Boolean> REFERENCE_BOOLEAN_TYPE 	= new ReferenceBooleanType();
	
	public static final ScalarType<Float> FLOAT_TYPE 					= new FloatType();
	
	public static final ScalarType<Float> REFERENCE_FLOAT_TYPE = new ReferenceFloatType();
	
	public static final ScalarType<Double> DOUBLE_TYPE 					= new DoubleType();
	
	public static final ScalarType<Double> REFERENCE_DOUBLE_TYPE 		= new ReferenceDoubleType();

	
	public static final ScalarType<Short> SHORT_TYPE 					= new ShortType();
	
	public static final ScalarType<Byte> BYTE_TYPE 						= new ByteType();
	
	public static final ScalarType<Character> CHAR_TYPE 						= new CharType();
	
	
	public static final ScalarType<URL> URL_TYPE 						= new URLType();
	
	public static final ScalarType<ParsedURL> PARSED_URL_TYPE 			= new ParsedURLType();
	
	public static final ScalarType<File> FILE_TYPE 						= new FileType();
	
	public static final ScalarType<ByteBuffer> BINARY_DATA_TYPE				= new BinaryDataType();
	
	public static final ScalarType<UUID> UUID_TYPE 						= new UUIDType();
	
	public static final ScalarType<Date> DATE_TYPE 						= new DateType();
	
	public static final ScalarType<ScalarType> SCALAR_TYPE_TYPE 		= new ScalarTypeType();
	
	public static final ScalarType<CollectionType> COLLECTION_TYPE_TYPE = new CollectionTypeType();
	
	public static final ScalarType<Pattern> PATTERN_TYPE 				= new PatternType();
	
	public static final ScalarType<Enum> ENUMERATED_TYPE 			= new EnumeratedType();
	
	public static final ScalarType<Class> CLASS_TYPE 					= new ClassType();
	
	public static final ScalarType<Field> FIELD_TYPE 					= new FieldType();
	
	public static final ScalarType COMPOSITE_AS_SCALAR_TYPE = new CompositeAsScalarType(); 
	
}