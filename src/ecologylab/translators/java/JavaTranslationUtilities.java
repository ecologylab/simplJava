package ecologylab.translators.java;

import java.util.HashMap;

import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.annotations.Hint;

public class JavaTranslationUtilities {

	private static HashMap<String, String> keywords = new HashMap<String, String>();
	
	static
	{
		keywords.put("abstract", "abstract");
		keywords.put("continue","continue");
		keywords.put("for","for");
		keywords.put("new","new");
		keywords.put("switch","switch");
		keywords.put("assert","assert");
		keywords.put("default","default");
		keywords.put("package","package");
		keywords.put("synchronized","synchronized");
		keywords.put("boolean","boolean");
		keywords.put("do","do");
		keywords.put("if","if");
		keywords.put("private","private");
		keywords.put("this","this");
		keywords.put("break","break");
		keywords.put("double","double");
		keywords.put("implements","implements");
		keywords.put("protected","protected");
		keywords.put("throw","throw");
		keywords.put("byte","byte");
		keywords.put("else","else");
		keywords.put("import","import");
		keywords.put("public","public");
		keywords.put("throws","throws");
		keywords.put("case","case");
		keywords.put("enum","enum");
		keywords.put("instanceof","instanceof");
		keywords.put("return","return");
		keywords.put("transient","transient");
		keywords.put("catch","catch");
		keywords.put("extends","extends");
		keywords.put("int","int");
		keywords.put("short","short");
		keywords.put("try","try");
		keywords.put("char","char");
		keywords.put("final","final");
		keywords.put("interface","interface");
		keywords.put("static","static");
		keywords.put("void","void");
		keywords.put("class","class");
		keywords.put("finally","finally");
		keywords.put("long","long");
		keywords.put("strictfp","strictfp");
		keywords.put("volatile","volatile");
		keywords.put("const","const");
		keywords.put("float","float");
		keywords.put("native ","native ");
		keywords.put("super","super");
		keywords.put("while","while");
		keywords.put("goto","goto");
	}
	
	/**
	 * Utility method to check if the given field name is a keyword in Java
	 * 
	 * @param fieldName
	 */
	public static boolean isKeyword(String fieldName)
	{
		return keywords.containsKey(fieldName);
	}
	
	/**
	 * Generatuing the get method name for the given field descriptor
	 * 
	 * @param fieldDescriptor
	 * @return
	 */
	public static String getGetMethodName(FieldDescriptor fieldDescriptor)
	{
		if(fieldDescriptor == null)
		{
			return "null";
		}
		else
		{
			String fieldName = fieldDescriptor.getName();
			StringBuilder propertyName = new StringBuilder();
			
			String declaringClassName = fieldDescriptor.getDeclaringClassDescriptor().getDescribedClassSimpleName();
			
			propertyName.append(JavaTranslationConstants.GET);
			propertyName.append(Character.toUpperCase(fieldName.charAt(0)));
			propertyName.append(fieldName.subSequence(1, fieldName.length()));
						
			return propertyName.toString();
		}			
	}
	
	/**
	 * Generating the set method name for the given field descriptor
	 * 
	 * @param fieldDescriptor
	 * @return
	 */
	public static String getSetMethodName(FieldDescriptor fieldDescriptor)
	{
		if(fieldDescriptor == null)
		{
			return "null";
		}
		else
		{
			String fieldName = fieldDescriptor.getName();
			StringBuilder propertyName = new StringBuilder();
			
			String declaringClassName = fieldDescriptor.getDeclaringClassDescriptor().getDescribedClassSimpleName();
			
			propertyName.append(JavaTranslationConstants.SET);
			propertyName.append(Character.toUpperCase(fieldName.charAt(0)));
			propertyName.append(fieldName.subSequence(1, fieldName.length()));
						
			return propertyName.toString();
		}			
	}

}
