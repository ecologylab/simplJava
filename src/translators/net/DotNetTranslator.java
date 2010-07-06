package translators.net;

import japa.parser.ParseException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import translators.parser.JavaDocParser;

import ecologylab.generic.HashMapArrayList;
import ecologylab.standalone.xmlpolymorph.BItem;
import ecologylab.standalone.xmlpolymorph.SchmItem;
import ecologylab.standalone.xmlpolymorph.Schmannel;
import ecologylab.xml.ClassDescriptor;
import ecologylab.xml.ElementState;
import ecologylab.xml.FieldDescriptor;
import ecologylab.xml.TranslationScope;
import ecologylab.xml.XMLTools;
import ecologylab.xml.XMLTranslationException;
import ecologylab.xml.library.rss.Channel;
import ecologylab.xml.library.rss.Item;
import ecologylab.xml.library.rss.RssState;

/**
 * This class is the main class which provides the functionality of translation of Java classes into
 * the C# implementation files.
 * 
 * <p>
 * It uses the same syntactical annotations used the by {@code ecologylab.xml} to translate Java
 * objects into xml files. Since it uses the same annotations the data types supported for
 * translation are also the same. The entry point functions into the class are.
 * <ul>
 * <li>{@code translateToCSharp(Class<? extends ElementState>, Appendable)}</li>
 * </ul>
 * </p>
 * 
 * @author Nabeel Shahzad
 * @version 1.0
 */
public class DotNetTranslator
{
	/**
	 * Constructor method
	 * <p>
	 * Initializes the {@code nestedTranslationHooks} member of the class
	 * </p>
	 */
	public DotNetTranslator()
	{
	}

/**
    * The main entry function into the class. Goes through a sequence of steps
    * to convert the Java class file into C# header file. It mainly
    * looks for {@code @xml_attribute} , {@code @xml_collection} and {@code
    * @xml_nested} attributes of the {@code ecologylab.xml}.
    * <p>
    * This function will <b>not</b> try to generate the header file for the
    * Class whose objects are present in the current Java file and annotated by
    * {@code ecologylab.xml} attributes.
    * </p>
    * 
    * @param inputClass
    * @param appendable
    * @throws IOException
    * @throws DotNetTranslationException
    */
	public void translateToCSharp(Appendable appendable, Class<? extends ElementState>... classes)
			throws IOException, DotNetTranslationException
	{
		int length = classes.length;
		for (int i = 0; i < length; i++)
		{
			translateToCSharp(classes[i], appendable);
		}

	}

	/**
	 * 
	 * @param inputClass
	 * @param appendable
	 * @throws IOException
	 * @throws DotNetTranslationException
	 */
	public void translateToCSharp(Class<? extends ElementState> inputClass, Appendable appendable)
			throws IOException, DotNetTranslationException
	{
		ClassDescriptor<?, ?> classDescriptor = ClassDescriptor.getClassDescriptor(inputClass);

		HashMapArrayList<String, ? extends FieldDescriptor> fieldDescriptors = classDescriptor
				.getFieldDescriptorsByFieldName();

		appendHeaderComments(inputClass, appendable);

		importNameSpaces(appendable);

		openNameSpace(inputClass, appendable);
		openClassFile(inputClass, appendable);

		if (fieldDescriptors.size() > 0)
		{
			classDescriptor.resolveUnresolvedScopeAnnotationFDs();

			for (FieldDescriptor fieldDescriptor : fieldDescriptors)
			{
				if (fieldDescriptor.belongsTo(classDescriptor))
					appendFieldAsCSharpAttribute(fieldDescriptor, appendable);
			}

			appendDefaultConstructor(inputClass, appendable);

			for (FieldDescriptor fieldDescriptor : fieldDescriptors)
			{
				if (fieldDescriptor.belongsTo(classDescriptor))
					appendGettersAndSetters(fieldDescriptor, appendable);
			}
		}

		closeClassFile(appendable);
		closeNameSpace(appendable);
	}

	/**
	 * Takes an input class to generate an C# version of the file. Takes the {@code directoryLocation}
	 * of the files where the file needs to be generated.
	 * <p>
	 * This function internally calls the {@code translateToCSharp} main entry function to generate
	 * the required files
	 * </p>
	 * 
	 * @param inputClass
	 * @param appendable
	 * @throws IOException
	 * @throws XMLTranslationException
	 * @throws DotNetTranslationException
	 */
	public void translateToCSharp(File directoryLocation, TranslationScope tScope)
			throws IOException, XMLTranslationException, DotNetTranslationException
	{
		// Generate header and implementation files
		ArrayList<Class<? extends ElementState>> classes = tScope.getAllClasses();
		int length = classes.size();
		for (int i = 0; i < length; i++)
		{
			translateToCSharp(classes.get(i), directoryLocation);
		}
	}

	/**
	 * 
	 * @param directoryLocation
	 * @param tScope
	 * @param workSpaceLocation
	 * @throws IOException
	 * @throws XMLTranslationException
	 * @throws ParseException
	 * @throws DotNetTranslationException
	 */
	public void translateToCSharp(File directoryLocation, TranslationScope tScope,
			File workSpaceLocation) throws IOException, XMLTranslationException, ParseException,
			DotNetTranslationException
	{
		// Parse source files for javadocs
		JavaDocParser.parseSourceFileIfExists(tScope, workSpaceLocation);

		// Generate header and implementation files
		ArrayList<Class<? extends ElementState>> classes = tScope.getAllClasses();
		int length = classes.size();
		for (int i = 0; i < length; i++)
		{
			translateToCSharp(classes.get(i), directoryLocation);
		}
	}

	/**
	 * Takes an input class to generate an C# version of the file. Takes the {@code directoryLocation}
	 * of the files where the file needs to be generated.
	 * <p>
	 * This function internally calls the {@code translateToCSharp} main entry function to generate
	 * the required files
	 * </p>
	 * 
	 * @param inputClass
	 * @param appendable
	 * @throws IOException
	 * @throws DotNetTranslationException
	 */
	private void translateToCSharp(Class<? extends ElementState> inputClass, File directoryLocation)
			throws IOException, DotNetTranslationException
	{
		File outputFile = createCSharpFileWithDirectoryStructure(inputClass, directoryLocation);
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));

		translateToCSharp(inputClass, bufferedWriter);
		bufferedWriter.close();
	}

	/**
	 * 
	 * @param inputClass
	 * @param appendable
	 * @throws IOException
	 */
	private void appendHeaderComments(Class<? extends ElementState> inputClass, Appendable appendable)
			throws IOException
	{
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
		DateFormat yearFormat = new SimpleDateFormat("yyyy");

		Date date = new Date();
		appendable.append("//\n//  " + inputClass.getSimpleName()
				+ ".cs\n//  ecologylabXML\n//\n//  Generated by DotNetTranslator on "
				+ dateFormat.format(date) + ".\n//  Copyright " + yearFormat.format(date)
				+ " Interface Ecology Lab. \n//\n\n");
	}

	/**
	 * Creates a directory structure from the path of the given by the {@code directoryLocation}
	 * parameter Uses the class and package names from the parameter {@code inputClass}
	 * <p>
	 * This function deletes the files if the files with same class existed inside the directory
	 * structure and creates a new file for that class
	 * </p>
	 * 
	 * @param inputClass
	 * @param directoryLocation
	 * @return
	 * @throws IOException
	 */
	private File createCSharpFileWithDirectoryStructure(Class<?> inputClass, File directoryLocation)
			throws IOException
	{
		String packageName = XMLTools.getPackageName(inputClass);
		String className = XMLTools.getClassName(inputClass);
		String currentDirectory = directoryLocation.toString()
				+ DotNetTranslationConstants.FILE_PATH_SEPARATOR;

		String[] arrayPackageNames = packageName
				.split(DotNetTranslationConstants.PACKAGE_NAME_SEPARATOR);

		for (String directoryName : arrayPackageNames)
		{
			currentDirectory += directoryName + DotNetTranslationConstants.FILE_PATH_SEPARATOR;
		}

		File directory = new File(currentDirectory);
		directory.mkdirs();

		File currentFile = new File(currentDirectory + className
				+ DotNetTranslationConstants.FILE_EXTENSION);

		if (currentFile.exists())
		{
			currentFile.delete();
		}

		currentFile.createNewFile();
		return currentFile;
	}

	/**
	 * 
	 * @param appendable
	 * @throws IOException
	 */
	private void importNameSpaces(Appendable appendable) throws IOException
	{
		appendable.append(DotNetTranslationConstants.USING);
		appendable.append(DotNetTranslationConstants.SPACE);
		appendable.append(DotNetTranslationConstants.SYSTEM);
		appendable.append(DotNetTranslationConstants.END_LINE);

		appendable.append(DotNetTranslationConstants.SINGLE_LINE_BREAK);

		appendable.append(DotNetTranslationConstants.USING);
		appendable.append(DotNetTranslationConstants.SPACE);
		appendable.append(DotNetTranslationConstants.SYSTEM);
		appendable.append(DotNetTranslationConstants.DOT);
		appendable.append(DotNetTranslationConstants.COLLECTIONS);
		appendable.append(DotNetTranslationConstants.DOT);
		appendable.append(DotNetTranslationConstants.GENERIC);
		appendable.append(DotNetTranslationConstants.END_LINE);

		appendable.append(DotNetTranslationConstants.SINGLE_LINE_BREAK);

		appendable.append(DotNetTranslationConstants.USING);
		appendable.append(DotNetTranslationConstants.SPACE);
		appendable.append(DotNetTranslationConstants.ECOLOGYLAB_NAMESPACE);
		appendable.append(DotNetTranslationConstants.END_LINE);

		appendable.append(DotNetTranslationConstants.DOUBLE_LINE_BREAK);
	}

	/**
	 * 
	 * @param inputClass
	 * @param appendable
	 * @throws IOException
	 */
	private void openNameSpace(Class<? extends ElementState> inputClass, Appendable appendable)
			throws IOException
	{
		appendable.append(DotNetTranslationConstants.NAMESPACE);
		appendable.append(DotNetTranslationConstants.SPACE);
		appendable.append(inputClass.getPackage().getName());
		appendable.append(DotNetTranslationConstants.SPACE);
		appendable.append(DotNetTranslationConstants.SINGLE_LINE_BREAK);
		appendable.append(DotNetTranslationConstants.OPENING_CURLY_BRACE);
		appendable.append(DotNetTranslationConstants.SINGLE_LINE_BREAK);
	}

	/**
	 * 
	 * @param appendable
	 * @throws IOException
	 */
	private void closeNameSpace(Appendable appendable) throws IOException
	{
		appendable.append(DotNetTranslationConstants.CLOSING_CURLY_BRACE);
		appendable.append(DotNetTranslationConstants.SINGLE_LINE_BREAK);
	}

	/**
	 * 
	 * @param inputClass
	 * @param appendable
	 * @throws IOException
	 */
	private void appendDefaultConstructor(Class<? extends ElementState> inputClass,
			Appendable appendable) throws IOException
	{
		appendable.append(DotNetTranslationConstants.DOUBLE_TAB);
		appendable.append(DotNetTranslationConstants.PUBLIC);
		appendable.append(DotNetTranslationConstants.SPACE);
		appendable.append(inputClass.getSimpleName());
		appendable.append(DotNetTranslationConstants.OPENING_BRACE);
		appendable.append(DotNetTranslationConstants.CLOSING_BRACE);
		appendable.append(DotNetTranslationConstants.SINGLE_LINE_BREAK);
		appendable.append(DotNetTranslationConstants.DOUBLE_TAB);
		appendable.append(DotNetTranslationConstants.OPENING_CURLY_BRACE);
		appendable.append(DotNetTranslationConstants.SPACE);
		appendable.append(DotNetTranslationConstants.CLOSING_CURLY_BRACE);
		appendable.append(DotNetTranslationConstants.SINGLE_LINE_BREAK);
	}

	/**
	 * 
	 * @param fieldDescriptor
	 * @param appendable
	 * @throws IOException
	 * @throws DotNetTranslationException
	 */
	private void appendFieldAsCSharpAttribute(FieldDescriptor fieldDescriptor, Appendable appendable)
			throws IOException, DotNetTranslationException
	{
		appendFieldComments(fieldDescriptor, appendable);
		appendAnnotation(fieldDescriptor, appendable);
		appendable.append(DotNetTranslationConstants.DOUBLE_TAB);
		appendable.append(DotNetTranslationConstants.PRIVATE);
		appendable.append(DotNetTranslationConstants.SPACE);
		appendable.append(fieldDescriptor.getCSharpType());
//		appendable.append(DotNetTranslationUtilities.getCSharpType(fieldDescriptor.getField()));
		appendable.append(DotNetTranslationConstants.SPACE);
		appendable.append(fieldDescriptor.getFieldName());
		appendable.append(DotNetTranslationConstants.END_LINE);
		appendable.append(DotNetTranslationConstants.DOUBLE_LINE_BREAK);
	}

	/**
	 * 
	 * @param fieldDescriptor
	 * @param appendable
	 * @throws IOException
	 */
	private void appendFieldComments(FieldDescriptor fieldDescriptor, Appendable appendable)
			throws IOException
	{
		appendable.append(DotNetTranslationConstants.DOUBLE_TAB);
		appendable.append(DotNetTranslationConstants.OPEN_COMMENTS);
		appendable.append(DotNetTranslationConstants.SINGLE_LINE_BREAK);

		appendCommentsFromArray(appendable, JavaDocParser.getFieldJavaDocsArray(fieldDescriptor
				.getField()), true);

		appendable.append(DotNetTranslationConstants.DOUBLE_TAB);
		appendable.append(DotNetTranslationConstants.CLOSE_COMMENTS);
		appendable.append(DotNetTranslationConstants.SINGLE_LINE_BREAK);

	}

	/**
	 * 
	 * @param fieldDescriptor
	 * @param appendable
	 * @throws IOException
	 */
	private void appendAnnotation(FieldDescriptor fieldDescriptor, Appendable appendable)
			throws IOException
	{
		Annotation[] annotations = fieldDescriptor.getField().getAnnotations();
		for (Annotation annotation : annotations)
		{
			appendable.append(DotNetTranslationConstants.DOUBLE_TAB);
			appendable.append(DotNetTranslationConstants.OPENING_SQUARE_BRACE);
			appendable.append(DotNetTranslationUtilities.getCSharpAnnotation(annotation));
			appendable.append(DotNetTranslationConstants.CLOSING_SQUARE_BRACE);
			appendable.append(DotNetTranslationConstants.SINGLE_LINE_BREAK);
		}
	}

	/**
	 * 
	 * @param fieldDescriptor
	 * @param appendable
	 */
	private void appendGettersAndSetters(FieldDescriptor fieldDescriptor, Appendable appendable)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * @param inputClass
	 * @param appendable
	 * @throws IOException
	 */
	private void openClassFile(Class<? extends ElementState> inputClass, Appendable appendable)
			throws IOException
	{
		appendClassComments(inputClass, appendable);
		appendable.append(DotNetTranslationConstants.TAB);
		appendable.append(DotNetTranslationConstants.PUBLIC);
		appendable.append(DotNetTranslationConstants.SPACE);
		appendable.append(DotNetTranslationConstants.CLASS);
		appendable.append(DotNetTranslationConstants.SPACE);
		appendable.append(inputClass.getSimpleName());
		appendable.append(DotNetTranslationConstants.SPACE);
		appendable.append(DotNetTranslationConstants.INHERITANCE_OPERATOR);
		appendable.append(DotNetTranslationConstants.SPACE);
		appendable.append(DotNetTranslationConstants.INHERITANCE_OBJECT);
		appendable.append(DotNetTranslationConstants.SINGLE_LINE_BREAK);
		appendable.append(DotNetTranslationConstants.TAB);
		appendable.append(DotNetTranslationConstants.OPENING_CURLY_BRACE);
		appendable.append(DotNetTranslationConstants.SINGLE_LINE_BREAK);
	}

	private void appendClassComments(Class<? extends ElementState> inputClass, Appendable appendable)
			throws IOException
	{
		appendable.append(DotNetTranslationConstants.TAB);
		appendable.append(DotNetTranslationConstants.OPEN_COMMENTS);
		appendable.append(DotNetTranslationConstants.SINGLE_LINE_BREAK);

		appendCommentsFromArray(appendable, JavaDocParser.getClassJavaDocsArray(inputClass), false);

		appendable.append(DotNetTranslationConstants.TAB);
		appendable.append(DotNetTranslationConstants.CLOSE_COMMENTS);
		appendable.append(DotNetTranslationConstants.SINGLE_LINE_BREAK);

	}

	private void appendCommentsFromArray(Appendable appendable, String[] javaDocCommentArray,
			boolean doubleTabs) throws IOException
	{
		String numOfTabs = DotNetTranslationConstants.TAB;
		if (doubleTabs)
			numOfTabs = DotNetTranslationConstants.DOUBLE_TAB;
		
		if (javaDocCommentArray != null)
		{
			for (String comment : javaDocCommentArray)
			{
				appendable.append(numOfTabs);
				appendable.append(DotNetTranslationConstants.XML_COMMENTS);
				appendable.append(comment);
				appendable.append(DotNetTranslationConstants.SINGLE_LINE_BREAK);
			}
		}
		else
		{
			appendable.append(numOfTabs);
			appendable.append(DotNetTranslationConstants.XML_COMMENTS);
			appendable.append("missing java doc comments or could not find the source file.");
			appendable.append(DotNetTranslationConstants.SINGLE_LINE_BREAK);
		}
	}

	/**
	 * 
	 * @param appendable
	 * @throws IOException
	 */
	private void closeClassFile(Appendable appendable) throws IOException
	{
		appendable.append(DotNetTranslationConstants.TAB);
		appendable.append(DotNetTranslationConstants.CLOSING_CURLY_BRACE);
		appendable.append(DotNetTranslationConstants.SINGLE_LINE_BREAK);

	}

	/**
	 * Main method to test the working of the library.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception
	{
		DotNetTranslator c = new DotNetTranslator();

//		c.translateToCSharp(
//						new File("/csharp_output"),
//						TranslationScope.get("RSSTranslations", Schmannel.class, BItem.class, SchmItem.class,
//								RssState.class, Item.class, Channel.class),
//						new File(
//								"/Users/nabeelshahzad/Documents/workspace/ecologylabFundamental/ecologylab/xml/library/rss"));
		c.translateToCSharp(System.out, Channel.class);
	}
}
