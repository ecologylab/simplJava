package translators.net;

/**
 * General class file declares various constants used by the translators for generating the right
 * output files.
 * 
 * @author Nabeel Shahzad
 */
public interface DotNetTranslationConstants
{

	/*
	 * File constants
	 */
	public static final String	PACKAGE_NAME_SEPARATOR	= "\\.";

	public static final String	FILE_PATH_SEPARATOR			= "/";

	public static final String	FILE_EXTENSION					= ".cs";

	public static final String	XML_FILE_EXTENSION			= ".xml";

	/*
	 * Keywords
	 */
	public static final String	PUBLIC									= "public";

	public static final String	PRIVATE									= "private";

	public static final String	CLASS										= "class";

	public static final String	INHERITANCE_OBJECT			= "ElementState";

	public static final String	INHERITANCE_OPERATOR		= ":";

	public static final String	NAMESPACE								= "namespace";

	public static final String	USING										= "using";

	public static final String	SYSTEM									= "System";

	public static final String	COLLECTIONS							= "Collections";

	public static final String	GENERIC									= "Generic";

	public static final String	ECOLOGYLAB_NAMESPACE		= "ecologylabFundamental.ecologylab.atttributes";
	
	public static final String	SERIALIZATION_NAMESPACE		= "ecologylabFundamental.ecologylab.serialization";

	/*
	 * Formatting constants
	 */

	public static final String	OPENING_CURLY_BRACE			= "{";

	public static final String	CLOSING_CURLY_BRACE			= "}";

	public static final String	OPENING_SQUARE_BRACE		= "[";

	public static final String	CLOSING_SQUARE_BRACE		= "]";

	public static final String	OPENING_BRACE						= "(";

	public static final String	CLOSING_BRACE						= ")";

	public static final String	SINGLE_LINE_BREAK				= "\n";

	public static final String	DOUBLE_LINE_BREAK				= "\n\n";

	public static final String	TAB											= "\t";

	public static final String	DOUBLE_TAB							= "\t\t";

	public static final String	SPACE										= " ";

	public static final String	DOT											= ".";

	public static final String	END_LINE								= ";";

	public static final String	OPEN_COMMENTS						= "/// <summary>";

	public static final String	XML_COMMENTS						= "/// ";

	public static final String	CLOSE_COMMENTS					= "/// </summary>";

	/*
	 * Scalar types
	 */
	public static final String	DOTNET_INTEGER					= "Int32";

	public static final String	DOTNET_FLOAT						= "Single";

	public static final String	DOTNET_DOUBLE						= "Double";

	public static final String	DOTNET_BYTE							= "Char";

	public static final String	DOTNET_CHAR							= "Byte";

	public static final String	DOTNET_BOOLEAN					= "Boolean";

	public static final String	DOTNET_LONG							= "Int64";

	public static final String	DOTNET_SHORT						= "Int16";

	public static final String	DOTNET_STRING						= "String";

	/*
	 * Reference types
	 */
	public static final String	DOTNET_DATE							= "DateTime";

	public static final String	DOTNET_STRING_BUILDER		= "StringBuilder";

	public static final String	DOTNET_URL							= "Uri";

	public static final String	DOTNET_PARSED_URL				= "ParsedURL";

	public static final String	DOTNET_SCALAR_TYPE			= "ScalarType";

	public static final String	DOTNET_CLASS						= "Type";

	public static final String	DOTNET_FIELD						= "FieldInfo";

	/*
	 * Collection types
	 */
	public static final String	DOTNET_ARRAYLIST				= "List";

	public static final String	DOTNET_HASHMAP					= "Dictionary";

	public static final String	DOTNET_HASHMAPARRAYLIST	= "DictionaryList";

	public static final String	DOTNET_SCOPE						= "Scope";

}
