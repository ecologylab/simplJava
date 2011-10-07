package ecologylab.translators.net;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import ecologylab.generic.Debug;
import ecologylab.generic.HashMapArrayList;
import ecologylab.semantics.html.utils.StringBuilderUtils;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.FieldTypes;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.XMLTools;
import ecologylab.serialization.annotations.Hint;
import ecologylab.serialization.annotations.simpl_classes;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_hints;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_map;
import ecologylab.serialization.annotations.simpl_nowrap;
import ecologylab.serialization.annotations.simpl_other_tags;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_scope;
import ecologylab.serialization.annotations.simpl_tag;
import ecologylab.serialization.types.element.IMappable;
import ecologylab.translators.java.JavaTranslationUtilities;
import ecologylab.translators.parser.JavaDocParser;

/**
 * 
 * This class provides the functionality for the translation of a Translation Scope into C# files
 * 
 * @author Nabeel Shahzad
 * @version 1.0
 * 
 */
public class DotNetTranslator implements DotNetTranslationConstants
{

	private HashMap<String, String>			libraryNamespaces							= new HashMap<String, String>();

	private HashMap<String, String>			allNamespaces									= new HashMap<String, String>();

	private String											currentNamespace;

	private boolean											implementMappableInterface		= false;

	private ArrayList<String>						additionalImportLines;

	private ArrayList<ClassDescriptor>	excludeClassesFromTranslation	= new ArrayList<ClassDescriptor>();

	/**
	 * Constructor
	 */
	public DotNetTranslator()
	{

	}

	/**
	 * The main entry function. Goes through a sequence of steps to convert the Translation Scope into
	 * a C# file
	 * 
	 * @param appendable
	 * @param classes
	 * @throws IOException
	 * @throws DotNetTranslationException
	 */
	private void translateToCSharp(Appendable appendable, ClassDescriptor... classes)
			throws IOException, DotNetTranslationException
	{
		int length = classes.length;
		for (int i = 0; i < length; i++)
		{
			translateToCSharp(classes[i], appendable);
		}
	}

	/**
	 * @param inputClass
	 * @param appendable
	 * @throws IOException
	 * @throws DotNetTranslationException
	 */
	private void translateToCSharp(ClassDescriptor inputClass, Appendable appendable)
			throws IOException, DotNetTranslationException
	{
		ClassDescriptor classDescriptor = inputClass;

		HashMapArrayList<String, ? extends FieldDescriptor> fieldDescriptors = classDescriptor
				.getDeclaredFieldDescriptorsByFieldName();

		StringBuilder classFile = new StringBuilder();
		StringBuilder header = new StringBuilder();

		appendHeaderComments(inputClass.getDescribedClassSimpleName(), header);
		openNameSpace(inputClass, classFile);

		openClassFile(inputClass, classFile);

		classDescriptor.resolveUnresolvedScopeAnnotationFDs();

		if (fieldDescriptors.size() > 0)
		{
			classDescriptor.resolveUnresolvedScopeAnnotationFDs();

			for (FieldDescriptor fieldDescriptor : fieldDescriptors)
			{
				if (fieldDescriptor.belongsTo(classDescriptor))
					appendFieldAsCSharpAttribute(inputClass, fieldDescriptor, classFile);
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
	 * @param directoryLocation
	 * @param tScope
	 * @throws IOException
	 * @throws SIMPLTranslationException
	 * @throws DotNetTranslationException
	 */
	public void translateToCSharp(File directoryLocation, TranslationScope tScope)
			throws IOException, SIMPLTranslationException, DotNetTranslationException
	{
		translateToCSharp(directoryLocation, tScope, null);
	}

	/**
	 * @param directoryLocation
	 * @param tScope
	 * @param workSpaceLocation
	 * @throws IOException
	 * @throws SIMPLTranslationException
	 * @throws DotNetTranslationException
	 */
	public void translateToCSharp(File directoryLocation, final TranslationScope tScope,
			File workSpaceLocation) throws IOException, SIMPLTranslationException,
			DotNetTranslationException
	{
		System.out.println("Parsing source files to extract comments");

		TranslationScope anotherScope = TranslationScope.augmentTranslationScope(tScope);

		if (workSpaceLocation != null)
			JavaDocParser.parseSourceFileIfExists(anotherScope, workSpaceLocation);

		System.out.println("generating classes...");

		Collection<ClassDescriptor<? extends FieldDescriptor>> classes = anotherScope
				.entriesByClassName().values();

		int length = classes.size();
		for (ClassDescriptor classDesc : classes)
		{
			if (excludeClassesFromTranslation.contains(classDesc))
			{
				System.out.println("Excluding " + classDesc + "from translation as requested");
				continue;
			}
			System.out.println("Translating " + classDesc);
			translateToCSharp(classDesc, directoryLocation);
		}
		File tScopeDirectory = createGetTranslationScopeFolder(directoryLocation);
		generateTranslationScopeGetterClass(tScopeDirectory, tScope);
		System.out.println("DONE!");
	}

	/**
	 * @param inputClass
	 * @param directoryLocation
	 * @throws IOException
	 * @throws DotNetTranslationException
	 */
	public void translateToCSharp(ClassDescriptor inputClass, File directoryLocation)
			throws IOException, DotNetTranslationException
	{
		File outputFile = createCSharpFileWithDirectoryStructure(inputClass, directoryLocation);
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));

		translateToCSharp(inputClass, bufferedWriter);
		bufferedWriter.close();
	}

	/**
	 * @param directoryLocation
	 * @param tScope
	 * @throws IOException
	 */
	private void generateTranslationScopeGetterClass(File directoryLocation, TranslationScope tScope)
			throws IOException
	{
		String tScopeCamelCasedName = XMLTools.javaNameFromElementName(tScope.getName(), true);
		File sourceFile = new File(directoryLocation + FILE_PATH_SEPARATOR + tScopeCamelCasedName
				+ FILE_EXTENSION);
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(sourceFile));

		importDefaultNamespaces(bufferedWriter);
		importAllNamespaces(bufferedWriter);
		namespaceComments(bufferedWriter);
		bufferedWriter.append(SINGLE_LINE_BREAK);
		openNameSpace(tScope.getPackageName(), bufferedWriter);
		openTranslationScopeClassFile(tScopeCamelCasedName, bufferedWriter);
		appendDefaultConstructor(tScopeCamelCasedName, bufferedWriter);
		appendTranslationScopeGetterFunction(FGET, bufferedWriter, tScope);
		closeClassFile(bufferedWriter);
		closeNameSpace(bufferedWriter);
		bufferedWriter.close();
	}

	/**
	 * Appends header comments
	 * 
	 * @param appendable
	 * @throws IOException
	 */
	private void namespaceComments(Appendable appendable) throws IOException
	{
		appendable.append("//developer should modify the namespace");
		appendable.append(SINGLE_LINE_BREAK);
		appendable.append("//by default it falls into ecologylab.serialization");
	}

	/**
	 * @param functionName
	 * @param appendable
	 * @param tScope
	 * @throws IOException
	 */
	private void appendTranslationScopeGetterFunction(String functionName, Appendable appendable,
			TranslationScope tScope) throws IOException
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

		Collection<ClassDescriptor<? extends FieldDescriptor>> allClasses = tScope.entriesByClassName()
				.values();
		for (ClassDescriptor<? extends FieldDescriptor> myClass : allClasses)
		{
			appendable.append(COMMA);
			appendable.append(SINGLE_LINE_BREAK);
			appendable.append(DOUBLE_TAB);
			appendable.append(DOUBLE_TAB);
			appendable.append(TYPE_OF);
			appendable.append(OPENING_BRACE);
			appendable.append(myClass.getClassSimpleName());
			appendable.append(CLOSING_BRACE);
		}

		appendable.append(CLOSING_BRACE);
		appendable.append(END_LINE);
		appendable.append(SINGLE_LINE_BREAK);
		appendable.append(DOUBLE_TAB);
		appendable.append(CLOSING_CURLY_BRACE);
		appendable.append(SINGLE_LINE_BREAK);
	}

	/**
	 * @param className
	 * @param appendable
	 * @throws IOException
	 */
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

	/**
	 * @param appendable
	 * @throws IOException
	 */
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

	/**
	 * @param directoryLocation
	 * @return
	 */
	private File createGetTranslationScopeFolder(File directoryLocation)
	{
		String tScopeDirectoryPath = directoryLocation.toString() + FILE_PATH_SEPARATOR;

		tScopeDirectoryPath += TRANSATIONSCOPE_FOLDER + FILE_PATH_SEPARATOR;
		File tScopeDirectory = new File(tScopeDirectoryPath);
		tScopeDirectory.mkdir();
		return tScopeDirectory;
	}

	/**
	 * @param className
	 * @param appendable
	 * @throws IOException
	 */
	private void appendHeaderComments(String className, Appendable appendable) throws IOException
	{
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
		DateFormat yearFormat = new SimpleDateFormat("yyyy");

		Date date = new Date();

		appendable.append("//\n// " + className
				+ ".cs\n// s.im.pl serialization\n//\n// Generated by DotNetTranslator on "
				+ dateFormat.format(date) + ".\n// Copyright " + yearFormat.format(date)
				+ " Interface Ecology Lab. \n//\n\n");
	}

	/**
	 * Writes and opens the namespace
	 * 
	 * @param inputClass
	 * @param appendable
	 * @throws IOException
	 */
	private void openNameSpace(ClassDescriptor inputClass, Appendable appendable) throws IOException
	{
		openNameSpace(inputClass.getDescribedClassPackageName(), appendable);
	}

	/**
	 * @param classNameSpace
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
	 * opens the c# class
	 * 
	 * @param inputClass
	 * @param appendable
	 * @throws IOException
	 */
	private void openClassFile(ClassDescriptor inputClass, Appendable appendable) throws IOException
	{
		appendClassComments(inputClass, appendable);

		Annotation[] annotations = inputClass.getDescribedClass().getAnnotations();

		ClassDescriptor genericSuperClassDescriptor = inputClass.getSuperClass();
		if (genericSuperClassDescriptor == null)
			genericSuperClassDescriptor = ClassDescriptor.getClassDescriptor(ElementState.class);

		appendClassAnnotations(appendable, inputClass, "");

		appendable.append(TAB);
		appendable.append(PUBLIC);
		appendable.append(SPACE);
		appendable.append(CLASS);
		appendable.append(SPACE);
		appendable.append(inputClass.getDescribedClassSimpleName());
		appendGenericTypeVariables(appendable, inputClass);
		appendable.append(SPACE);
		appendable.append(INHERITANCE_OPERATOR);
		appendable.append(SPACE);
		appendable.append(genericSuperClassDescriptor.getDescribedClassSimpleName());

		if (ElementState.class.isAssignableFrom(genericSuperClassDescriptor.getClass()))
			;
		{
			libraryNamespaces.put(genericSuperClassDescriptor.getPackageName(),
					genericSuperClassDescriptor.getPackageName());
			allNamespaces.put(genericSuperClassDescriptor.getPackageName(),
					genericSuperClassDescriptor.getPackageName());
		}

		ArrayList<String> interfaces = inputClass.getInterfaceList();

		if (interfaces != null)
		{
			for (int i = 0; i < interfaces.size(); i++)
			{
				appendable.append(',');
				appendable.append(SPACE);
				appendable.append(interfaces.get(i));
				implementMappableInterface = true;

				libraryNamespaces.put(IMappable.class.getPackage().getName(), IMappable.class.getPackage()
						.getName());

			}
		}

		appendable.append(SINGLE_LINE_BREAK);
		appendable.append(TAB);
		appendable.append(OPENING_CURLY_BRACE);
		appendable.append(SINGLE_LINE_BREAK);

	}

	/**
	 * @param inputClass
	 * @param appendable
	 * @throws IOException
	 */
	private void appendClassComments(ClassDescriptor inputClass, Appendable appendable)
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

	/**
	 * @param appendable
	 * @param javaDocCommentArray
	 * @param doubleTabs
	 * @throws IOException
	 */
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

	/**
	 * @param appendable
	 * @param inputClass
	 * @throws IOException
	 */
	private void appendGenericTypeVariables(Appendable appendable, ClassDescriptor inputClass)
			throws IOException
	{
		ArrayList<String> typeVariables = inputClass.getGenericTypeVariables();
		if (typeVariables != null && typeVariables.size() > 0)
		{
			appendable.append('<');
			int i = 0;
			for (String typeVariable : typeVariables)
			{
				if (i == 0)
					appendable.append(typeVariable);
				else
					appendable.append(", ").append(typeVariable);
				i++;
			}
			appendable.append('>');
		}
	}

	/**
	 * @param context
	 *          TODO
	 * @param fieldDescriptor
	 * @param appendable
	 * @throws IOException
	 * @throws DotNetTranslationException
	 */
	private void appendFieldAsCSharpAttribute(ClassDescriptor context,
			FieldDescriptor fieldDescriptor, Appendable appendable) throws IOException,
			DotNetTranslationException
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
		appendFieldAnnotations(context, fieldDescriptor, appendable);

		appendable.append(DOUBLE_TAB);
		appendable.append(PRIVATE);
		appendable.append(SPACE);
		appendable.append(cSharpType);
		appendable.append(SPACE);
		appendable.append(fieldDescriptor.getName());
		appendable.append(END_LINE);
		appendable.append(DOUBLE_LINE_BREAK);

		appendComments(appendable, false, isKeyword);
	}

	/**
	 * @param fieldDescriptor
	 */
	private void registerNamespaces(FieldDescriptor fieldDescriptor)
	{
		HashMap<String, String> namespaces = fieldDescriptor.getNamespaces();

		if (namespaces != null && namespaces.size() > 0)
		{
			for (String key : namespaces.keySet())
			{
				libraryNamespaces.put(key, namespaces.get(key));
				allNamespaces.put(key, namespaces.get(key));
			}
		}
	}

	/**
	 * @param fieldAccessor
	 * @param appendable
	 * @return
	 * @throws IOException
	 */
	private boolean checkForKeywords(FieldDescriptor fieldAccessor, Appendable appendable)
			throws IOException
	{
		if (DotNetTranslationUtilities.isKeyword(fieldAccessor.getName()))
		{
			Debug.warning(fieldAccessor, " Field Name: [" + fieldAccessor.getName()
					+ "]. This is a keyword in C#. Cannot translate");
			return true;
		}

		return false;
	}

	/**
	 * @param appendable
	 * @param start
	 * @param isKeywrord
	 * @throws IOException
	 */
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

	private void appendClassAnnotations(Appendable appendable, ClassDescriptor classDesc,
			String tabSpacing) throws IOException
	{
		ClassDescriptor superClass = classDesc.getSuperClass();
		if (superClass != null && !superClass.getDescribedClassSimpleName().equals("ElementState"))
			appendAnnotation(appendable, "", simpl_inherit.class);

		String tagName = classDesc.getTagName();
		String autoTagName = XMLTools.getXmlTagName(classDesc.getDescribedClassSimpleName(), null);
		if (tagName != null && !tagName.equals("") && !tagName.equals(autoTagName))
		{
			appendAnnotation(appendable, SINGLE_LINE_BREAK, simpl_tag.class, true, tagName);
		}

		// TODO @simpl_other_tags
		ArrayList<String> otherTags = classDesc.otherTags();
		if (otherTags != null && otherTags.size() > 0)
		{
			appendAnnotation(appendable, SINGLE_LINE_BREAK, simpl_other_tags.class, true, otherTags);
		}

		appendable.append(SINGLE_LINE_BREAK);

		appendClassAnnotationsHook(appendable, classDesc, tabSpacing);
	}

	private void appendClassAnnotationsHook(Appendable appendable, ClassDescriptor classDesc,
			String tabSpacing)
	{

	}

	private void addDependency(String name)
	{
		libraryNamespaces.put(name, name);
		allNamespaces.put(name, name);
	}

	private void appendFieldAnnotations(ClassDescriptor contextCd, FieldDescriptor fieldDescriptor,
			Appendable appendable) throws IOException
	{
		int type = fieldDescriptor.getType();
		String collectionMapTagValue = fieldDescriptor.getCollectionOrMapTagName();

		if (type == FieldTypes.COMPOSITE_ELEMENT)
		{
			// @simpl_composite
			appendAnnotation(appendable, TAB, simpl_composite.class);
		}
		else if (type == FieldTypes.COLLECTION_ELEMENT || type == FieldTypes.COLLECTION_SCALAR)
		{
			addDependency(List.class.getName());
			if (!fieldDescriptor.isWrapped())
			{
				// @simpl_nowrap
				appendAnnotation(appendable, TAB, simpl_nowrap.class);
			}
			// @simpl_collection
			if (fieldDescriptor.isPolymorphic())
				appendAnnotation(appendable, TAB, simpl_collection.class);
			else
				appendAnnotation(appendable, TAB, simpl_collection.class, true, collectionMapTagValue);
		}
		else if (type == FieldTypes.MAP_ELEMENT)
		{
			// @simpl_map
			appendAnnotation(appendable, TAB, simpl_map.class, true, collectionMapTagValue);
		}
		else
		{
			// @simpl_scalar
			appendAnnotation(appendable, TAB, simpl_scalar.class);
		}

		// @simpl_tag
		String tagName = fieldDescriptor.getTagName();
		String autoTagName = XMLTools.getXmlTagName(fieldDescriptor.getName(), null);
		if (tagName != null && !tagName.equals("") && !tagName.equals(autoTagName))
		{
			appendAnnotation(appendable, TAB, simpl_tag.class, true, tagName);
		}

		// @simpl_other_tags
		ArrayList<String> otherTags = fieldDescriptor.otherTags();
		if (otherTags != null && otherTags.size() > 0)
		{
			appendAnnotation(appendable, TAB, simpl_other_tags.class, true, otherTags);
		}

		if (!((type == FieldTypes.COMPOSITE_ELEMENT) || (type == FieldTypes.COLLECTION_ELEMENT) || (type == FieldTypes.COLLECTION_SCALAR)))
		{
			Hint hint = fieldDescriptor.getXmlHint();
			if (hint != null)
			{
				// @simpl_hints
				appendAnnotation(appendable, TAB, simpl_hints.class, false, Hint.class.getSimpleName()
						+ "." + hint.name());
				addDependency(Hint.class.getName());
			}
		}

		// @simpl_classes
		Collection<ClassDescriptor> polyClassDescriptors = fieldDescriptor
				.getPolymorphicClassDescriptors();
		if (polyClassDescriptors != null)
		{
			HashSet<ClassDescriptor> classDescriptors = new HashSet<ClassDescriptor>(polyClassDescriptors);
			appendAnnotation(appendable, TAB, simpl_classes.class, false,
					JavaTranslationUtilities.getJavaClassesAnnotation(classDescriptors));
		}

		// @simpl_scope
		String polyScope = fieldDescriptor.getUnresolvedScopeAnnotation();
		if (polyScope != null && polyScope.length() > 0)
		{
			appendAnnotation(appendable, TAB, simpl_scope.class, true, polyScope);
		}

		appendFieldAnnotationsHook(appendable, contextCd, fieldDescriptor, TAB);
	}



	private void appendFieldAnnotationsHook(Appendable appendable, ClassDescriptor context,
			FieldDescriptor fieldDescriptor, String tabSpacing)
	{

	}

	private void appendAnnotation(Appendable appendable, String tab, Class annotationClass,
			boolean quoted, ArrayList<String> params) throws IOException
	{
		StringBuilder sb = StringBuilderUtils.acquire();
		if (params != null && params.size() > 0)
			for (int i = 0; i < params.size(); ++i)
				sb.append(i == 0 ? "(" : ", ").append(quoted ? "\"" : "").append(params.get(i)).append(quoted ? "\"" : "").append(i == params.size() - 1 ? ")" : "");
		appendAnnotation(appendable, tab, annotationClass, false, sb.toString());
		StringBuilderUtils.release(sb);
	}

	public void appendAnnotation(Appendable appendable, String indent, Class annotationClass)
			throws IOException
	{
		appendAnnotation(appendable, indent, annotationClass, false, "");
	}

	public void appendAnnotation(Appendable appendable, String indent, Class annotationClass,
			boolean quoted, String params) throws IOException
	{
		appendable.append(indent);
		appendable.append(OPENING_SQUARE_BRACE);
		appendable.append(annotationClass.getSimpleName());
		if (params != null && params.length() > 0)
			appendable.append("(").append(quoted ? "\"" : "").append(params).append(quoted ? "\"" : "")
					.append(")");
		appendable.append(CLOSING_SQUARE_BRACE);
	}

	/**
	 * @param inputClass
	 * @param appendable
	 * @throws IOException
	 */
	private void appendDefaultConstructor(ClassDescriptor inputClass, Appendable appendable)
			throws IOException
	{
		appendDefaultConstructor(inputClass.getDescribedClassSimpleName(), appendable);
	}

	/**
	 * @param className
	 * @param appendable
	 * @throws IOException
	 */
	private void appendDefaultConstructor(String className, Appendable appendable) throws IOException
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
		appendable.append(OPENING_CURLY_BRACE);
		appendable.append(RETURN);
		appendable.append(SPACE);
		appendable.append(fieldDescriptor.getName());
		appendable.append(END_LINE);
		appendable.append(CLOSING_CURLY_BRACE);
		appendable.append(SINGLE_LINE_BREAK);
		appendable.append(DOUBLE_TAB);
		appendable.append(TAB);
		appendable.append(SET);
		appendable.append(OPENING_CURLY_BRACE);
		appendable.append(fieldDescriptor.getName());
		appendable.append(SPACE);
		appendable.append(ASSIGN);
		appendable.append(SPACE);
		appendable.append(VALUE);
		appendable.append(END_LINE);
		appendable.append(CLOSING_CURLY_BRACE);
		appendable.append(SINGLE_LINE_BREAK);

		appendable.append(DOUBLE_TAB);
		appendable.append(CLOSING_CURLY_BRACE);
		appendable.append(SINGLE_LINE_BREAK);

		appendComments(appendable, false, isKeyword);

	}

	/**
	 * @param appendable
	 * @throws IOException
	 */
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
	 * @param someClass
	 */
	public void excludeClassFromTranslation(ClassDescriptor someClass)
	{
		excludeClassesFromTranslation.add(someClass);
	}

	/**
	 * @param appendable
	 * @throws IOException
	 */
	private void closeNameSpace(Appendable appendable) throws IOException
	{
		appendable.append(CLOSING_CURLY_BRACE);
		appendable.append(SINGLE_LINE_BREAK);
	}

	/**
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

	/**
	 * @param appendable
	 * @throws IOException
	 */
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
	 * @param additionalImportLines
	 */
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
	 * @param inputClass
	 * @param directoryLocation
	 * @return
	 * @throws IOException
	 */
	private File createCSharpFileWithDirectoryStructure(ClassDescriptor inputClass,
			File directoryLocation) throws IOException
	{
		String packageName = inputClass.getDescribedClassPackageName();
		String className = inputClass.getDescribedClassSimpleName();
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

}
 