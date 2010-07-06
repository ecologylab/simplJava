package translators.parser;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationScope;

/**
 * This class is used to parse java source file and mantain optimized datastructure to hold comments
 * on class and fields.
 * 
 * @author nabeel
 */
public class JavaDocParser
{
	/**
	 * Hashmap maps class names to hashmap of fields to java docs on fields
	 */
	protected static HashMap<String, HashMap<String, String>>	javaDocFieldComments	= new HashMap<String, HashMap<String, String>>();

	/**
	 * Hashmap maps the comments on class to their class names.
	 */
	protected static HashMap<String, String>									javaDocClassComments	= new HashMap<String, String>();

	/**
	 * Simple visitor implementation for visiting Field Visitor nodes.
	 */
	@SuppressWarnings("unchecked")
	private static class FieldVisitor extends VoidVisitorAdapter
	{
		@Override
		public void visit(ClassOrInterfaceDeclaration n, Object arg)
		{
			if (n != null)
			{
				String currentClass = n.getName();
				javaDocFieldComments.put(currentClass, new HashMap<String, String>());

				if (n.getJavaDoc() != null)
				{
					javaDocClassComments.put(currentClass, escape(n.getJavaDoc().toString(), 4));
				}

				if (n.getMembers() != null)
				{
					for (BodyDeclaration member : n.getMembers())
					{
						member.accept(this, currentClass);
					}
				}
			}
		}

		@Override
		public void visit(FieldDeclaration n, Object arg)
		{
			if (n != null && n.getJavaDoc() != null && arg instanceof String)
			{
				String currentClass = (String) arg;
				HashMap<String, String> fieldComments = javaDocFieldComments.get(currentClass);

				if (fieldComments != null)
				{
					for (VariableDeclarator v : n.getVariables())
					{
						fieldComments.put(v.toString(), escape(n.getJavaDoc().toString(), 5));
					}
				}
			}
		}
	}

	public static String[] getClassJavaDocsArray(Class<? extends ElementState> inputClass)
	{
		String javaDocs = getClassJavaDocs(inputClass);
		if (javaDocs != null)
		{
			return escapeToArray(javaDocs);
		}
		else
			return null;
	}
	
	public static String[] getFieldJavaDocsArray(Field field)
	{
		String javaDocs = getFieldJavaDocs(field);
		if (javaDocs != null)
		{
			return escapeToArray(javaDocs);
		}
		else
			return null;
	}

	/**
	 * Gets the class level javadocs on the input class file. This method is used in conjunction with
	 * the <code> static void parse(File file) throws ParseException, IOException </code> method.
	 * 
	 * @param inputClass
	 * @return String
	 */
	public static String getClassJavaDocs(Class<? extends ElementState> inputClass)
	{
		return getClassJavaDocs(inputClass.getSimpleName());
	}

	/**
	 * Gets the class level javadocs on the input class. This method is used in conjunction with the
	 * <code> static void parse(File file) throws ParseException, IOException </code> method.
	 * 
	 * @param className
	 * @return
	 */
	public static String getClassJavaDocs(String className)
	{
		return javaDocClassComments.get(className);
	}

	/**
	 * Gets the field level javadocs comments on the input file. This method is used in conjunction
	 * with the <code> static void parse(File file) throws ParseException, IOException </code> method.
	 * 
	 * @param thatField
	 * @return
	 */
	public static String getFieldJavaDocs(Field thatField)
	{
		Class<?> thatClass = thatField.getDeclaringClass();
		return getFieldJavaDocs(thatClass.getSimpleName(), thatField.getName());
	}

	/**
	 * @param className
	 * @param fieldName
	 * @return
	 */
	public static String getFieldJavaDocs(String className, String fieldName)
	{
		String javaDocs = null;
		HashMap<String, String> fieldCommentsMap = javaDocFieldComments.get(className);
		if (fieldCommentsMap != null)
		{
			javaDocs = fieldCommentsMap.get(fieldName);
		}
		return javaDocs;
	}

	/**
	 * Parses the input file and fills up the internal optimized data structures with formatted
	 * comments in the given java source file.
	 * 
	 * @param file
	 * @throws ParseException
	 * @throws IOException
	 * @return True if the parse was successful
	 */
	@SuppressWarnings("unchecked")
	public static boolean parse(File file) throws ParseException, IOException
	{
		CompilationUnit cu;
		cu = JavaParser.parse(file);
		new FieldVisitor().visit(cu, null);

		return true;
	}

	public static void parseSourceFileIfExists(TranslationScope translationScope,
			File workSpaceLocation) throws ParseException, IOException
	{
		ArrayList<Class<? extends ElementState>> classes = translationScope.getAllClasses();
		int length = classes.size();

		for (int i = 0; i < length; i++)
		{
			parseSourceFileIfExists(classes.get(i), workSpaceLocation);
		}
	}

	private static void parseSourceFileIfExists(Class<? extends ElementState> inputClass,
			File workSpaceLocation) throws ParseException, IOException
	{
		if (workSpaceLocation != null)
		{
			File sourceFile = findFile(inputClass, workSpaceLocation);
			if (sourceFile != null)
			{
				parse(sourceFile);
			}
		}
	}

	private static File findFile(Class<? extends ElementState> inputClass, File workSpaceLocation)
			throws IOException
	{
		FileTraversal fileTraversal = new FileTraversal();
		return fileTraversal.searchForFile(inputClass, workSpaceLocation);
	}

	/**
	 * Escapes invisible characters from the comments and does tab formatting on the comments.
	 * 
	 * @param javaDocs
	 * @param numOfTabs
	 * @return String : formatted String comment
	 */
	private static String escape(String javaDocs, int numOfTabs)
	{
		String tabString = makeTabString(numOfTabs);

		String strippedComments = javaDocs.replace("*", "").replace("/", "").trim();
		String[] commentsArray = strippedComments.split("\n");

		StringBuilder finalComment = new StringBuilder();

		for (String part : commentsArray)
		{
			part = part.trim();
			if (!part.isEmpty())
				finalComment.append(part).append("\n" + tabString);
		}

		return finalComment.toString().trim();
	}
	
	private static String[] escapeToArray(String javaDocs)
	{
		String strippedComments = javaDocs.replace("*", "").replace("/", "").trim();
		String[] commentsArray = strippedComments.split("\n");


		for(int i=0; i<commentsArray.length; i++)
		{
			commentsArray[i] = commentsArray[i].trim();
		}

		return commentsArray;
	}

	/**
	 * Makes a string of tabs used for formatting
	 * 
	 * @param numOfTabs
	 * @return String
	 */
	private static String makeTabString(int numOfTabs)
	{
		StringBuilder tabString = new StringBuilder();
		for (int i = 0; i < numOfTabs; i++)
		{
			tabString.append('\t');
		}
		return tabString.toString();
	}
}
