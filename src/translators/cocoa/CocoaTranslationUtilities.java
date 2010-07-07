package translators.cocoa;

import java.util.HashMap;

/**
 * Utilities class to provide static helper methods and code logic used again and again by the
 * system
 * 
 * @author Nabeel Shahzad
 */
public class CocoaTranslationUtilities
{

	/*
	 * Static hashmap of keywords which cannot be translated from java to objective-c
	 */
	private static HashMap<String, String>	keywords	= new HashMap<String, String>();

	static
	{
		keywords.put("super", "super");
		keywords.put("void", "void");
		keywords.put("char", "char");
		keywords.put("short", "short");
		keywords.put("int", "int");
		keywords.put("long", "long");
		keywords.put("float", "float");
		keywords.put("double", "double");
		keywords.put("signed", "signed");
		keywords.put("unsigned", "unsigned");
		keywords.put("id", "id");
		keywords.put("const", "const");
		keywords.put("volatile", "volatile");
		keywords.put("in", "in");
		keywords.put("out", "out");
		keywords.put("inout", "inout");
		keywords.put("bycopy", "bycopy");
		keywords.put("byref", "byref");
		keywords.put("oneway", "oneway");
		keywords.put("self", "self");
	}

	// /**
	// * Methods that does the job of mapping the Java datatypes to corresponding Objective-C
	// datatypes.
	// *
	// * @param type
	// * @return String
	// * @throws CocoaTranslationException
	// */
	// public static String getObjectiveCType(Class<?> type) throws CocoaTranslationException
	// {
	// if (int.class == type)
	// {
	// return CocoaTranslationConstants.OBJC_INTEGER;
	// }
	// else if (float.class == type)
	// {
	// return CocoaTranslationConstants.OBJC_FLOAT;
	// }
	// else if (double.class == type)
	// {
	// return CocoaTranslationConstants.OBJC_DOUBLE;
	// }
	// else if (byte.class == type)
	// {
	// return CocoaTranslationConstants.OBJC_BYTE;
	// }
	// else if (char.class == type)
	// {
	// return CocoaTranslationConstants.OBJC_CHAR;
	// }
	// else if (boolean.class == type)
	// {
	// return CocoaTranslationConstants.OBJC_BOOLEAN;
	// }
	// else if (long.class == type)
	// {
	// return CocoaTranslationConstants.OBJC_LONG;
	// }
	// else if (short.class == type)
	// {
	// return CocoaTranslationConstants.OBJC_SHORT;
	// }
	// else if (String.class == type)
	// {
	// return CocoaTranslationConstants.OBJC_STRING;
	// }
	// else if (StringBuilder.class == type)
	// {
	// return CocoaTranslationConstants.OBJC_STRING_BUILDER;
	// }
	// else if (URL.class == type)
	// {
	// return CocoaTranslationConstants.OBJC_URL;
	// }
	// else if (ParsedURL.class == type)
	// {
	// return CocoaTranslationConstants.OBJC_PARSED_URL;
	// }
	// else if (ScalarType.class == type)
	// {
	// return CocoaTranslationConstants.OBJC_SCALAR_TYPE;
	// }
	// else if (Date.class == type)
	// {
	// return CocoaTranslationConstants.OBJC_DATE;
	// }
	// else if (ArrayList.class == type || ArrayList.class == type.getSuperclass())
	// {
	// return CocoaTranslationConstants.OBJC_ARRAYLIST;
	// }
	// else if (HashMap.class == type)
	// {
	// return CocoaTranslationConstants.OBJC_HASHMAP;
	// }
	// else if (HashMapArrayList.class == type)
	// {
	// return CocoaTranslationConstants.OBJC_HASHMAPARRAYLIST;
	// }
	// else if (Scope.class == type)
	// {
	// return CocoaTranslationConstants.OBJC_SCOPE;
	// }
	// else if (Class.class == type)
	// {
	// return CocoaTranslationConstants.OBJC_CLASS;
	// }
	// else if (Field.class == type)
	// {
	// return CocoaTranslationConstants.OBJC_FIELD;
	// }
	// else if (Color.class == type)
	// {
	// return CocoaTranslationConstants.OBJC_COLOR;
	// }
	// else
	// {
	// throw new CocoaTranslationException(CocoaTranslationExceptionTypes.UNSUPPORTED_DATATYPE);
	// }
	// }

	/**
	 * Utility methods get the class simple from the class type object
	 * 
	 * @param Class
	 *          <?> thatClass
	 */
	public static String classSimpleName(Class<?> thatClass)
	{
		return thatClass.toString().substring(
				thatClass.toString().lastIndexOf(CocoaTranslationConstants.DOT) + 1);
	}

	/**
	 * Utility mehthods to check if the given field name is a keyword in objective-c
	 * 
	 * @param fieldName
	 */
	public static boolean isKeyword(String fieldName)
	{
		return keywords.containsKey(fieldName);

	}
}
