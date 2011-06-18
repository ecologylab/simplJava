package ecologylab.serialization;

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
import java.net.URL;
import java.util.HashMap;

public class JavaDocumentParser {
	
	private String classComment;
	private HashMap<String, String> fieldComments;
	private Class javaClass;
	
	public JavaDocumentParser(Class javaClass)
	{
		this.javaClass = javaClass;
	}
	
	public boolean Parse() throws Exception
	{
		fieldComments = new HashMap<String, String>();
		String path = getJavaFilePath();
		return parse(new File(path));
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
	public boolean parse(File file) throws ParseException, IOException
	{
		CompilationUnit cu;
		cu = JavaParser.parse(file);
		new FieldVisitor().visit(cu, null);

		return true;
	}
	
	/**
	 * Simple visitor implementation for visiting Field Visitor nodes.
	 */
	@SuppressWarnings("unchecked")
	private class FieldVisitor extends VoidVisitorAdapter
	{
		@Override
		public void visit(ClassOrInterfaceDeclaration n, Object arg)
		{
			if (n != null)
			{
				String currentClass = n.getName();
				
				if (n.getJavaDoc() != null)
				{
					classComment = escape(n.getJavaDoc().toString(), 4);					
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
				//HashMap<String, String> fieldComments = javaDocFieldComments.get(currentClass);

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
	
	public String getClassComment()
	{
		return classComment;
	}
	
	public HashMap<String, String> getFieldComments()
	{
		return fieldComments;
	}
	
	public String getFieldComment(Field field)
	{
		return fieldComments.get(field.getName());		
	}
	
	private String getJavaFilePath()
	{
		URL location;
		String classLocation = javaClass.getName().replace('.', '/') + ".java";
		final ClassLoader loader = javaClass.getClassLoader();
		if (loader == null) 
		{
			location = null;
			System.out.println("Cannot load the class");
		} else 
		{
			location = loader.getResource(classLocation);
			System.out.println("Class "+location);
		}
		return (location==null)?"":location.getPath();
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
