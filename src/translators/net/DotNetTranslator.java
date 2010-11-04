package translators.net;

import japa.parser.ParseException;
import japa.parser.ast.type.Type;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.TypeVariable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import translators.cocoa.CocoaTranslationConstants;
import translators.parser.JavaDocParser;
import ecologylab.generic.Debug;
import ecologylab.generic.HashMapArrayList;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.XMLTools;
import ecologylab.serialization.library.rss.Channel;
import ecologylab.serialization.library.rss.Item;
import ecologylab.serialization.library.rss.RssState;
import ecologylab.serialization.types.element.Mappable;
import ecologylab.standalone.xmlpolymorph.BItem;
import ecologylab.standalone.xmlpolymorph.SchmItem;
import ecologylab.standalone.xmlpolymorph.Schmannel;

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

	private HashMap<String, String>	libraryNamespaces						= new HashMap<String, String>();

	private HashMap<String, String>	allNamespaces								= new HashMap<String, String>();

	private String									currentNamespace;

	private boolean									implementMappableInterface	= false;

	private ArrayList<String>				additionalImportLines;

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
	private void translateToCSharp(Appendable appendable, Class<? extends ElementState>... classes)
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
	private void translateToCSharp(Class<? extends ElementState> inputClass, Appendable appendable)
			throws IOException, DotNetTranslationException
	{
		ClassDescriptor<?, ?> classDescriptor = ClassDescriptor.getClassDescriptor(inputClass);

		HashMapArrayList<String, ? extends FieldDescriptor> fieldDescriptors = classDescriptor
				.getFieldDescriptorsByFieldName();

		StringBuilder classFile = new StringBuilder();
		StringBuilder header = new StringBuilder();

		appendHeaderComments(inputClass, header);

		openNameSpace(inputClass, classFile);
		openClassFile(inputClass, classFile);

		if (fieldDescriptors.size() > 0)
		{
			classDescriptor.resolveUnresolvedScopeAnnotationFDs();

			for (FieldDescriptor fieldDescriptor : fieldDescriptors)
			{
				if (fieldDescriptor.belongsTo(classDescriptor))
					appendFieldAsCSharpAttribute(fieldDescriptor, classFile);
			}

			appendDefaultConstructor(inputClass, classFile);

			for (FieldDescriptor fieldDescriptor : fieldDescriptors)
			{
				if (fieldDescriptor.belongsTo(classDescriptor))
					appendGettersAndSetters(fieldDescriptor, classFile);
			}

			if (implementMappableInterface)
			{
				implementMappableMethods(classFile);
				implementMappableInterface = false;
			}
		}

		closeClassFile(classFile);
		closeNameSpace(classFile);

		importNameSpaces(header);

		libraryNamespaces.clear();
		setAdditionalImportNamespaces(additionalImportLines);

		appendable.append(header);
		appendable.append(classFile);
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
		ArrayList<Class<? extends ElementState>> classes = TranslationScope.augmentTranslationScope(
				tScope).getAllClasses();
		int length = classes.size();
		for (int i = 0; i < length; i++)
		{
			translateToCSharp(classes.get(i), directoryLocation);
		}
		
		// create a folder to put the translation scope getter class
		File tScopeDirectory = createGetTranslationScopeFolder(directoryLocation);
		// generate translation scope getter class
		generateTranslationScopeGetterClass(tScopeDirectory, tScope);
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
			File workSpaceLocation) throws IOException, SIMPLTranslationException,
			DotNetTranslationException
	{
		System.out.println("Parsing source files to extract comments");

		TranslationScope anotherScope = TranslationScope.augmentTranslationScope(tScope);
		// Parse source files for javadocs
		JavaDocParser.parseSourceFileIfExists(anotherScope, workSpaceLocation);

		System.out.println("generating classes...");

		// Generate header and implementation files
		ArrayList<Class<? extends ElementState>> classes = anotherScope.getAllClasses();
		int length = classes.size();
		for (int i = 0; i < length; i++)
		{
			translateToCSharp(classes.get(i), directoryLocation);
		}

		// create a folder to put the translation scope getter class
		File tScopeDirectory = createGetTranslationScopeFolder(directoryLocation);
		// generate translation scope getter class
		generateTranslationScopeGetterClass(tScopeDirectory, tScope);

		System.out.println("DONE !");
	}

	private void generateTranslationScopeGetterClass(File directoryLocation, TranslationScope tScope)
			throws IOException
	{
		File sourceFile = new File(directoryLocation + FILE_PATH_SEPARATOR + tScope.getName()
				+ FILE_EXTENSION);
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(sourceFile));

		importDefaultNamespaces(bufferedWriter);
		importAllNamespaces(bufferedWriter);

		namespaceComments(bufferedWriter);
		
		
		bufferedWriter.append(SINGLE_LINE_BREAK);
		
		openNameSpace(tScope.getPackageName(), bufferedWriter);

		openTranslationScopeClassFile(tScope.getName(), bufferedWriter);

		appendDefaultConstructor(tScope.getName(), bufferedWriter);
		appendTranslationScopeGetterFunction(FGET, bufferedWriter, tScope);
		
		closeClassFile(bufferedWriter);
		closeNameSpace(bufferedWriter);
		bufferedWriter.close();

	}

	private void namespaceComments(Appendable appendable) throws IOException
	{
		appendable.append("//developer should modify the namespace");
		appendable.append(SINGLE_LINE_BREAK);
		appendable.append("//by default it falls into ecologylab.serialization");
	}
	
	private void appendTranslationScopeGetterFunction(String functionName, Appendable appendable, TranslationScope tScope) throws IOException
	{
		appendable.append(SINGLE_LINE_BREAK);
		appendable.append(DOUBLE_TAB);
		appendable.append(PUBLIC);
		appendable.append(SPACE);
		appendable.append(STATIC);
		appendable.append(SPACE);
		appendable.append(DOTNET_TRANSLATION_SCOPE);
		appendable.append(SPACE);
		appendable.append(functionName);
		appendable.append(OPENING_BRACE);
		appendable.append(CLOSING_BRACE);
		appendable.append(SINGLE_LINE_BREAK);
		appendable.append(DOUBLE_TAB);
		appendable.append(OPENING_CURLY_BRACE);
		appendable.append(SINGLE_LINE_BREAK);
		
		appendable.append(DOUBLE_TAB);
		appendable.append(TAB);
		appendable.append(RETURN);
		appendable.append(SPACE);
		appendable.append(DOTNET_TRANSLATION_SCOPE);
		appendable.append(DOT);
		appendable.append(FGET);
		appendable.append(OPENING_BRACE);
		appendable.append(QUOTE);
		appendable.append(tScope.getName());
		appendable.append(QUOTE);
				
		ArrayList<Class<? extends ElementState>> allClasses = tScope.getAllClasses();		
		for(Class<? extends ElementState> myClass : allClasses)
		{
			appendable.append(COMMA);
			appendable.append(SINGLE_LINE_BREAK);
			appendable.append(DOUBLE_TAB);
			appendable.append(DOUBLE_TAB);
			appendable.append(TYPE_OF);
			appendable.append(OPENING_BRACE);
			appendable.append(myClass.getSimpleName());
			appendable.append(CLOSING_BRACE);
		}
		
		appendable.append(CLOSING_BRACE);
		appendable.append(END_LINE);
		appendable.append(SINGLE_LINE_BREAK);
		appendable.append(DOUBLE_TAB);
		appendable.append(CLOSING_CURLY_BRACE);
		appendable.append(SINGLE_LINE_BREAK);
	}

	private void openTranslationScopeClassFile(String className, Appendable appendable)
			throws IOException
	{
		appendable.append(TAB);
		appendable.append(PUBLIC);
		appendable.append(SPACE);
		appendable.append(CLASS);
		appendable.append(SPACE);
		appendable.append(className);
		
		appendable.append(SINGLE_LINE_BREAK);
		appendable.append(TAB);
		appendable.append(OPENING_CURLY_BRACE);
		appendable.append(SINGLE_LINE_BREAK);
	}

	private void importAllNamespaces(Appendable appendable) throws IOException
	{
		// append all the registered namespace
		if (allNamespaces != null && allNamespaces.size() > 0)
		{
			for (String namespace : allNamespaces.values())
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

	private File createGetTranslationScopeFolder(File directoryLocation)
	{
		String tScopeDirectoryPath = directoryLocation.toString() + FILE_PATH_SEPARATOR;

		tScopeDirectoryPath += TRANSATIONSCOPE_FOLDER + FILE_PATH_SEPARATOR;
		File tScopeDirectory = new File(tScopeDirectoryPath);
		tScopeDirectory.mkdir();
		return tScopeDirectory;
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
		String currentDirectory = directoryLocation.toString() + FILE_PATH_SEPARATOR;

		String[] arrayPackageNames = packageName.split(PACKAGE_NAME_SEPARATOR);

		for (String directoryName : arrayPackageNames)
		{
			currentDirectory += directoryName + FILE_PATH_SEPARATOR;
		}

		File directory = new File(currentDirectory);
		directory.mkdirs();

		File currentFile = new File(currentDirectory + className + FILE_EXTENSION);

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
		importDefaultNamespaces(appendable);

		// append all the registered namespace
		if (libraryNamespaces != null && libraryNamespaces.size() > 0)
		{
			for (String namespace : libraryNamespaces.values())
			{
				// do not append if it belogns to current namespace
				if (!namespace.equals(currentNamespace))
				{
					appendable.append(SINGLE_LINE_BREAK);
					appendable.append(USING);
					appendable.append(SPACE);
					appendable.append(namespace);
					appendable.append(END_LINE);
				}
			}
		}

		appendable.append(DOUBLE_LINE_BREAK);
	}

	private void importDefaultNamespaces(Appendable appendable) throws IOException
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
		openNameSpace(inputClass.getPackage().getName(), appendable);
	}

	/**
	 * 
	 * @param inputClass
	 * @param appendable
	 * @throws IOException
	 */
	private void openNameSpace(String classNameSpace, Appendable appendable) throws IOException
	{
		currentNamespace = classNameSpace;
		allNamespaces.put(currentNamespace, currentNamespace);
		
		appendable.append(NAMESPACE);
		appendable.append(SPACE);
		appendable.append(currentNamespace);
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
		appendDefaultConstructor(inputClass.getSimpleName(), appendable);
	}
	
	/**
	 * 
	 * @param inputClass
	 * @param appendable
	 * @throws IOException
	 */
	private void appendDefaultConstructor(String className,
			Appendable appendable) throws IOException
	{
		appendable.append(DOUBLE_TAB);
		appendable.append(PUBLIC);
		appendable.append(SPACE);
		appendable.append(className);
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
		registerNamespaces(fieldDescriptor);

		String cSharpType = fieldDescriptor.getCSharpType();
		if (cSharpType == null)
		{
			System.out.println("ERROR, no valid CSharpType found for : " + fieldDescriptor);
			return;
		}

		boolean isKeyword = checkForKeywords(fieldDescriptor, appendable);
		appendComments(appendable, true, isKeyword);

		appendFieldComments(fieldDescriptor, appendable);
		appendAnnotation(fieldDescriptor, appendable);
		appendable.append(DOUBLE_TAB);
		appendable.append(PRIVATE);
		appendable.append(SPACE);
		appendable.append(cSharpType);
		appendable.append(SPACE);
		appendable.append(fieldDescriptor.getFieldName());
		appendable.append(END_LINE);
		appendable.append(DOUBLE_LINE_BREAK);

		appendComments(appendable, false, isKeyword);
	}

	private void registerNamespaces(FieldDescriptor fieldDescriptor)
	{
		ArrayList<Class<?>> genericClasses = XMLTools.getGenericParameters(fieldDescriptor.getField());
		Class typeClass = fieldDescriptor.getFieldType();

		if (genericClasses != null)
			for (Class genericClass : genericClasses)
			{
				if (ElementState.class.isAssignableFrom(genericClass))
				{
					libraryNamespaces.put(genericClass.getPackage().getName(), genericClass.getPackage()
							.getName());
					allNamespaces.put(genericClass.getPackage().getName(), genericClass.getPackage()
							.getName());
				}
			}

		if (typeClass != null)
		{
			if (ElementState.class.isAssignableFrom(typeClass))
			{
				libraryNamespaces.put(typeClass.getPackage().getName(), typeClass.getPackage().getName());
				allNamespaces.put(typeClass.getPackage().getName(), typeClass.getPackage().getName());
			}
		}
	}

	private boolean checkForKeywords(FieldDescriptor fieldAccessor, Appendable appendable)
			throws IOException
	{
		if (DotNetTranslationUtilities.isKeyword(fieldAccessor.getFieldName()))
		{
			Debug.warning(fieldAccessor, " Field Name: [" + fieldAccessor.getFieldName()
					+ "]. This is a keyword in C#. Cannot translate");
			return true;
		}

		return false;
	}

	private void appendComments(Appendable appendable, boolean start, boolean isKeywrord)
			throws IOException
	{
		if (isKeywrord)
			if (start)
			{
				appendable.append("/*");
				appendable.append(SINGLE_LINE_BREAK);
			}
			else
			{
				appendable.append("*/");
				appendable.append(SINGLE_LINE_BREAK);
			}
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

		appendCommentsFromArray(appendable, JavaDocParser.getFieldJavaDocsArray(fieldDescriptor
				.getField()), true);

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
	private void appendGettersAndSetters(FieldDescriptor fieldDescriptor, Appendable appendable)
			throws IOException
	{
		String cSharpType = fieldDescriptor.getCSharpType();
		if (cSharpType == null)
		{
			System.out.println("ERROR, no valid CSharpType found for : " + fieldDescriptor);
			return;
		}

		appendable.append(SINGLE_LINE_BREAK);

		boolean isKeyword = checkForKeywords(fieldDescriptor, appendable);
		appendComments(appendable, true, isKeyword);

		appendable.append(DOUBLE_TAB);
		appendable.append(PUBLIC);
		appendable.append(SPACE);
		appendable.append(cSharpType);
		appendable.append(SPACE);
		appendable.append(DotNetTranslationUtilities.getPropertyName(fieldDescriptor));
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

		appendComments(appendable, false, isKeyword);

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

		Class<?> genericSuperclass = inputClass.getSuperclass();

		appendAnnotations(appendable, annotations, TAB);

		appendable.append(TAB);
		appendable.append(PUBLIC);
		appendable.append(SPACE);
		appendable.append(CLASS);
		appendable.append(SPACE);
		appendable.append(inputClass.getSimpleName());
		appendGenericTypeVariables(appendable, inputClass);
		appendable.append(SPACE);
		appendable.append(INHERITANCE_OPERATOR);
		appendable.append(SPACE);
		appendable.append(genericSuperclass.getSimpleName());

		if (ElementState.class.isAssignableFrom(genericSuperclass))
			;
		{
			libraryNamespaces.put(genericSuperclass.getPackage().getName(), genericSuperclass
					.getPackage().getName());
			allNamespaces.put(genericSuperclass.getPackage().getName(), genericSuperclass.getPackage()
					.getName());
		}

		// add interface implementations
		Class<?>[] interfaces = inputClass.getInterfaces();

		for (int i = 0; i < interfaces.length; i++)
		{
			if (interfaces[i].isAssignableFrom(Mappable.class))
			{
				appendable.append(',');
				appendable.append(SPACE);
				appendable.append(interfaces[i].getSimpleName());
				implementMappableInterface = true;

				libraryNamespaces.put(Mappable.class.getPackage().getName(), Mappable.class.getPackage()
						.getName());
			}
		}

		appendable.append(SINGLE_LINE_BREAK);
		appendable.append(TAB);
		appendable.append(OPENING_CURLY_BRACE);
		appendable.append(SINGLE_LINE_BREAK);
	}

	private void appendGenericTypeVariables(Appendable appendable,
			Class<? extends ElementState> inputClass) throws IOException
	{
		TypeVariable<?>[] typeVariables = inputClass.getTypeParameters();
		if (typeVariables != null && typeVariables.length > 0)
		{
			appendable.append('<');
			int i = 0;
			for (TypeVariable<?> typeVariable : typeVariables)
			{
				if (i == 0)
					appendable.append(typeVariable.getName());
				else
					appendable.append(", " + typeVariable.getName());
				i++;
			}

			appendable.append('>');
		}

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

	public void setAdditionalImportNamespaces(ArrayList<String> additionalImportLines)
	{
		if (additionalImportLines == null)
			return;

		for (String newImport : additionalImportLines)
		{
			libraryNamespaces.put(newImport, newImport);
			allNamespaces.put(newImport, newImport);
		}

		this.additionalImportLines = additionalImportLines;
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

		c
				.translateToCSharp(
						new File("/csharp_output"),
						TranslationScope.get("RSSTranslations", Schmannel.class, BItem.class, SchmItem.class,
								RssState.class, Item.class, Channel.class));
		// c.translateToCSharp(System.out, Channel.class);
	}

}
