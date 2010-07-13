package translators.net;

import java.lang.annotation.Annotation;

import ecologylab.serialization.Hint;
import ecologylab.serialization.ElementState.simpl_classes;
import ecologylab.serialization.ElementState.simpl_collection;
import ecologylab.serialization.ElementState.simpl_hints;
import ecologylab.serialization.ElementState.xml_tag;

/**
 * Static methods to do repeated useful tasks during the translation
 * 
 * 
 * @author nabeel
 * 
 */
public class DotNetTranslationUtilities
{
//	/**
//	 * Gets the string representing the equivalent C# type
//	 * 
//	 * @param field
//	 * @return
//	 * @throws DotNetTranslationException
//	 */
//	public static String getCSharpType(Field field) throws DotNetTranslationException
//	{
//		Class<?> fieldType = field.getType();
//
//		String result = null;
//
//		result = inferCSharpType(fieldType);
//
//		if (isGeneric(field))
//		{
//			result += getCSharpGenericParametersString(field);
//		}
//
//		return result;
//	}
//
//	/**
//	 * Utility function to translate Java type to CSharp type
//	 * 
//	 * @param fieldType
//	 * @return
//	 * @throws DotNetTranslationException
//	 */
//	public static String inferCSharpType(Class<?> fieldType) throws DotNetTranslationException
//	{
//		String result = null;
//
//		if (int.class == fieldType)
//		{
//			result = DotNetTranslationConstants.DOTNET_INTEGER;
//		}
//		else if (float.class == fieldType)
//		{
//			result = DotNetTranslationConstants.DOTNET_FLOAT;
//		}
//		else if (double.class == fieldType)
//		{
//			result = DotNetTranslationConstants.DOTNET_DOUBLE;
//		}
//		else if (byte.class == fieldType)
//		{
//			result = DotNetTranslationConstants.DOTNET_BYTE;
//		}
//		else if (char.class == fieldType)
//		{
//			result = DotNetTranslationConstants.DOTNET_CHAR;
//		}
//		else if (boolean.class == fieldType)
//		{
//			result = DotNetTranslationConstants.DOTNET_BOOLEAN;
//		}
//		else if (long.class == fieldType)
//		{
//			result = DotNetTranslationConstants.DOTNET_LONG;
//		}
//		else if (short.class == fieldType)
//		{
//			result = DotNetTranslationConstants.DOTNET_SHORT;
//		}
//		else if (String.class == fieldType)
//		{
//			result = DotNetTranslationConstants.DOTNET_STRING;
//		}
//		else if (StringBuilder.class == fieldType)
//		{
//			result = DotNetTranslationConstants.DOTNET_STRING_BUILDER;
//		}
//		else if (URL.class == fieldType)
//		{
//			result = DotNetTranslationConstants.DOTNET_URL;
//		}
//		else if (ParsedURL.class == fieldType)
//		{
//			result = DotNetTranslationConstants.DOTNET_PARSED_URL;
//		}
//		else if (ScalarType.class == fieldType)
//		{
//			result = DotNetTranslationConstants.DOTNET_SCALAR_TYPE;
//		}
//		else if (Date.class == fieldType)
//		{
//			result = DotNetTranslationConstants.DOTNET_DATE;
//		}
//		else if (ArrayList.class == fieldType)
//		{
//			result = DotNetTranslationConstants.DOTNET_ARRAYLIST;
//		}
//		else if (HashMap.class == fieldType)
//		{
//			result = DotNetTranslationConstants.DOTNET_HASHMAP;
//		}
//		else if (HashMapArrayList.class == fieldType)
//		{
//			result = DotNetTranslationConstants.DOTNET_HASHMAPARRAYLIST;
//		}
//		else if (Scope.class == fieldType)
//		{
//			result = DotNetTranslationConstants.DOTNET_SCOPE;
//		}
//		else if (Class.class == fieldType)
//		{
//			result = DotNetTranslationConstants.DOTNET_CLASS;
//		}
//		else if (Field.class == fieldType)
//		{
//			result = DotNetTranslationConstants.DOTNET_FIELD;
//		}
//		else
//		{
//			// Assume the field is custom object
//			result = fieldType.getSimpleName();
//		}
//		return result;
//	}
//
//	/**
//	 * Utility function to check if the field is declared as generic type
//	 * 
//	 * @param field
//	 * @return
//	 */
//	public static boolean isGeneric(Field field)
//	{
//		if (field.getGenericType() instanceof ParameterizedType)
//		{
//			return true;
//		}
//		else
//			return false;
//	}
//
//	/**
//	 * Utility function to get the generic parameters of a field as class array
//	 * 
//	 * @param field
//	 * @return
//	 */
//	public static Class<?>[] getGenericParameters(Field field)
//	{
//		Class<?>[] result = null;
//
//		if (field.getGenericType() instanceof ParameterizedType)
//		{
//			Type[] ta = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
//			result = new Class<?>[ta.length];
//
//			for (int i = 0; i < ta.length; i++)
//			{
//				result[i] = ((Class<?>) ta[i]);
//			}
//		}
//
//		return result;
//	}
//
//	/**
//	 * Utility function to get the generic parameters as string array
//	 * 
//	 * @param field
//	 * @return
//	 * @throws DotNetTranslationException
//	 */
//	public static String[] getCSharpGenericParamters(Field field) throws DotNetTranslationException
//	{
//		String[] result = null;
//
//		Class<?>[] paramClasses = getGenericParameters(field);
//		if (paramClasses.length > 0)
//		{
//			result = new String[paramClasses.length];
//			for (int i = 0; i < paramClasses.length; i++)
//			{
//				result[i] = inferCSharpType(paramClasses[i]);
//			}
//		}
//
//		return result;
//	}
//
//	/**
//	 * Utility function to get the generic parameters as comma separated String
//	 * 
//	 * @param field
//	 * @return
//	 * @throws DotNetTranslationException
//	 */
//	public static String getCSharpGenericParametersString(Field field)
//			throws DotNetTranslationException
//	{
//		StringBuilder result = null;
//
//		String[] paramArray = getCSharpGenericParamters(field);
//		if (paramArray.length > 0)
//		{
//			result = new StringBuilder();
//			result.append("<");
//			result.append(implode(paramArray, ", "));
//			result.append(">");
//		}
//		return result.toString();
//	}
//
//	/**
//	 * Utility function to implode an array of strings
//	 * 
//	 * @param ary
//	 * @param delim
//	 * @return
//	 */
//	public static String implode(String[] ary, String delim)
//	{
//		String out = "";
//		for (int i = 0; i < ary.length; i++)
//		{
//			if (i != 0)
//			{
//				out += delim;
//			}
//			out += ary[i];
//		}
//		return out;
//	}

	/**
	 * Utility function to translate java annotation to C# attribute
	 * 
	 * @param annotation
	 * @return
	 */
	public static String getCSharpAnnotation(Annotation annotation)
	{
		String simpleName = getSimpleName(annotation);

		if (annotation instanceof simpl_collection)
		{
			return getCSharpCollectionAnnotation(annotation);
		}
		else if (annotation instanceof xml_tag)
		{
			return getCSharpTagAnnotation(annotation);
		}
		else if (annotation instanceof simpl_classes)
		{
			return getCSharpClassesAnnotation(annotation);
		}
		else if (annotation instanceof simpl_hints)
		{
			return getCSharpHintsAnnotation(annotation);
		}

		return simpleName;
	}

	/**
	 * Utility function to translate java classes annotation to C# attribute
	 * 
	 * @param annotation
	 * @return
	 */
	private static String getCSharpClassesAnnotation(Annotation annotation)
	{
		String parameter = null;
		simpl_classes classesAnnotation = (simpl_classes) annotation;
		Class<?>[] classArray = classesAnnotation.value();

		String simpleName = getSimpleName(annotation);

		if (classArray != null)
		{
			parameter = "(new Type[] { ";

			for (int i = 0; i < classArray.length; i++)
			{
				String tempString = "typeof(" + classArray[i].getSimpleName() + ")";
				if (i != classArray.length - 1)
					parameter += tempString + ", ";
				else
					parameter += tempString;
			}

			parameter += " })";

			return simpleName + parameter;
		}
		else
			return null;
	}

	/**
	 * Utility function to translate java tag annotation to C# attribute
	 * 
	 * @param annotation
	 * @return
	 */
	private static String getCSharpTagAnnotation(Annotation annotation)
	{
		String parameter = null;
		xml_tag tagAnnotation = (xml_tag) annotation;
		String tagValue = tagAnnotation.value();

		String simpleName = getSimpleName(annotation);

		if (tagValue != null && !tagValue.isEmpty())
		{
			parameter = "(" + "\"" + tagValue + "\"" + ")";
			return simpleName + parameter;
		}
		else
		{
			return simpleName;
		}
	}
	
	/**
	 * Utility function to translate java hints annotation to C# attribute
	 * 
	 * @param annotation
	 * @return
	 */
	private static String getCSharpHintsAnnotation(Annotation annotation)
	{
		String parameter = null;
		
		simpl_hints tagAnnotation = (simpl_hints) annotation;
		Hint[] hintsArray = tagAnnotation.value();

		String simpleName = getSimpleName(annotation);

		if (hintsArray != null && hintsArray.length > 0 )
		{
			parameter = "(new Hint[] { ";
			
			for (int i = 0; i < hintsArray.length; i++)
			{
				String tempString = "Hint." + hintsArray[i].toString();
				if (i != hintsArray.length - 1)
					parameter += tempString + ", ";
				else
					parameter += tempString;
			}
			
			parameter += " })";
			return simpleName + parameter;
		}
		else
		{
			return null;
		}
	}
	

	/**
	 * Utility function to translate java collection annotation to C# attribute
	 * 
	 * @param annotation
	 * @return
	 */
	private static String getCSharpCollectionAnnotation(Annotation annotation)
	{
		String parameter = null;
		simpl_collection collectionAnnotation = (simpl_collection) annotation;
		String tagValue = collectionAnnotation.value();
		String simpleName = getSimpleName(annotation);

		if (tagValue != null && !tagValue.isEmpty())
		{
			parameter = "(" + "\"" + tagValue + "\"" + ")";
			return simpleName + parameter;
		}
		else
		{
			return simpleName;
		}
	}

	/**
	 * Gets the simple name of the annotation. For the time being it is replacing xml with serial
	 * 
	 * @param annotation
	 * @return
	 */
	private static String getSimpleName(Annotation annotation)
	{
		return annotation.annotationType().getSimpleName();
	}

}
