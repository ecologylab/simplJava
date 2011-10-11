package ecologylab.translators.net;

import static ecologylab.translators.net.DotNetTranslationConstants.SPACE;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ecologylab.generic.Debug;
import ecologylab.generic.HashMapArrayList;
import ecologylab.semantics.html.utils.StringBuilderUtils;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.MetaInformation;
import ecologylab.serialization.MetaInformation.Argument;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.types.CollectionType;
import ecologylab.serialization.types.ScalarType;
import ecologylab.serialization.types.element.IMappable;
import ecologylab.translators.AbstractCodeTranslator;
import ecologylab.translators.CodeTranslatorConfig;

/**
 * 
 * This class provides the functionality for the translation of a Translation Scope into C# files
 * 
 * @author Nabeel Shahzad
 * @version 1.0
 * 
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class DotNetTranslator extends AbstractCodeTranslator implements DotNetTranslationConstants
{
	
	/**
	 * Dependences for each class, globally.
	 */
	private Set<String>									globalDependencies						= new HashSet<String>();

	/**
	 * Dependencies of current class.
	 */
	private Set<String>									currentClassDependencies			= new HashSet<String>();

	/**
	 * Dependencies of the encompassing TranslationScope class.
	 */
	private Set<String>									libraryTScopeDependencies						= new HashSet<String>();

	private String											currentNamespace;

	private boolean											implementMappableInterface		= false;

	private ArrayList<ClassDescriptor>	excludeClassesFromTranslation	= new ArrayList<ClassDescriptor>();
	
	public DotNetTranslator()
	{
		addGlobalDependency("System");
		addGlobalDependency("System.Collections");
		addGlobalDependency("System.Collections.Generic");
		addGlobalDependency("Simpl.Serialization");
		addGlobalDependency("Simpl.Serialization.Attributes");
		addGlobalDependency("Simpl.Fundamental.Generic");
		addGlobalDependency("ecologylab.collections");
	}

	@Override
	public void translate(File directoryLocation, TranslationScope tScope)
			throws IOException, SIMPLTranslationException, DotNetTranslationException
	{
		debug("Generating C# classes ...");
		Collection<ClassDescriptor<? extends FieldDescriptor>> classes = tScope.entriesByClassName().values();
		for (ClassDescriptor classDesc : classes)
		{
			if (excludeClassesFromTranslation.contains(classDesc))
			{
				debug("Excluding " + classDesc + "from translation as requested.");
				continue;
			}
			debug("Translating " + classDesc + "...");
			translate(classDesc, directoryLocation);
		}
		generateLibraryTScopeClass(directoryLocation, tScope);
		debug("DONE!");
	}

	@Override
	public void translate(ClassDescriptor inputClass, File directoryLocation)
			throws IOException, DotNetTranslationException
	{
		File outputFile = createCSharpFileWithDirectoryStructure(directoryLocation, inputClass.getDescribedClassPackageName(), inputClass.getDescribedClassSimpleName());
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
		translate(inputClass, implementMappableInterface, bufferedWriter);
		bufferedWriter.close();
	}

	/**
	 * The actual method converting a class descriptor into C# codes.
	 * 
	 * @param inputClass
	 * @param appendable
	 * @throws IOException
	 * @throws DotNetTranslationException
	 */
	private void translate(ClassDescriptor inputClass, boolean implementMappable, Appendable appendable)
			throws IOException, DotNetTranslationException
	{
		HashMapArrayList<String, ? extends FieldDescriptor> fieldDescriptors =
				inputClass.getDeclaredFieldDescriptorsByFieldName();
		inputClass.resolveUnresolvedScopeAnnotationFDs();
		currentClassDependencies = new HashSet<String>(globalDependencies);

		StringBuilder classBody = StringBuilderUtils.acquire();
		
		// file header
		StringBuilder header = StringBuilderUtils.acquire();
		appendHeaderComments(inputClass.getDescribedClassSimpleName(), SINGLE_LINE_COMMENT, FILE_EXTENSION, header);
		
		// unit scope
		openUnitScope(inputClass, classBody);

		// class
		// class: opening
		openClassBody(inputClass, classBody);
		// class: fields
		for (FieldDescriptor fieldDescriptor : fieldDescriptors)
		{
			if (fieldDescriptor.belongsTo(inputClass))
				appendField(inputClass, fieldDescriptor, classBody);
		}
		// class: constructor(s)
		appendConstructor(inputClass, classBody);
		// class: getters and setters
		for (FieldDescriptor fieldDescriptor : fieldDescriptors)
		{
			if (fieldDescriptor.belongsTo(inputClass))
				appendGettersAndSetters(inputClass, fieldDescriptor, classBody);
		}
		// class: mappable
		if (implementMappable)
			implementMappable(classBody);
		// class: closing
		closeClassBody(classBody);
		
		// unit scope: closing
		closeUnitScope(classBody);

		// dependencies
		currentClassDependencies.addAll(deriveDependencies(inputClass));
		appendDependencies(currentClassDependencies, header);
		currentClassDependencies.clear();
		
		// write to the file stream
		appendable.append(header);
		appendable.append(classBody);
		
		StringBuilderUtils.release(header);
		StringBuilderUtils.release(classBody);
	}

	/**
	 * Writes and opens the namespace
	 * 
	 * @param inputClass
	 * @param appendable
	 * @throws IOException
	 */
	@Override
	protected void openUnitScope(ClassDescriptor inputClass, Appendable appendable) throws IOException
	{
		currentNamespace = inputClass.getCSharpNamespace();
		addTScopeDependency(currentNamespace);
		
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
	@Override
	protected void openClassBody(ClassDescriptor inputClass, Appendable appendable) throws IOException
	{
		appendClassComments(inputClass, appendable);
		
		appendClassMetaInformation(inputClass, appendable);
		
		appendable.append(TAB);
		appendable.append(PUBLIC);
		appendable.append(SPACE);
		appendable.append(CLASS);
		appendable.append(SPACE);
		appendable.append(inputClass.getDescribedClassSimpleName());
		appendGenericTypeVariables(appendable, inputClass);
		
		ClassDescriptor superCD = inputClass.getSuperClass();
		if (superCD != null)
		{
			appendable.append(SPACE);
			appendable.append(INHERITANCE_OPERATOR);
			appendable.append(SPACE);
			appendable.append(superCD.getDescribedClassSimpleName());
			currentClassDependencies.add(superCD.getCSharpNamespace());
		}
	
		// TODO currently interfaces can only be done through reflection
		ArrayList<String> interfaces = inputClass.getInterfaceList();
		if (interfaces != null)
		{
			for (int i = 0; i < interfaces.size(); i++)
			{
				appendable.append(',');
				appendable.append(SPACE);
				appendable.append(interfaces.get(i));
				implementMappableInterface = true;
				currentClassDependencies.add(IMappable.class.getPackage().getName());
			}
		}
	
		appendable.append(SINGLE_LINE_BREAK);
		appendable.append(TAB);
		appendable.append(OPENING_CURLY_BRACE);
		appendable.append(SINGLE_LINE_BREAK);
	}

	/**
	 * Append class comments.
	 * 
	 * @param inputClass
	 * @param appendable
	 * @throws IOException
	 */
	@Override
	protected void appendClassComments(ClassDescriptor inputClass, Appendable appendable)
			throws IOException
	{
		String comment = inputClass.getComment();
		if (comment != null && comment.length() > 0)
			appendStructuredComments(appendable, TAB, inputClass.getComment());
	}

	@Override
	protected void appendClassMetaInformation(ClassDescriptor inputClass, Appendable appendable)
			throws IOException
	{
		List<MetaInformation> metaInfo = inputClass.getMetaInformation();
		if (metaInfo != null)
			for (MetaInformation piece : metaInfo)
			{
				appendMetaInformation(piece, appendable, TAB);
			}
		
		appendClassMetaInformationHook(inputClass, appendable);
	}

	@Override
	protected void appendClassMetaInformationHook(ClassDescriptor inputClass, Appendable appendable)
	{
		// empty implementation
	}

	/**
	 * @param appendable
	 * @param inputClass
	 * @throws IOException
	 */
	@Override
	protected void appendGenericTypeVariables(Appendable appendable, ClassDescriptor inputClass)
			throws IOException
	{
		// TODO currently generic parameters can only be done through reflection!
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
	 * Append a field to the translated class source.
	 * 
	 * @param context
	 * @param fieldDescriptor
	 * @param appendable
	 * @throws IOException
	 * @throws DotNetTranslationException
	 */
	@Override
	protected void appendField(ClassDescriptor context, FieldDescriptor fieldDescriptor, Appendable appendable)
			throws IOException, DotNetTranslationException
	{
		String cSharpType = fieldDescriptor.getCSharpType();
		if (cSharpType == null)
		{
			System.out.println("ERROR, no valid CSharpType found for : " + fieldDescriptor);
			return;
		}
	
		boolean isKeyword = checkForKeywords(fieldDescriptor);
		if (isKeyword)
				appendable.append(OPEN_BLOCK_COMMENTS).append(SINGLE_LINE_BREAK);
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
		if (isKeyword)
				appendable.append(CLOSE_BLOCK_COMMENTS).append(SINGLE_LINE_BREAK);
	}

	private Set<String> deriveDependencies(ClassDescriptor inputClass)
	{
		Set<String> dependencies = new HashSet<String>();
		
		Set<ScalarType> scalars = inputClass.deriveScalarDependencies();
		for (ScalarType scalar : scalars)
			dependencies.add(scalar.getCSharpNamespace());
		
		Set<ClassDescriptor> composites = inputClass.deriveCompositeDependencies();
		for (ClassDescriptor composite : composites)
			dependencies.add(composite.getCSharpNamespace());
		
		Set<CollectionType> collections = inputClass.deriveCollectionDependencies();
		for (CollectionType collection : collections)
			dependencies.add(collection.getCSharpNamespace());
		
		return dependencies;
	}
	
	/**
	 * @param fieldDescriptor
	 * @param appendable
	 * @throws IOException
	 */
	@Override
	protected void appendFieldComments(FieldDescriptor fieldDescriptor, Appendable appendable)
			throws IOException
	{
		String comment = fieldDescriptor.getComment();
		if (comment != null && comment.length() > 0)
			appendStructuredComments(appendable, DOUBLE_TAB, comment);
	}

	@Override
	protected void appendFieldAnnotations(ClassDescriptor contextCd, FieldDescriptor fieldDescriptor,
			Appendable appendable) throws IOException
	{
		List<MetaInformation> metaInfo = fieldDescriptor.getMetaInformation();
		if (metaInfo != null)
			for (MetaInformation piece : metaInfo)
			{
				appendMetaInformation(piece, appendable, DOUBLE_TAB);
				// TODO process dependencies
			}
	
		appendFieldAnnotationsHook(contextCd, fieldDescriptor, appendable);
	}

	@Override
	protected void appendFieldAnnotationsHook(ClassDescriptor contextCd, FieldDescriptor fieldDesc,
			Appendable appendable) throws IOException
	{
		// for derived classes to use.
	}

	/**
	 * @param inputClass
	 * @param appendable
	 * @throws IOException
	 */
	@Override
	protected void appendConstructor(ClassDescriptor inputClass, Appendable appendable)
			throws IOException
	{
		appendDefaultConstructor(inputClass.getDescribedClassSimpleName(), appendable);
		
		appendConstructorHook(inputClass, appendable);
	}

	@Override
	protected void appendConstructorHook(ClassDescriptor inputClass, Appendable appendable) throws IOException
	{
		// for derived classes to use.
	}

	/**
	 * @param className
	 * @param appendable
	 * @throws IOException
	 */
	@Override
	protected void appendDefaultConstructor(String className, Appendable appendable)
			throws IOException
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
	 * @param context
	 * @param fieldDescriptor
	 * @param appendable
	 * @throws IOException
	 */
	@Override
	protected void appendGettersAndSetters(ClassDescriptor context, FieldDescriptor fieldDescriptor, Appendable appendable)
			throws IOException
	{
		String cSharpType = fieldDescriptor.getCSharpType();
		if (cSharpType == null)
		{
			System.out.println("ERROR, no valid CSharpType found for : " + fieldDescriptor);
			return;
		}
	
		appendable.append(SINGLE_LINE_BREAK);
	
		boolean isKeyword = checkForKeywords(fieldDescriptor);
		if (isKeyword)
				appendable.append(OPEN_BLOCK_COMMENTS).append(SINGLE_LINE_BREAK);
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
		if (isKeyword)
				appendable.append(CLOSE_BLOCK_COMMENTS).append(SINGLE_LINE_BREAK);
	
		appendGettersAndSettersHook(context, fieldDescriptor, appendable);
	}

	@Override
	protected void appendGettersAndSettersHook(ClassDescriptor context, FieldDescriptor fieldDescriptor, Appendable appendable)
	{
		// for derived classes to use.
	}

	/**
	 * @param appendable
	 * @throws IOException
	 */
	private void implementMappable(Appendable appendable) throws IOException
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
	@Override
	protected void closeClassBody(Appendable appendable) throws IOException
	{
		appendable.append(TAB);
		appendable.append(CLOSING_CURLY_BRACE);
		appendable.append(SINGLE_LINE_BREAK);
	}

	/**
	 * @param appendable
	 * @throws IOException
	 */
	@Override
	protected void closeUnitScope(Appendable appendable) throws IOException
	{
		appendable.append(CLOSING_CURLY_BRACE);
		appendable.append(SINGLE_LINE_BREAK);
	}

	/**
	 * @param dependencies 
	 * @param appendable
	 * @throws IOException
	 */
	@Override
	protected void appendDependencies(Collection<String> dependencies, Appendable appendable)
			throws IOException
	{
		if (dependencies != null && dependencies.size() > 0)
		{
			List<String> sortedDependencies = new ArrayList<String>(dependencies);
			Collections.sort(sortedDependencies);
			for (String namespace : sortedDependencies)
			{
				// do not append if it belongs to current namespace
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
	 * @param fieldDescriptor
	 * @return  true if the name is a keyword, otherwise false.
	 */
	private boolean checkForKeywords(FieldDescriptor fieldDescriptor)
	{
		if (DotNetTranslationUtilities.isKeyword(fieldDescriptor.getName()))
		{
			Debug.warning(fieldDescriptor, "Field [" + fieldDescriptor.getName() + "]: This is a keyword in C#. Cannot translate.");
			return true;
		}
		return false;
	}

	/**
	 * Append comments (in C# style).
	 * 
	 * @param appendable
	 * @param spacing
	 * @param comments The array of comments, each element in a line.
	 * @throws IOException
	 */
	@Override
	protected void appendStructuredComments(Appendable appendable, String spacing, String... comments)
			throws IOException
	{
		appendable.append(spacing).append(OPEN_COMMENTS).append(SINGLE_LINE_BREAK);
		if (comments != null && comments.length > 0)
			for (String comment : comments)
				appendable.append(spacing).append(XML_COMMENTS).append(comment).append(SINGLE_LINE_BREAK);
		else
			appendable.append(spacing).append(XML_COMMENTS).append("(missing comments)").append(SINGLE_LINE_BREAK);
		appendable.append(spacing).append(CLOSE_COMMENTS).append(SINGLE_LINE_BREAK);
	}

	@Override
	protected void appendMetaInformation(MetaInformation metaInfo, Appendable appendable, String spacing)
			throws IOException
	{
		appendable.append(spacing);
		appendable.append(OPENING_SQUARE_BRACE);
		appendable.append(DotNetTranslationUtilities.translateAnnotationName(metaInfo.simpleTypeName));
		if (metaInfo.args != null && metaInfo.args.size() > 0)
		{
			appendable.append("(");
			if (metaInfo.argsInArray)
			{
				String argType = metaInfo.args.get(0).simpleTypeName;
				appendable.append("new ").append(argType).append("[] {");
				for (int i = 0; i < metaInfo.args.size(); ++i)
					appendable.append(i==0?"":", ").append(DotNetTranslationUtilities.translateMetaInfoArgValue(metaInfo.args.get(i).value));
				appendable.append("}");
			}
			else
			{
				for (int i = 0; i < metaInfo.args.size(); ++i)
				{
					Argument a = metaInfo.args.get(i);
					appendable.append(i==0?"":", ").append(metaInfo.args.size()>1?a.name+" = ":"").append(DotNetTranslationUtilities.translateMetaInfoArgValue(a.value));
				}
			}
			appendable.append(")");
		}
		appendable.append(CLOSING_SQUARE_BRACE);
	}
	
	@Override
	public void addGlobalDependency(String name)
	{
		globalDependencies.add(name);
	}

	@Override
	public void addCurrentClassDependency(String name)
	{
		currentClassDependencies.add(name);
	}
	
	@Override
	public void addTScopeDependency(String name)
	{
		libraryTScopeDependencies.add(name);
	}

	private File createCSharpFileWithDirectoryStructure(File directoryLocation, String packageName,
			String className) throws IOException
	{
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
	 * @param directoryLocation
	 * @param tScope
	 * @throws IOException
	 */
	@Override
	protected void generateLibraryTScopeClass(File directoryLocation, TranslationScope tScope)
			throws IOException
	{
		CodeTranslatorConfig config = CodeTranslatorConfig.getDefaultConfig(TargetLanguage.C_SHARP);
		String packageName = config.getGeneratedTranslationScopeClassPackageName();
		String tscopeClassName = config.getGeneratedTranslationScopeClassSimpleName();
		
		File sourceFile = createCSharpFileWithDirectoryStructure(directoryLocation, packageName, tscopeClassName);
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(sourceFile));

		// append global dependencies
		appendDependencies(globalDependencies, bufferedWriter);
		
		// append all generated classes to the translation scope
		if (libraryTScopeDependencies != null && libraryTScopeDependencies.size() > 0)
		{
			for (String namespace : libraryTScopeDependencies)
			{
				bufferedWriter.append(SINGLE_LINE_BREAK);
				bufferedWriter.append(USING);
				bufferedWriter.append(SPACE);
				bufferedWriter.append(namespace);
				bufferedWriter.append(END_LINE);
			}
		}
		bufferedWriter.append(DOUBLE_LINE_BREAK);
		
		// append notice information
		bufferedWriter.append("// Developer should proof-read this TranslationScope before using it for production.");
		bufferedWriter.append(SINGLE_LINE_BREAK);
		
		// header
		bufferedWriter.append(NAMESPACE);
		bufferedWriter.append(SPACE);
		bufferedWriter.append(packageName);
		bufferedWriter.append(SPACE);
		bufferedWriter.append(SINGLE_LINE_BREAK);
		bufferedWriter.append(OPENING_CURLY_BRACE);
		bufferedWriter.append(SINGLE_LINE_BREAK);
		
		// class body
		openLibraryTScopeClassBody(tscopeClassName, bufferedWriter);
		appendDefaultConstructor(tscopeClassName, bufferedWriter);
		appendLibraryTScopeGetter(FGET, bufferedWriter, tScope);
		closeClassBody(bufferedWriter);
		closeUnitScope(bufferedWriter);
		
		bufferedWriter.close();
	}

	/**
	 * @param className
	 * @param appendable
	 * @throws IOException
	 */
	@Override
	protected void openLibraryTScopeClassBody(String className, Appendable appendable)
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
	 * @param functionName
	 * @param appendable
	 * @param tScope
	 * @throws IOException
	 */
	@Override
	protected void appendLibraryTScopeGetter(String functionName, Appendable appendable,
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

		Collection<ClassDescriptor<? extends FieldDescriptor>> allClasses = tScope.entriesByClassName().values();
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
	 * @param someClass
	 */
	public void excludeClassFromTranslation(ClassDescriptor someClass)
	{
		excludeClassesFromTranslation.add(someClass);
	}
	
}
