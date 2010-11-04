package translators.net;

import java.lang.annotation.Annotation;
import java.util.HashMap;

import ecologylab.generic.Debug;
import ecologylab.semantics.metadata.Metadata.mm_name;
import ecologylab.serialization.ElementState.simpl_scope;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.Hint;
import ecologylab.serialization.ElementState.simpl_classes;
import ecologylab.serialization.ElementState.simpl_collection;
import ecologylab.serialization.ElementState.simpl_hints;
import ecologylab.serialization.ElementState.simpl_map;
import ecologylab.serialization.ElementState.xml_other_tags;
import ecologylab.serialization.ElementState.xml_tag;
import ecologylab.serialization.simpl_descriptor_classes;

/**
 * Static methods to do repeated useful tasks during the translation
 * 
 * 
 * @author nabeel
 * 
 */
public class DotNetTranslationUtilities
{
	
	private static HashMap<String, String> keywords = new HashMap<String, String>();
	
	static
	{
		keywords.put("object", "object");
		keywords.put("as", "as");
		keywords.put("byte", "byte");
		keywords.put("class", "class");
		keywords.put("delegate", "delegate");
		keywords.put("event", "event");
		keywords.put("fixed", "fixed");
		keywords.put("goto", "goto");
		keywords.put("interface", "interface");
		keywords.put("namespace", "namespace");
		keywords.put("out", "out");
		keywords.put("public", "public");
		keywords.put("sealed", "sealed");
		keywords.put("static", "static");
		keywords.put("throw", "throw");
		keywords.put("ulong", "ulong");
		keywords.put("var", "var");
		keywords.put("case", "case");
		keywords.put("const", "const");
		keywords.put("do", "do");
		keywords.put("explicit", "explicit");
		keywords.put("float", "float");
		keywords.put("if", "if");
		keywords.put("internal", "internal");
		keywords.put("new", "new");
		keywords.put("override", "override");
		keywords.put("readonly", "readonly");
		keywords.put("short", "short");
		keywords.put("string", "string");
		keywords.put("true", "true");
		keywords.put("unchecked", "unchecked");
		keywords.put("virtual", "virtual");
		keywords.put("base", "base");
		keywords.put("catch", "catch");
		keywords.put("continue", "continue");
		keywords.put("double", "double");
		keywords.put("extern", "extern");
		keywords.put("for", "for");
		keywords.put("implicit", "implicit");
		keywords.put("is", "is");
		keywords.put("null", "null");
		keywords.put("params", "params");
		keywords.put("ref", "ref");
		keywords.put("sizeof", "sizeof");
		keywords.put("struct", "struct");
		keywords.put("try", "try");
		keywords.put("unsafe", "unsafe");
		keywords.put("void", "void");
		keywords.put("bool", "bool");
		keywords.put("char", "char");
		keywords.put("decimal", "decimal");
		keywords.put("else", "else");
		keywords.put("false", "false");		
		keywords.put("foreach", "foreach");
		keywords.put("in", "in");		
		keywords.put("lock", "lock");
		keywords.put("private", "private");
		keywords.put("return", "return");
		keywords.put("stackalloc", "return");
		keywords.put("switch", "switch");
		keywords.put("typeof", "typeof");
		keywords.put("ushort", "ushort");
		keywords.put("volatile", "volatile");
		keywords.put("break", "break");
		keywords.put("checked", "checked");
		keywords.put("default", "default");
		keywords.put("enum", "enum");
		keywords.put("finally", "finally");
		keywords.put("int", "int");
		keywords.put("long", "long");
		keywords.put("operator", "operator");
		keywords.put("protected", "protected");
		keywords.put("sbyte", "sbyte");
		keywords.put("this", "this");
		keywords.put("uint", "uint");
		keywords.put("using", "using");
		keywords.put("while", "while");	
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
		else if (annotation instanceof simpl_map)
		{
			return getCSharpMapAnnotation(annotation);
		}
		else if (annotation instanceof simpl_classes)
		{
			return getCSharpClassesAnnotation(annotation);
		}
		else if (annotation instanceof simpl_hints)
		{
			return getCSharpHintsAnnotation(annotation);
		}
		else if (annotation instanceof simpl_scope)
		{
			return getCSharpScopeAnnotation(annotation);
		}
		else if (annotation instanceof xml_tag)
		{
			return getCSharpTagAnnotation(annotation);
		}
		else if (annotation instanceof xml_other_tags)
		{
			return getCSharpOtherTagsAnnotation(annotation);
		}
		else if (annotation instanceof simpl_descriptor_classes)
		{
			return getCSharpOtherDescAnnotation(annotation);
		}
		else if (annotation instanceof mm_name)
		{
			return getCSharpMMNameAnnotation(annotation);
		}

		return simpleName;
	}




	private static String getCSharpOtherDescAnnotation(Annotation annotation)
{
		String parameter = null;
		simpl_descriptor_classes classesAnnotation = (simpl_descriptor_classes) annotation;
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
	

	private static String getCSharpScopeAnnotation(Annotation annotation)
	{
		String parameter = null;
		simpl_scope scopeAnnotation = (simpl_scope) annotation;
		String scopeValue = scopeAnnotation.value();
		String simpleName = getSimpleName(scopeAnnotation);
		if(scopeValue != null && !scopeValue.isEmpty())
		{
			parameter = "(\"" + scopeValue + "\")";
			return simpleName + parameter;
		}
		else
		{
			Debug.error(scopeAnnotation, "Scope without a parameter");
			return null;
		}
		
	}
	


	private static String getCSharpOtherTagsAnnotation(Annotation annotation)
	{
		String parameter = null;
		xml_other_tags scopeAnnotation = (xml_other_tags) annotation;
		String[] scopeValue = scopeAnnotation.value();
		String simpleName = getSimpleName(scopeAnnotation);
		if(scopeValue != null && scopeValue.length > 0)
		{
			parameter = "(new String[]{";
			for(String otherTag : scopeValue)
				parameter += "\"" + otherTag + "\", ";
			
			parameter += "})";
			return simpleName + parameter;
		}
		else
		{
			Debug.error(scopeAnnotation, "xml_other_tags without any parameters");
			return null;
		}
	}


	private static String getCSharpMMNameAnnotation(Annotation annotation)
	{
		String parameter = null;
		mm_name mmNameAnnotation = (mm_name) annotation;
		String tagValue = mmNameAnnotation.value();
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


	private static String getCSharpMapAnnotation(Annotation annotation)
	{
		String parameter = null;
		simpl_map collectionAnnotation = (simpl_map) annotation;
		String tagValue = collectionAnnotation.value();
		String simpleName = getSimpleName(annotation);

		if (tagValue != null && !tagValue.isEmpty())
		{
			parameter = "(" + "\"" + tagValue + "\"" + ")";
			return simpleName + parameter;
		}
		else
		{
			Debug.warning(collectionAnnotation, "Map declared with no tags");
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
	
	public static String getPropertyName(FieldDescriptor fieldDescriptor)
	{
		if(fieldDescriptor == null)
		{
			return "null";
		}
		else
		{
			String fieldName = fieldDescriptor.getFieldName();
			StringBuilder propertyName = new StringBuilder();
			
			String declaringClassName = fieldDescriptor.getField().getDeclaringClass().getSimpleName();
			
			if(Character.isLowerCase(fieldName.charAt(0)))
			{
				propertyName.append(Character.toUpperCase(fieldName.charAt(0)));
				propertyName.append(fieldName.subSequence(1, fieldName.length()));
				
				if(propertyName.toString().equals(declaringClassName))
				{
					StringBuilder pName = new StringBuilder();
					
					pName.append('P');
					pName.append(propertyName);
					
					return pName.toString();
				}
			}	
			else
			{
				propertyName.append('P');
				propertyName.append(fieldName);
			}
			return propertyName.toString();
		}			
	}
	

}
