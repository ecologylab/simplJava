package ecologylab.translators.java;

/**
 * this file declares various constants used by the translators for generating the right
 * output files.
 * 
 * @author Sumith
 */

public interface JavaTranslationConstants {
	/*
	 * File constants
	 */
	public static final String	PACKAGE_NAME_SEPARATOR		= "\\.";

	public static final String	FILE_PATH_SEPARATOR				= "/";

	public static final String	FILE_EXTENSION						= ".java";

	public static final String	XML_FILE_EXTENSION				= ".xml";

	public static final String	TRANSATIONSCOPE_FOLDER		= "tscope";

	/*
	 * Keywords
	 */
	public static final String	PUBLIC										= "public";

	public static final String	STATIC										= "static";

	public static final String	PRIVATE										= "private";

	public static final String	GET												= "get";

	public static final String	SET												= "set";

	public static final String	CLASS											= "class";

	public static final String	RETURN										= "return";

	public static final String	VALUE											= "value";

	public static final String	INHERITANCE_OBJECT				= "ElementState";

	public static final String	INHERITANCE_OPERATOR			= "extends";

	public static final String	PACKAGE									= "package";

	public static final String	IMPORT											= "import";

	public static final String	SYSTEM										= "System";

	public static final String	COLLECTIONS								= "Collections";

	public static final String	GENERIC										= "Generic";

	public static final String	ECOLOGYLAB_NAMESPACE			= "ecologylab.attributes";

	public static final String	SERIALIZATION_NAMESPACE		= "ecologylab.serialization";

	public static final String	KEY												= "key";

	public static final String	TYPE_OF										= "typeof";

	public static final String	DEFAULT_IMPLEMENTATION		= "throw new NotImplementedException();";
	
	public static final String	THIS		= "this";
	
	public static final String	JAVA		= "java";
	
	public static final String	UTIL		= "util";

	/*
	 * Formatting constants
	 */

	public static final String	OPENING_CURLY_BRACE				= "{";

	public static final String	CLOSING_CURLY_BRACE				= "}";

	public static final String	OPENING_SQUARE_BRACE			= "[";

	public static final String	CLOSING_SQUARE_BRACE			= "]";
	
	public static final String	AT_SIGN			= "@";

	public static final String	OPENING_BRACE							= "(";

	public static final String	CLOSING_BRACE							= ")";

	public static final String	SINGLE_LINE_BREAK					= "\n";

	public static final String	DOUBLE_LINE_BREAK					= "\n\n";

	public static final String	TAB												= "\t";

	public static final String	DOUBLE_TAB								= "\t\t";

	public static final String	SPACE											= " ";
	
	public static final String	EQUALS											= "=";

	public static final String	DOT												= ".";

	public static final String	QUOTE											= "\"";

	public static final String	COMMA											= ",";

	public static final String	END_LINE									= ";";

	public static final String	ASSIGN										= "=";

	public static final String	OPEN_COMMENTS							= "/** ";

	public static final String	XML_COMMENTS							= " *";

	public static final String	CLOSE_COMMENTS						= " */ ";

	public static final String	SINGLE_LINE_COMMENT				= "//";

	/*
	 * Scalar types
	 */
	public static final String	JAVA_INTEGER						= "int";

	public static final String	JAVA_FLOAT							= "float";

	public static final String	JAVA_DOUBLE							= "double";

	public static final String	JAVA_BYTE								= "byte";

	public static final String	JAVA_CHAR								= "char";

	public static final String	JAVA_BOOLEAN						= "boolean";

	public static final String	JAVA_LONG								= "long";

	public static final String	JAVA_SHORT							= "short";

	public static final String	JAVA_STRING							= "String";

	/*
	 * Reference types
	 */
	public static final String	JAVA_DATE								= "Date";

	public static final String	JAVA_STRING_BUILDER			= "StringBuilder";

	public static final String	JAVA_URL								= "Url";

	public static final String	JAVA_PARSED_URL					= "ParsedURL";

	public static final String	JAVA_SCALAR_TYPE				= "ScalarType";

	public static final String	JAVA_CLASS							= "Class";

	public static final String	JAVA_FIELD							= "Field";

	public static final String	JAVA_OBJECT							= "Object";

	public static final String	JAVA_TRANSLATION_SCOPE	= "TranslationScope";

	/*
	 * Collection types
	 */
	public static final String	JAVA_ARRAYLIST					= "ArrayList";

	public static final String	JAVA_HASHMAP						= "HashMap";

	public static final String	JAVA_HASHMAPARRAYLIST		= "HashMapArrayList";

	public static final String	JAVA_SCOPE							= "Scope";

	/*
	 * Other constants
	 */
	public static final String	FGET											= "Get";

}
