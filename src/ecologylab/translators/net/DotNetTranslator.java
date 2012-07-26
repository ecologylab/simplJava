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
import ecologylab.serialization.GenericTypeVar;
import ecologylab.serialization.MetaInformation;
import ecologylab.serialization.MetaInformation.Argument;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.Hint;
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
	private Set<String>									currentClassDependencies;

	/**
	 * Dependencies of the encompassing TranslationScope class.
	 */
	private Set<String>									libraryTScopeDependencies			= new HashSet<String>();

	private String											currentNamespace;

	private boolean											implementMappableInterface		= false;

	private ArrayList<ClassDescriptor>	excludeClassesFromTranslation	= new ArrayList<ClassDescriptor>();
	
	protected CodeTranslatorConfig			config;
	
	public DotNetTranslator()
	{
		super("csharp");
		
		addGlobalDependency("System");
		addGlobalDependency("System.Collections");
		addGlobalDependency("System.Collections.Generic");
		addGlobalDependency("Simpl.Serialization");
		addGlobalDependency("Simpl.Serialization.Attributes");
		addGlobalDependency("Simpl.Fundamental.Generic");
		addGlobalDependency("ecologylab.collections");
	}

	@Override
	public void translate(File directoryLocation, SimplTypesScope tScope, CodeTranslatorConfig config)
			throws IOException, SIMPLTranslationException, DotNetTranslationException
	{
		debug("Generating C# classes ...");
		this.config = config;
		Collection<ClassDescriptor<? extends FieldDescriptor>> classes = tScope.entriesByClassName().values();
		for (ClassDescriptor classDesc : classes)
		{
			if (excludeClassesFromTranslation.contains(classDesc))
			{
				debug("Excluding " + classDesc + "from translation as requested.");
				continue;
			}
			translate(classDesc, directoryLocation, config);
		}
		generateLibraryTScopeClass(directoryLocation, tScope, config.getLibraryTScopeClassPackage(), config.getLibraryTScopeClassSimpleName());
		debug("DONE !");
	}

	@Override
	public void translate(ClassDescriptor inputClass, File directoryLocation, CodeTranslatorConfig config)
			throws IOException, DotNetTranslationException
	{
		debug("Generating C# class: " + inputClass.getDescribedClassName() + "...");
		this.config = config;
		File outputFile = createFileWithDirStructure(
				directoryLocation,
				inputClass.getDescribedClassPackageName().split(PACKAGE_NAME_SEPARATOR),
				inputClass.getDescribedClassSimpleName(),
				FILE_EXTENSION
				);
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
		translate(inputClass, implementMappableInterface, bufferedWriter);
		bufferedWriter.close();
		debug("done.");
	}

	/**
	 * The actual method converting a class descriptor into C# codes.
	 * 
	 * @param inputClass
	 * @param implementMappable
	 * @param appendable
	 * @throws IOException
	 * @throws DotNetTranslationException
	 */
	private void translate(ClassDescriptor inputClass, boolean implementMappable, Appendable appendable)
			throws IOException, DotNetTranslationException
	{
		HashMapArrayList<String, ? extends FieldDescriptor> fieldDescriptors =
				inputClass.getDeclaredFieldDescriptorsByFieldName();
		inputClass.resolvePolymorphicAnnotations();
		currentClassDependencies = new HashSet<String>(globalDependencies);

		StringBuilder classBody = StringBuilderUtils.acquire();
		
		// file header
		StringBuilder header = StringBuilderUtils.acquire();
		appendHeaderComments(inputClass.getDescribedClassSimpleName(), SINGLE_LINE_COMMENT, FILE_EXTENSION, header);
		
		// unit scope
		openUnitScope(inputClass.getCSharpNamespace(), classBody);

		// class
		// class: opening
		openClassBody(inputClass, classBody);
		// class: fields
		for (FieldDescriptor fieldDescriptor : fieldDescriptors)
		{
			if (fieldDescriptor.belongsTo(inputClass) || inputClass.isCloned() && fieldDescriptor.belongsTo(inputClass.getClonedFrom()))
				appendField(inputClass, fieldDescriptor, classBody);
		}
		// class: constructor(s)
		appendConstructor(inputClass, classBody, null);
		// class: getters and setters
		for (FieldDescriptor fieldDescriptor : fieldDescriptors)
		{
			if (fieldDescriptor.belongsTo(inputClass) || inputClass.isCloned() && fieldDescriptor.belongsTo(inputClass.getClonedFrom()))
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

	@Override
	protected void openUnitScope(String unitScopeName, Appendable appendable) throws IOException
	{
		currentNamespace = unitScopeName;
		addLibraryTScopeDependency(currentNamespace);
		
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
		appendClassGenericTypeVariables(appendable, inputClass);
		
		ClassDescriptor superCD = inputClass.getSuperClass();
		if (superCD != null)
		{
			appendable.append(SPACE);
			appendable.append(INHERITANCE_OPERATOR);
			appendable.append(SPACE);
			appendable.append(superCD.getDescribedClassSimpleName());
			appendSuperClassGenericTypeVariables(appendable, inputClass);
			addCurrentClassDependency(superCD.getCSharpNamespace());
		}
		superClassHook(inputClass, appendable);
	
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
				addCurrentClassDependency(IMappable.class.getPackage().getName());
			}
		}
	
		appendable.append(SINGLE_LINE_BREAK);
		appendable.append(TAB);
		appendable.append(OPENING_CURLY_BRACE);
		appendable.append(SINGLE_LINE_BREAK);
	}

	protected void superClassHook(ClassDescriptor inputClass, Appendable appendable) throws IOException
	{
		
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
			appendStructuredComments(appendable, TAB, comment);
	}

	@Override
	protected void appendClassMetaInformation(ClassDescriptor inputClass, Appendable appendable)
			throws IOException
	{
		List<MetaInformation> metaInfo = inputClass.getMetaInformation();
		appendClassMetaInformationHook(inputClass, appendable);
		if (metaInfo != null)
			for (MetaInformation piece : metaInfo)
			{
				appendable.append(TAB);
				appendMetaInformation(piece, appendable);
			}
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
	protected void appendClassGenericTypeVariables(Appendable appendable, ClassDescriptor inputClass)
			throws IOException
	{
		appendable.append(DotNetGenericsUtils.toDefinitionWithGenerics(inputClass));
	}

	@Override
	protected void appendSuperClassGenericTypeVariables(Appendable appendable,
			ClassDescriptor inputClass) throws IOException
	{
//		StringBuilder sb = new StringBuilder();
//		List<GenericTypeVar> genericTypeVars = inputClass.getGenericTypeVars();
//		if (genericTypeVars != null && genericTypeVars.size() > 0)
//		{
//			sb.append(" where ");
//			int len = sb.length();
//			for (int i = 0; i < genericTypeVars.size(); ++i)
//			{
//				GenericTypeVar genericTypeVar = genericTypeVars.get(i);
//				sb.append(i == 0 ? "" : ", ");
//				if (genericTypeVar.getConstraintGenericTypeVar() != null)
//				{
//					sb.append(genericTypeVar.getName()).append(" : ")
//					  .append(genericTypeVar.getConstraintGenericTypeVar().getName());
//				}
//				else if (genericTypeVar.getConstraintClassDescriptor() != null)
//				{
//					sb.append(genericTypeVar.getName()).append(" : ")
//					  .append(genericTypeVar.getConstraintClassDescriptor().getDescribedClassSimpleName());
//					List<GenericTypeVar> CGTVargs = genericTypeVar.getConstraintGenericTypeVarArgs();
//					if (CGTVargs != null && CGTVargs.size() > 0)
//					{
//						sb.append('<');
//						for (int j = 0; j < CGTVargs.size(); ++j)
//						{
//							GenericTypeVar cgtv = CGTVargs.get(j);
//							sb.append(j == 0 ? "" : ", ").append(cgtv.getName());
//						}
//						sb.append('>');
//					}
//				}
//				else
//				{
//					warning("Unprocessed generic type var: " + genericTypeVar);
//				}
//			}
//			if (sb.length() > len)
//				appendable.append(sb);
//		}
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
		appendFieldMetaInformation(context, fieldDescriptor, appendable);
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

	@Override
	protected void appendFieldGenericTypeVars(ClassDescriptor contextCd,
			FieldDescriptor fieldDescriptor, Appendable appendable) throws IOException
	{
		// TODO Auto-generated method stub
		
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
	protected void appendFieldMetaInformation(ClassDescriptor contextCd, FieldDescriptor fieldDescriptor,
			Appendable appendable) throws IOException
	{
		List<MetaInformation> metaInfo = fieldDescriptor.getMetaInformation();
		appendFieldMetaInformationHook(contextCd, fieldDescriptor, appendable);
		if (metaInfo != null)
			for (MetaInformation piece : metaInfo)
			{
				appendable.append(DOUBLE_TAB);
				appendMetaInformation(piece, appendable);
			}
	}

	@Override
	protected void appendFieldMetaInformationHook(ClassDescriptor contextCd, FieldDescriptor fieldDesc,
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
	protected void appendConstructor(ClassDescriptor inputClass, Appendable appendable, String className)
			throws IOException
	{
		appendDefaultConstructor(inputClass.getDescribedClassSimpleName(), appendable);
		
		appendConstructorHook(inputClass, appendable, null);
	}

	@Override
	protected void appendConstructorHook(ClassDescriptor inputClass, Appendable appendable, String classSimpleName) throws IOException
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
	
		String fieldName = fieldDescriptor.getName();
		String propertyName = DotNetTranslationUtilities.getPropertyName(context, fieldDescriptor);
		boolean isKeyword = checkForKeywords(fieldDescriptor);
		if (isKeyword)
				appendable.append(OPEN_BLOCK_COMMENTS).append(SINGLE_LINE_BREAK);
		appendable.append(DOUBLE_TAB);
		appendable.append(PUBLIC);
		appendable.append(SPACE);
		appendable.append(cSharpType);
		appendable.append(SPACE);
		appendable.append(propertyName);
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
		appendable.append(fieldName);
		appendable.append(END_LINE);
		appendable.append(CLOSING_CURLY_BRACE);
		appendable.append(SINGLE_LINE_BREAK);
		appendable.append(DOUBLE_TAB);
		appendable.append(TAB);
		appendable.append(SET);
		appendable.append(SINGLE_LINE_BREAK);
		appendable.append("\t\t\t{\n");
		appendable.append("\t\t\t\tif (this.").append(fieldName).append(" != value)\n");
		appendable.append("\t\t\t\t{\n");
		appendable.append("\t\t\t\t\tthis.").append(fieldName).append(" = value;\n");
		appendable.append("\t\t\t\t\tthis.RaisePropertyChanged( () => this.").append(propertyName).append(" );\n");
		appendable.append("\t\t\t\t}\n");
		appendable.append("\t\t\t}\n");
		appendable.append(DOUBLE_TAB);
		appendable.append(CLOSING_CURLY_BRACE);
		appendable.append(SINGLE_LINE_BREAK);
		if (isKeyword)
				appendable.append(CLOSE_BLOCK_COMMENTS).append(SINGLE_LINE_BREAK);
	
		appendGettersAndSettersHook(context, fieldDescriptor, appendable);
	}

	@Override
	protected void appendGettersAndSettersHook(ClassDescriptor context,
			FieldDescriptor fieldDescriptor, Appendable appendable)
	{
		// TODO Auto-generated method stub
		
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
	protected void appendMetaInformation(MetaInformation metaInfo, Appendable appendable)
			throws IOException
	{
		String metaInfoPackage = metaInfo.typeName.substring(0, metaInfo.typeName.lastIndexOf('.'));
		addCurrentClassDependency(DotNetTranslationUtilities.translateAnnotationPackage(metaInfoPackage));
		
		appendable.append(OPENING_SQUARE_BRACE);
		appendable.append(DotNetTranslationUtilities.translateAnnotationName(metaInfo.simpleTypeName));
		if (metaInfo.args != null && metaInfo.args.size() > 0)
		{
			appendable.append("(");
			if (metaInfo.argsInArray)
			{
				String argType = metaInfo.args.get(0).simpleTypeName;
				if (argType.equals("Class"))
					argType = "Type";
				appendable.append("new ").append(argType).append("[] {");
				for (int i = 0; i < metaInfo.args.size(); ++i)
					appendable.append(i==0?"":", ").append(translateMetaInfoArgValue(metaInfo.args.get(i).value));
				appendable.append("}");
			}
			else
			{
				for (int i = 0; i < metaInfo.args.size(); ++i)
				{
					Argument a = metaInfo.args.get(i);
					appendable.append(i==0?"":", ").append(metaInfo.args.size()>1?a.name+" = ":"").append(translateMetaInfoArgValue(a.value));
				}
			}
			appendable.append(")");
		}
		appendable.append(CLOSING_SQUARE_BRACE);
		appendable.append(SINGLE_LINE_BREAK);
	}
	
	public String translateMetaInfoArgValue(Object argValue)
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
		else if (argValue instanceof Class)
		{
			return "typeof(" + ((Class) argValue).getSimpleName() + ")";
		}
		// eles if (argValue instanceof ClassDescriptor)
		return null;
	}

	@Override
	public void addGlobalDependency(String name)
	{
		if (name != null)
			globalDependencies.add(name);
	}

	@Override
	public void addCurrentClassDependency(String name)
	{
		if (name != null)
			currentClassDependencies.add(name);
	}
	
	@Override
	public void addLibraryTScopeDependency(String name)
	{
		if (name != null)
			libraryTScopeDependencies.add(name);
	}

	/**
	 * @param directoryLocation
	 * @param tScope
	 * @throws IOException
	 */
	@Override
	public void generateLibraryTScopeClass(File directoryLocation, SimplTypesScope tScope, String tscopePackageName, String tscopeClassName)
			throws IOException
	{
		File sourceFile = createFileWithDirStructure(directoryLocation, tscopePackageName.split(PACKAGE_NAME_SEPARATOR), tscopeClassName, FILE_EXTENSION);
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(sourceFile));

		// append dependencies
		appendDependencies(globalDependencies, bufferedWriter);
		appendDependencies(libraryTScopeDependencies, bufferedWriter);
		
		// append notice information
		bufferedWriter.append("// Developer should proof-read this TranslationScope before using it for production.");
		bufferedWriter.append(SINGLE_LINE_BREAK);
		
		// header
		bufferedWriter.append(NAMESPACE);
		bufferedWriter.append(SPACE);
		bufferedWriter.append(tscopePackageName);
		bufferedWriter.append(SPACE);
		bufferedWriter.append(SINGLE_LINE_BREAK);
		bufferedWriter.append(OPENING_CURLY_BRACE);
		bufferedWriter.append(SINGLE_LINE_BREAK);
		
		// class: header
		bufferedWriter.append(TAB);
		bufferedWriter.append(PUBLIC);
		bufferedWriter.append(SPACE);
		bufferedWriter.append(CLASS);
		bufferedWriter.append(SPACE);
		bufferedWriter.append(tscopeClassName);
		bufferedWriter.append(SINGLE_LINE_BREAK);
		bufferedWriter.append(TAB);
		bufferedWriter.append(OPENING_CURLY_BRACE);
		bufferedWriter.append(SINGLE_LINE_BREAK);
		
		// class: constructor
		appendDefaultConstructor(tscopeClassName, bufferedWriter);
		
		// class: the Get() method
		generateLibraryTScopeGetter(bufferedWriter, tScope);
		
		closeClassBody(bufferedWriter);
		closeUnitScope(bufferedWriter);
		
		bufferedWriter.close();
	}

	protected void generateLibraryTScopeGetter(Appendable appendable, SimplTypesScope tScope)
			throws IOException
	{
		appendable.append(SINGLE_LINE_BREAK);
		appendable.append(DOUBLE_TAB);
		appendable.append(PUBLIC);
		appendable.append(SPACE);
		appendable.append(STATIC);
		appendable.append(SPACE);
		appendable.append(DOTNET_TRANSLATION_SCOPE);
		appendable.append(SPACE);
		appendable.append("Get");
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
		appendTranslatedClassList(tScope, appendable);
		appendable.append(CLOSING_BRACE);
		appendable.append(END_LINE);
		appendable.append(SINGLE_LINE_BREAK);
		appendable.append(DOUBLE_TAB);
		appendable.append(CLOSING_CURLY_BRACE);
		appendable.append(SINGLE_LINE_BREAK);
	}

	@Override
	protected void appendTranslatedClassList(SimplTypesScope tScope, Appendable appendable) throws IOException
	{
		List<String> lines = new ArrayList<String>();
		
		Collection<ClassDescriptor<? extends FieldDescriptor>> allClasses = tScope.entriesByClassName().values();
		for (ClassDescriptor<? extends FieldDescriptor> oneClass : allClasses)
		{
			if (excludeClassesFromTranslation.contains(oneClass))
				continue;
			lines.add(String.format(",\n\t\t\t\ttypeof(%s%s)", oneClass.getDescribedClassSimpleName(), oneClass.isGenericClass() ? "<>" : ""));
		}
		Collections.sort(lines);
		for (String line : lines)
			appendable.append(line);
	}

	/**
	 * @param someClass
	 */
	@Override
	public void excludeClassFromTranslation(ClassDescriptor someClass)
	{
		excludeClassesFromTranslation.add(someClass);
	}
	
}
