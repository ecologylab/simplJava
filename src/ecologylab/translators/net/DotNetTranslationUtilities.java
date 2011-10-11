package ecologylab.translators.net;

import java.util.HashMap;
import java.util.Map;

import ecologylab.semantics.html.utils.StringBuilderUtils;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.XMLTools;
import ecologylab.serialization.annotations.Hint;

/**
 * Static methods to do repeated useful tasks during the translation
 * 
 * 
 * @author nabeel
 * 
 */
public class DotNetTranslationUtilities
{

	private static final String							PROPERTY_SAFE_SUFFIX	= "Prop";

	private static HashMap<String, String>	keywords							= new HashMap<String, String>();

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
	
	/**
	 * (Potentially) The look-up table for translating Java annotations to C# attributes. Only for
	 * those not conforming with the standard translation method. 
	 */
	private static Map<String, String> annotationTranslations = new HashMap<String, String>();
	
	static
	{
		// init annotationTranslations
	}
	
	/**
	 * Utility function to translate java annotation names to C# attribute names;
	 * 
	 * @param annotation
	 * @return
	 */
	public static String translateAnnotationName(String simpleName)
	{
		if (annotationTranslations.containsKey(simpleName))
			return annotationTranslations.get(simpleName);
		return XMLTools.classNameFromElementName(simpleName);
	}
	
	public static String translateMetaInfoArgValue(Object argValue)
	{
		// TODO to make this extendible, use an interface MetaInfoArgValueTranslator and allow users
		//      to inject new ones to handle different kind of cases.
		if (argValue instanceof String)
		{
			return "\"" + argValue.toString() + "\"";
		}
		else if (argValue instanceof Hint)
		{
			switch ((Hint) argValue)
			{
			case XML_ATTRIBUTE: return "Hint.XmlAttribute"; 
			case XML_LEAF: return "Hint.XmlLeaf"; 
			case XML_LEAF_CDATA: return "Hint.XmlLeafCdata"; 
			case XML_TEXT: return "Hint.XmlText"; 
			case XML_TEXT_CDATA: return "Hint.XmlTextCdata"; 
			default: return "Hint.Undefined";
			}
		}
		// eles if (argValue instanceof ClassDescriptor)
		return null;
	}

	/**
	 * Generate a C# property name for this field.
	 * 
	 * @param fieldDescriptor
	 * @return
	 */
	public static String getPropertyName(FieldDescriptor fieldDescriptor)
	{
		String fieldName = fieldDescriptor.getName();
		StringBuilder propertyNameBuilder = StringBuilderUtils.acquire();
		String propertyName = null;

		ClassDescriptor elementCD = fieldDescriptor.getElementClassDescriptor();
		String declaringClassName = elementCD == null ? null : elementCD.getDescribedClassSimpleName();

		if (Character.isLowerCase(fieldName.charAt(0)))
		{
			propertyNameBuilder.append(Character.toUpperCase(fieldName.charAt(0)));
			propertyNameBuilder.append(fieldName, 1, fieldName.length());
			propertyName = propertyNameBuilder.toString();
			if (propertyName.equals(declaringClassName))
			{
				propertyNameBuilder.append(PROPERTY_SAFE_SUFFIX);
				propertyName = propertyNameBuilder.toString();
			}
		}
		else
		{
			propertyNameBuilder.append(fieldName);
			propertyNameBuilder.append(PROPERTY_SAFE_SUFFIX);
			propertyName = propertyNameBuilder.toString();
		}
		StringBuilderUtils.release(propertyNameBuilder);
		return propertyName;
	}

}
