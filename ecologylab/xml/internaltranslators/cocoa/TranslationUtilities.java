package ecologylab.xml.internaltranslators.cocoa;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import ecologylab.collections.Scope;
import ecologylab.generic.HashMapArrayList;
import ecologylab.net.ParsedURL;
import ecologylab.xml.types.scalar.ScalarType;

/**
 * Utilities class to provide static helper methods and code logic used again and again by the
 * system
 * 
 * @author Nabeel Shahzad
 */
public class TranslationUtilities
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

	/**
	 * Methods that does the job of mapping the Java datatypes to corresponding Objective-C datatypes.
	 * 
	 * @param type
	 * @return String
	 * @throws CocoaTranslationException
	 */
	public static String getObjectiveCType(Class<?> type) throws CocoaTranslationException
	{
		if (int.class == type)
		{
			return TranslationConstants.OBJC_INTEGER;
		}
		else if (float.class == type)
		{
			return TranslationConstants.OBJC_FLOAT;
		}
		else if (double.class == type)
		{
			return TranslationConstants.OBJC_DOUBLE;
		}
		else if (byte.class == type)
		{
			return TranslationConstants.OBJC_BYTE;
		}
		else if (char.class == type)
		{
			return TranslationConstants.OBJC_CHAR;
		}
		else if (boolean.class == type)
		{
			return TranslationConstants.OBJC_BOOLEAN;
		}
		else if (long.class == type)
		{
			return TranslationConstants.OBJC_LONG;
		}
		else if (short.class == type)
		{
			return TranslationConstants.OBJC_SHORT;
		}
		else if (String.class == type)
		{
			return TranslationConstants.OBJC_STRING;
		}
		else if (StringBuilder.class == type)
		{
			return TranslationConstants.OBJC_STRING_BUILDER;
		}
		else if (URL.class == type)
		{
			return TranslationConstants.OBJC_URL;
		}
		else if (ParsedURL.class == type)
		{
			return TranslationConstants.OBJC_PARSED_URL;
		}
		else if (ScalarType.class == type)
		{
			return TranslationConstants.OBJC_SCALAR_TYPE;
		}
		else if (Date.class == type)
		{
			return TranslationConstants.OBJC_DATE;
		}
		else if (ArrayList.class == type)
		{
			return TranslationConstants.OBJC_ARRAYLIST;
		}
		else if (HashMap.class == type)
		{
			return TranslationConstants.OBJC_HASHMAP;
		}
		else if (HashMapArrayList.class == type)
		{
			return TranslationConstants.OBJC_HASHMAPARRAYLIST;
		}
		else if (Scope.class == type)
		{
			return TranslationConstants.OBJC_SCOPE;
		}
		else if (Class.class == type)
		{
			return TranslationConstants.OBJC_CLASS;
		}
		else if (Field.class == type)
		{
			return TranslationConstants.OBJC_FIELD;
		}
		else
		{
			throw new CocoaTranslationException(CocaTranslationExceptionTypes.UNSUPPORTED_DATATYPE);
		}
	}

	/**
	 * Utility methods get the class simple from the class type object
	 * 
	 * @param Class
	 *          <?> thatClass
	 */
	public static String classSimpleName(Class<?> thatClass)
	{
		return thatClass.toString().substring(
				thatClass.toString().lastIndexOf(TranslationConstants.DOT) + 1);
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
