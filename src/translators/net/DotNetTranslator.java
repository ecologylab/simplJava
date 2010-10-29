package translators.net;

import japa.parser.ParseException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import translators.parser.JavaDocParser;
import ecologylab.generic.HashMapArrayList;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.XMLTools;
import ecologylab.serialization.library.rss.Channel;
import ecologylab.serialization.types.element.Mappable;

/**
 * This class is the main class which provides the functionality of translation of Java classes into
 * the C# implementation files.
 * 
 * <p>
 * It uses the same syntactical annotations used the by {@code ecologylab.serialization} to
 * translate Java objects into xml files. Since it uses the same annotations the data types
 * supported for translation are also the same. The entry point functions into the class are.
 * <ul>
 * <li>{@code translateToCSharp(Class<? extends ElementState>, Appendable)}</li>
 * </ul>
 * </p>
 * 
 * @author Nabeel Shahzad
 * @version 1.0
 */
public class DotNetTranslator implements DotNetTranslationConstants
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
	 * This is a way to add specific import namespaces. 
	 * FIXME: There should be a more elegant way to do this.
	 */
	public ArrayList<String>	additionalImportNamespaces;
	
	private boolean	implementMappableInterface = false;

/**
    * The main entry function into the class. Goes through a sequence of steps
    * to convert the Java class file into C# header file. It mainly
    * looks for {@code @xml_attribute} , {@code @xml_collection} and {@code
    * @xml_nested} attributes of the {@code ecologylab.serialization}.
    * <p>
    * This function will <b>not</b> try to generate the header file for the
    * Class whose objects are present in the current Java file and annotated by
    * {@code ecologylab.serialization} attributes.
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
			
			if(implementMappableInterface)
			{
				implementMappableMethods(appendable);
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
	 * @throws SIMPLTranslationException
	 * @throws DotNetTranslationException
	 */
	public void translateToCSharp(File directoryLocation, TranslationScope tScope)
			throws IOException, SIMPLTranslationException, DotNetTranslationException
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
	 * @throws SIMPLTranslationException
	 * @throws ParseException
	 * @throws DotNetTranslationException
	 */
	public void translateToCSharp(File directoryLocation, TranslationScope tScope,
			File workSpaceLocation) throws IOException, SIMPLTranslationException, ParseException,
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
				+ ".cs\n//  s.im.pl serialization\n//\n//  Generated by DotNetTranslator on "
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
				+ FILE_PATH_SEPARATOR;

		String[] arrayPackageNames = packageName
				.split(PACKAGE_NAME_SEPARATOR);

		for (String directoryName : arrayPackageNames)
		{
			currentDirectory += directoryName + FILE_PATH_SEPARATOR;
		}

		File directory = new File(currentDirectory);
		directory.mkdirs();

		File currentFile = new File(currentDirectory + className
				+ FILE_EXTENSION);

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
		appendable.append(USING);
		appendable.append(SPACE);
		appendable.append(SYSTEM);
		appendable.append(END_LINE);

		appendable.append(SINGLE_LINE_BREAK);

		appendable.append(USING);
		appendable.append(SPACE);
		appendable.append(SYSTEM);
		appendable.append(DOT);
		appendable.append(COLLECTIONS);
		appendable.append(DOT);
		appendable.append(GENERIC);
		appendable.append(END_LINE);

		appendable.append(SINGLE_LINE_BREAK);

		appendable.append(USING);
		appendable.append(SPACE);
		appendable.append(ECOLOGYLAB_NAMESPACE);
		appendable.append(END_LINE);

		appendable.append(SINGLE_LINE_BREAK);

		appendable.append(USING);
		appendable.append(SPACE);
		appendable.append(SERIALIZATION_NAMESPACE);
		appendable.append(END_LINE);
	
		if(additionalImportNamespaces != null && additionalImportNamespaces.size() > 0)
		{
			for(String namespace : additionalImportNamespaces)
			{
				appendable.append(SINGLE_LINE_BREAK);
				appendable.append(USING);
				appendable.append(SPACE);
				appendable.append(namespace);
				appendable.append(END_LINE);	
			}
		}
		appendable.append(DOUBLE_LINE_BREAK);
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
		appendable.append(NAMESPACE);
		appendable.append(SPACE);
		appendable.append(inputClass.getPackage().getName());
		appendable.append(SPACE);
		appendable.append(SINGLE_LINE_BREAK);
		appendable.append(OPENING_CURLY_BRACE);
		appendable.append(SINGLE_LINE_BREAK);
	}

	/**
	 * 
	 * @param appendable
	 * @throws IOException
	 */
	private void closeNameSpace(Appendable appendable) throws IOException
	{
		appendable.append(CLOSING_CURLY_BRACE);
		appendable.append(SINGLE_LINE_BREAK);
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
		appendable.append(DOUBLE_TAB);
		appendable.append(PUBLIC);
		appendable.append(SPACE);
		appendable.append(inputClass.getSimpleName());
		appendable.append(OPENING_BRACE);
		appendable.append(CLOSING_BRACE);
		appendable.append(SINGLE_LINE_BREAK);
		appendable.append(DOUBLE_TAB);
		appendable.append(OPENING_CURLY_BRACE);
		appendable.append(SPACE);
		appendable.append(CLOSING_CURLY_BRACE);
		appendable.append(SINGLE_LINE_BREAK);
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
		String cSharpType = fieldDescriptor.getCSharpType();
		if (cSharpType == null)
		{
			System.out.println("ERROR, no valid CSharpType found for : " + fieldDescriptor);
			return;
		}
		appendFieldComments(fieldDescriptor, appendable);
		appendAnnotation(fieldDescriptor, appendable);
		appendable.append(DOUBLE_TAB);
		appendable.append(PRIVATE);
		appendable.append(SPACE);
		appendable.append(cSharpType);
		// appendable.append(DotNetTranslationUtilities.getCSharpType(fieldDescriptor.getField()));
		appendable.append(SPACE);
		appendable.append(fieldDescriptor.getFieldName());
		appendable.append(END_LINE);
		appendable.append(DOUBLE_LINE_BREAK);
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
		appendable.append(DOUBLE_TAB);
		appendable.append(OPEN_COMMENTS);
		appendable.append(SINGLE_LINE_BREAK);

		appendCommentsFromArray(appendable,
				JavaDocParser.getFieldJavaDocsArray(fieldDescriptor.getField()), true);

		appendable.append(DOUBLE_TAB);
		appendable.append(CLOSE_COMMENTS);
		appendable.append(SINGLE_LINE_BREAK);

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
		appendAnnotations(appendable, annotations, DOUBLE_TAB);
	}

	private void appendAnnotations(Appendable appendable, Annotation[] annotations, String tabSpacing)
			throws IOException
	{
		for (Annotation annotation : annotations)
		{
			appendable.append(tabSpacing);
			appendable.append(OPENING_SQUARE_BRACE);
			appendable.append(DotNetTranslationUtilities.getCSharpAnnotation(annotation));
			appendable.append(CLOSING_SQUARE_BRACE);
			appendable.append(SINGLE_LINE_BREAK);
		}
	}

	/**
	 * 
	 * @param fieldDescriptor
	 * @param appendable
	 * @throws IOException 
	 */
	private void appendGettersAndSetters(FieldDescriptor fieldDescriptor, Appendable appendable) throws IOException
	{
		String cSharpType = fieldDescriptor.getCSharpType();
		if (cSharpType == null)
		{
			System.out.println("ERROR, no valid CSharpType found for : " + fieldDescriptor);
			return;
		}
		
		appendable.append(SINGLE_LINE_BREAK);		
		appendable.append(DOUBLE_TAB);
		appendable.append(PUBLIC);
		appendable.append(SPACE);
		appendable.append(cSharpType);
		appendable.append(SPACE);
		appendable.append(DotNetTranslationUtilities.getPropertyName(fieldDescriptor.getFieldName()));
		appendable.append(SINGLE_LINE_BREAK);
		appendable.append(DOUBLE_TAB);		
		appendable.append(OPENING_CURLY_BRACE);
		appendable.append(SINGLE_LINE_BREAK);
		appendable.append(DOUBLE_TAB);		
		appendable.append(TAB);		
		appendable.append(GET);
		appendable.append(SINGLE_LINE_BREAK);
		appendable.append(DOUBLE_TAB);		
		appendable.append(TAB);		
		appendable.append(OPENING_CURLY_BRACE);
		appendable.append(SINGLE_LINE_BREAK);
		appendable.append(DOUBLE_TAB);		
		appendable.append(DOUBLE_TAB);		
		appendable.append(RETURN);
		appendable.append(SPACE);
		appendable.append(fieldDescriptor.getFieldName());
		appendable.append(END_LINE);
		appendable.append(SINGLE_LINE_BREAK);
		appendable.append(DOUBLE_TAB);		
		appendable.append(TAB);		
		appendable.append(CLOSING_CURLY_BRACE);
		appendable.append(SINGLE_LINE_BREAK);
		
		appendable.append(DOUBLE_TAB);		
		appendable.append(TAB);		
		appendable.append(SET);
		appendable.append(SINGLE_LINE_BREAK);
		appendable.append(DOUBLE_TAB);		
		appendable.append(TAB);		
		appendable.append(OPENING_CURLY_BRACE);
		appendable.append(SINGLE_LINE_BREAK);
		appendable.append(DOUBLE_TAB);		
		appendable.append(DOUBLE_TAB);		
		appendable.append(fieldDescriptor.getFieldName());
		appendable.append(SPACE);
		appendable.append(ASSIGN);
		appendable.append(SPACE);
		appendable.append(VALUE);
		appendable.append(END_LINE);
		appendable.append(SINGLE_LINE_BREAK);
		appendable.append(DOUBLE_TAB);		
		appendable.append(TAB);		
		appendable.append(CLOSING_CURLY_BRACE);
		appendable.append(SINGLE_LINE_BREAK);
		
		appendable.append(DOUBLE_TAB);	
		appendable.append(CLOSING_CURLY_BRACE);
		appendable.append(SINGLE_LINE_BREAK);

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
		
		Annotation[] annotations = inputClass.getAnnotations();

		Class genericSuperclass = inputClass.getSuperclass();
		
		appendAnnotations(appendable, annotations, TAB);
		
		appendable.append(TAB);
		appendable.append(PUBLIC);
		appendable.append(SPACE);
		appendable.append(CLASS);
		appendable.append(SPACE);
		appendable.append(inputClass.getSimpleName());
		appendable.append(SPACE);
		appendable.append(INHERITANCE_OPERATOR);
		appendable.append(SPACE);
		appendable.append(genericSuperclass.getSimpleName());
		
		//add interface implementations
		Class<?>[] interfaces = inputClass.getInterfaces();
		
		for(int i = 0; i < interfaces.length; i ++)
		{
			if(interfaces[i].isAssignableFrom(Mappable.class))
			{
				appendable.append(',');
				appendable.append(SPACE);				
				appendable.append(interfaces[i].getSimpleName());
				implementMappableInterface  = true;
			}			
		}
		
		appendable.append(SINGLE_LINE_BREAK);
		appendable.append(TAB);
		appendable.append(OPENING_CURLY_BRACE);
		appendable.append(SINGLE_LINE_BREAK);
	}

	private void appendClassComments(Class<? extends ElementState> inputClass, Appendable appendable)
			throws IOException
	{
		appendable.append(TAB);
		appendable.append(OPEN_COMMENTS);
		appendable.append(SINGLE_LINE_BREAK);

		appendCommentsFromArray(appendable, JavaDocParser.getClassJavaDocsArray(inputClass), false);

		appendable.append(TAB);
		appendable.append(CLOSE_COMMENTS);
		appendable.append(SINGLE_LINE_BREAK);

	}

	private void appendCommentsFromArray(Appendable appendable, String[] javaDocCommentArray,
			boolean doubleTabs) throws IOException
	{
		String numOfTabs = TAB;
		if (doubleTabs)
			numOfTabs = DOUBLE_TAB;

		if (javaDocCommentArray != null)
		{
			for (String comment : javaDocCommentArray)
			{
				appendable.append(numOfTabs);
				appendable.append(XML_COMMENTS);
				appendable.append(comment);
				appendable.append(SINGLE_LINE_BREAK);
			}
		}
		else
		{
			appendable.append(numOfTabs);
			appendable.append(XML_COMMENTS);
			appendable.append("missing java doc comments or could not find the source file.");
			appendable.append(SINGLE_LINE_BREAK);
		}
	}
	
	private void implementMappableMethods(Appendable appendable) throws IOException
	{
		appendable.append(SINGLE_LINE_BREAK);		
		appendable.append(DOUBLE_TAB);
		appendable.append(PUBLIC);
		appendable.append(SPACE);
		appendable.append(DOTNET_OBJECT);
		appendable.append(SPACE);
		appendable.append(KEY);
		appendable.append(OPENING_BRACE);
		appendable.append(CLOSING_BRACE);
		appendable.append(SINGLE_LINE_BREAK);
		appendable.append(DOUBLE_TAB);		
		appendable.append(OPENING_CURLY_BRACE);
		appendable.append(SINGLE_LINE_BREAK);
		appendable.append(DOUBLE_TAB);		
		appendable.append(TAB);	
		appendable.append(DEFAULT_IMPLEMENTATION);
		appendable.append(SINGLE_LINE_BREAK);
		appendable.append(DOUBLE_TAB);					
		appendable.append(CLOSING_CURLY_BRACE);
		appendable.append(SINGLE_LINE_BREAK);
	}

	/**
	 * 
	 * @param appendable
	 * @throws IOException
	 */
	private void closeClassFile(Appendable appendable) throws IOException
	{
		appendable.append(TAB);
		appendable.append(CLOSING_CURLY_BRACE);
		appendable.append(SINGLE_LINE_BREAK);
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

		// c.translateToCSharp(
		// new File("/csharp_output"),
		// TranslationScope.get("RSSTranslations", Schmannel.class, BItem.class, SchmItem.class,
		// RssState.class, Item.class, Channel.class),
		// new File(
		// "/Users/nabeelshahzad/Documents/workspace/ecologylabFundamental/ecologylab/xml/library/rss"));
		c.translateToCSharp(System.out, Channel.class);
	}

	public void setAdditionalImportNamespaces(ArrayList<String> additionalImportLines)
	{
		this.additionalImportNamespaces = additionalImportLines;
	}

}
