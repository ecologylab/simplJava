package ecologylab.semantics.compiler;

import static ecologylab.translators.net.DotNetTranslationConstants.SPACE;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import ecologylab.generic.Debug;
import ecologylab.generic.StringTools;
import ecologylab.semantics.html.utils.StringBuilderUtils;
import ecologylab.semantics.metadata.MetadataClassDescriptor;
import ecologylab.semantics.metadata.MetadataFieldDescriptor;
import ecologylab.semantics.metametadata.MetaMetadata;
import ecologylab.semantics.metametadata.MetaMetadataCompositeField;
import ecologylab.semantics.metametadata.MetaMetadataField;
import ecologylab.semantics.metametadata.MetaMetadataNestedField;
import ecologylab.semantics.metametadata.MetaMetadataRepository;
import ecologylab.semantics.metametadata.MmdCompilerService;
import ecologylab.semantics.metametadata.MmdGenericTypeVar;
import ecologylab.semantics.namesandnums.SemanticsNames;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.MetaInformation;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.simpl_descriptor_classes;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.translators.net.DotNetTranslationException;
import ecologylab.translators.net.DotNetTranslator;

public class MetaMetadataDotNetTranslator extends DotNetTranslator implements MmdCompilerService
{
	
	public static final String	SCALAR_GETTER_SETTER_SUFFIX	= "Metadata";
	
	private static String[]			metaMetadataDefaultImports	= {
		"Ecologylab.Semantics.MetaMetadataNS",
		"Ecologylab.Semantics.Metadata.Builtins",
	};

	public MetaMetadataDotNetTranslator()
	{
		for (String importTarget : metaMetadataDefaultImports)
		{
			addGlobalDependency(importTarget);
		}
		
//		addLibraryTScopeDependency("ecologylab.semantics.metadata.builtins");
	}
	
  private static String csharpNSMetadata = "Ecologylab.Semantics.Metadata";
  private static String csharpNSMetadataNS = "Ecologylab.Semantics.MetadataNS";
  private static String csharpNSMetaMetadata = "Ecologylab.Semantics.Metametadata";
  private static String csharpNSMetaMetadataNS = "Ecologylab.Semantics.MetaMetadataNS";
  private static String csharpNSLibraryDot = "Ecologylab.Semantics.Generated.Library.";

	protected String javaPackage2CSharpNamespace(String packageName)
	{
	  String ns = super.javaPackage2CSharpNamespace(packageName);
	  
	  if (ns.startsWith(csharpNSMetadata)
	      && !ns.startsWith(csharpNSMetadataNS))
	  {
	    ns = ns.replace(csharpNSMetadata, csharpNSMetadataNS);
	    return ns;
	  }
  	  
	  if (ns.startsWith(csharpNSMetaMetadata)
	      && !ns.startsWith(csharpNSMetaMetadataNS))
	  {
	    ns = ns.replace(csharpNSMetaMetadata, csharpNSMetaMetadataNS);
	    return ns;
	  }
  	  
	  if (ns.startsWith(csharpNSLibraryDot))
	  {
	    if (!ns.endsWith("NS"))
        return ns + "NS";
	  }
	  
	  return ns;
	}
	
	private static final String csharpSemanticsNamespacePrefix = "Ecologylab.Semantics.";
	private static final String csharpGeneratedSemanticsNamespacePrefix = "Ecologylab.Semantics.Generated.";

	@Override
	protected String[] getGeneratedClassFileDirStructure(ClassDescriptor inputClass)
  {
    String packageName = inputClass.getDescribedClassPackageName();
    String[] result = getGeneratedClassFileDirStructure(packageName);
    
    String csharpNamespace = javaPackage2CSharpNamespace(packageName);
    inputClass.setDescribedClassPackageName(csharpNamespace);
    
    return result;
  }
	
	@Override
	protected String[] getGeneratedClassFileDirStructure(String classPackageName)
	{
    String csharpNamespace = javaPackage2CSharpNamespace(classPackageName);
    if (csharpNamespace.startsWith(csharpGeneratedSemanticsNamespacePrefix))
    {
      String dirStructureStr = csharpNamespace.substring(csharpGeneratedSemanticsNamespacePrefix.length());
      return dirStructureStr.split(PACKAGE_NAME_SEPARATOR);
    }
    else if (csharpNamespace.startsWith(csharpSemanticsNamespacePrefix))
    {
      String dirStructureStr = csharpNamespace.substring(csharpSemanticsNamespacePrefix.length());
      return dirStructureStr.split(PACKAGE_NAME_SEPARATOR);
    }
    else
    {
      return super.getGeneratedClassFileDirStructure(classPackageName);
    }
	}
	
	@Override
	protected void appendClassMetaInformationHook(ClassDescriptor classDesc, Appendable appendable)
	{
		MetaMetadata definingMmd = ((MetadataClassDescriptor) classDesc).getDefiningMmd();
		if (definingMmd.isRootMetaMetadata())
		{
			List<MetaInformation> metaInfo = classDesc.getMetaInformation();
			metaInfo.add(new MetaInformation(simpl_descriptor_classes.class, true,
					MetadataClassDescriptor.class, MetadataFieldDescriptor.class));
			metaInfo.add(new MetaInformation(simpl_inherit.class));
			this.addCurrentClassDependency("Simpl.Serialization.Attributes");
			this.addCurrentClassDependency(MetadataClassDescriptor.class.getPackage().getName());
			this.addCurrentClassDependency(MetadataFieldDescriptor.class.getPackage().getName());
		}
		super.appendClassMetaInformationHook(classDesc, appendable);
	}
	
	@Override
	protected void superClassHook(ClassDescriptor inputClass, Appendable appendable) throws IOException
	{
		MetaMetadata definingMmd = ((MetadataClassDescriptor) inputClass).getDefiningMmd();
		if (definingMmd.isRootMetaMetadata())
		{
		  // TODO we are moving away from Prism stuffs ...
//			appendable.append(" : NotificationObject");
//			addCurrentClassDependency("Microsoft.Practices.Prism.ViewModel");
		}
	}

	@Override
	protected void appendClassGenericTypeVariables(Appendable appendable, ClassDescriptor inputClass) throws IOException
	{
		MetadataClassDescriptor mdCD = (MetadataClassDescriptor) inputClass;
		MetaMetadata mmd = mdCD.getDefiningMmd();
		MetaMetadataRepository repository = mmd.getRepository();
		appendGenericTypeVarDefinitions(appendable, mmd.getMetaMetadataGenericTypeVars(), repository);
	}
	
	@Override
	protected void appendSuperClassGenericTypeVariables(Appendable appendable,
			ClassDescriptor inputClass) throws IOException
	{ 
		MetadataClassDescriptor mdCD = (MetadataClassDescriptor) inputClass;
		MetaMetadata mmd = mdCD.getDefiningMmd();
		MetaMetadataRepository repository = mmd.getRepository();

		int firstIndexOf = appendable.toString().lastIndexOf("<");
		Collection<MmdGenericTypeVar> mmdGenericTypeVars = mmd.getMetaMetadataGenericTypeVars();
		appendGenericTypeVarParameterizations(appendable, mmdGenericTypeVars, repository);

		Collection<MmdGenericTypeVar> superMmdGenericTypeVars = ((MetadataClassDescriptor) inputClass.getSuperClass()).getDefiningMmd().getMetaMetadataGenericTypeVars();
		if (appendable.toString().lastIndexOf("<") == firstIndexOf /*(mmdGenericTypeVars == null || mmdGenericTypeVars.size() == 0)*/ &&
			(superMmdGenericTypeVars != null || superMmdGenericTypeVars.size() > 0))
		{
			appendGenericTypeVarExtends(appendable, superMmdGenericTypeVars, repository);
		}
		
		// the where clause
		appendGenericTypeVarWhereClause(appendable, (Collection<MmdGenericTypeVar>) mmd.getMetaMetadataGenericTypeVars(), repository);
	}

	public void appendGenericTypeVarExtends(Appendable appendable,
			Collection<MmdGenericTypeVar> mmdGenericTypeVars, MetaMetadataRepository repository)
			throws IOException
	{
		if (mmdGenericTypeVars != null && mmdGenericTypeVars.size() > 0)
		{
			boolean first = true;
			for (MmdGenericTypeVar mmdGenericTypeVar : mmdGenericTypeVars)
			{
				String varName = mmdGenericTypeVar.getName();
				String extendsName = mmdGenericTypeVar.getExtendsAttribute();
				String argName = mmdGenericTypeVar.getArg();
				if (varName != null && extendsName != null && argName == null)
				{
					if (first)
					{
						appendable.append("<");
						first = false;
					}
					else
						appendable.append(", ");
					appendable.append(MmdGenericTypeVar.getMdClassNameFromMmdOrNoChange(extendsName, repository, this));
					appendGenericTypeVarExtends(appendable, mmdGenericTypeVar.getNestedGenericTypeVars(), repository);
				}
			}
			if (!first)
				appendable.append(">");
		}		
	}
	
	@Override
	protected void appendField(ClassDescriptor context, FieldDescriptor fieldDescriptor, Appendable appendable)
			throws DotNetTranslationException, IOException
	{
		((MetadataFieldDescriptor) fieldDescriptor).setCompilerService(this);
		super.appendField(context, fieldDescriptor, appendable);
	}

	@Override
	protected void appendFieldGenericTypeVars(ClassDescriptor contextCd,
			FieldDescriptor fieldDescriptor, Appendable appendable) throws IOException
	{
		if (fieldDescriptor.getCollectionType() != null)
			return; // should have been handled by MetadataFieldDescriptor.getJavaType()
		MetadataFieldDescriptor mdFD = (MetadataFieldDescriptor) fieldDescriptor;
		MetaMetadataField field = mdFD.getDefiningMmdField();
		if (field instanceof MetaMetadataNestedField)
		{
			MetaMetadataNestedField nestedField = (MetaMetadataNestedField) field;
			MetaMetadataRepository repository = nestedField.getRepository();
			appendGenericTypeVarParameterizations(appendable, (List<MmdGenericTypeVar>) nestedField
					.getMetaMetadataGenericTypeVars(), repository);
		}
	}

	public void appendGenericTypeVarDefinitions(Appendable appendable,
			Collection<MmdGenericTypeVar> mmdGenericTypeVars, MetaMetadataRepository repository)
			throws IOException
	{
		if (mmdGenericTypeVars != null && mmdGenericTypeVars.size() > 0)
		{
			boolean first = true;
			for (MmdGenericTypeVar mmdGenericTypeVar : mmdGenericTypeVars)
			{
				String varName = mmdGenericTypeVar.getName();
				String extendsName = mmdGenericTypeVar.getExtendsAttribute();
				String argName = mmdGenericTypeVar.getArg();
				if (varName != null && extendsName != null && argName == null)
				{
					if (!StringTools.isUpperCase(varName))
					{
						Debug.warning(MmdGenericTypeVar.class, "We recommend capital letters for generic variable names!");
					}
					if (first)
					{
						appendable.append("<");
						first = false;
					}
					else
						appendable.append(", ");
					appendable.append(varName);
					//appendGenericTypeVarParameterizations(appendable, mmdGenericTypeVar.getNestedGenericTypeVars(), repository);
				}
			}
			if (!first)
				appendable.append(">");
		}
	}

	public void appendGenericTypeVarWhereClause(Appendable appendable,
			Collection<MmdGenericTypeVar> mmdGenericTypeVars, MetaMetadataRepository repository)
			throws IOException
	{
		if (mmdGenericTypeVars != null && mmdGenericTypeVars.size() > 0)
		{
			boolean first = true;
			for (MmdGenericTypeVar mmdGenericTypeVar : mmdGenericTypeVars)
			{
				String varName = mmdGenericTypeVar.getName();
				String extendsName = mmdGenericTypeVar.getExtendsAttribute();
				String argName = mmdGenericTypeVar.getArg();
				if (varName != null && extendsName != null && argName == null)
				{
					if (!StringTools.isUpperCase(varName))
					{
						Debug.warning(MmdGenericTypeVar.class, "We recommend capital letters for generic variable names!");
					}
					if (first)
					{
						appendable.append(" where ");
						first = false;
					}
					else
						appendable.append(", ");
					appendable
							.append(varName)
							.append(" : ")
							.append(MmdGenericTypeVar.getMdClassNameFromMmdOrNoChange(extendsName, repository, this));
					appendGenericTypeVarParameterizations(appendable, mmdGenericTypeVar.getNestedGenericTypeVars(), repository);
				}
			}
		}
	}

	@Override
	public void appendGenericTypeVarParameterizations(Appendable appendable,
			Collection<MmdGenericTypeVar> mmdGenericTypeVars, MetaMetadataRepository repository)
			throws IOException
	{
		if (mmdGenericTypeVars != null && mmdGenericTypeVars.size() > 0)
		{
			boolean first = true;
			for (MmdGenericTypeVar mmdGenericTypeVar : mmdGenericTypeVars)
			{
				String varName = mmdGenericTypeVar.getName();
				String extendsName = mmdGenericTypeVar.getExtendsAttribute();
				String argName = mmdGenericTypeVar.getArg();
				if (argName != null && ((varName == null && extendsName == null) || varName != null))
				{
					if (first)
					{
						appendable.append("<");
						first = false;
					}
					else
						appendable.append(", ");
					appendable.append(MmdGenericTypeVar.getMdClassNameFromMmdOrNoChange(argName, repository, this));

					int firstIndexOf = appendable.toString().lastIndexOf("<");
					appendGenericTypeVarParameterizations(appendable, mmdGenericTypeVar.getNestedGenericTypeVars(), repository);						

					if (appendable.toString().lastIndexOf("<") == firstIndexOf)
						appendGenericTypeVarExtends(appendable, mmdGenericTypeVar.getNestedGenericTypeVars(), repository);
				}
			}
			if (!first)
				appendable.append(">");
		}
	}

	@Override
	protected void appendFieldMetaInformationHook(ClassDescriptor contextCd, FieldDescriptor fieldDesc, Appendable appendable) throws IOException
	{
		super.appendFieldMetaInformationHook(contextCd, fieldDesc, appendable);
		MetadataFieldDescriptor fd = (MetadataFieldDescriptor) fieldDesc;
		List<MetaInformation> metaInfoBuf = fd.getMetaInformation();
		MetaMetadataField f = fd.getDefiningMmdField();
		f.addAdditionalMetaInformation(metaInfoBuf, this);
	}

	@Override
	protected void appendConstructorHook(ClassDescriptor inputClass, Appendable appendable, String classSimpleName) throws IOException
	{
		super.appendConstructorHook(inputClass, appendable, classSimpleName);
		
		MetaMetadata definingMmd = ((MetadataClassDescriptor) inputClass).getDefiningMmd();
		if (definingMmd.isRootMetaMetadata())
			return;

		appendable.append("\n");
		appendable.append("\t\tpublic ").append(inputClass.getDescribedClassSimpleName())
				.append("(MetaMetadataCompositeField mmd) : base(mmd) { }\n");
		appendable.append("\n");
		addCurrentClassDependency(MetaMetadataCompositeField.class.getPackage().getName());
	}

	@Override
	protected void appendGettersAndSettersHook(ClassDescriptor context, FieldDescriptor fieldDescriptor, Appendable appendable)
	{
		super.appendGettersAndSettersHook(context, fieldDescriptor, appendable);
		// don't need this right now.
	}
	
	@Override
	protected void generateLibraryTScopeGetter(Appendable appendable, SimplTypesScope tScope) throws IOException
	{
		CompilerConfig cconfig = (CompilerConfig) config; 
		if (cconfig.getBuiltinDeclarationScopeName() == null)
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
			appendable.append(SemanticsNames.REPOSITORY_METADATA_TYPE_SCOPE);
			appendable.append(QUOTE);
			appendTranslatedClassList(tScope, appendable);
			appendable.append(CLOSING_BRACE);
			appendable.append(END_LINE);
			appendable.append(SINGLE_LINE_BREAK);
			appendable.append(DOUBLE_TAB);
			appendable.append(CLOSING_CURLY_BRACE);
			appendable.append(SINGLE_LINE_BREAK);
		}
		else
		{
			appendable.append("\n");
			appendable.append("\t\tpublic static SimplTypesScope Get()\n");
			appendable.append("\t\t{\n");
			appendable.append("\t\t\treturn SimplTypesScope.Get(\"").append(SemanticsNames.REPOSITORY_BUILTIN_DECLARATIONS_TYPE_SCOPE).append("\"");
			super.appendTranslatedClassList(tScope, appendable);
			appendable.append(");\n");
			appendable.append("\t\t}\n\n");
		}
	}

	@Override
	protected void appendTranslatedClassList(SimplTypesScope tScope, Appendable appendable) throws IOException
	{
		appendable.append(", MetadataBuiltinsTypesScope.Get()");
		super.appendTranslatedClassList(tScope, appendable);
		
//		CompilerConfig config = (CompilerConfig) this.config;
//		MetaMetadataRepository repository = config.loadRepository();
//		Collection<MetaMetadata> mmds = repository.values();
//		if (mmds != null && mmds.size() > 0)
//		{
//			int i = 0;
//			for (MetaMetadata mmd : mmds)
//			{
//				if (mmd.isNewMetadataClass())
//				{
//					ClassDescriptor cd = mmd.getMetadataClassDescriptor();
//					if (i > 0)
//						appendable.append(",\n");
//					appendable.append("\t\t\t\ttypeof(" + cd.getDescribedClassSimpleName() + ")");
//				}
//				++i;
//			}
//		}
//		appendable.append("\n\t\t\t");
	}

	@Override
	public void addCurrentClassDependency(ClassDescriptor dependency)
	{
		addCurrentClassDependency(javaPackage2CSharpNamespace(dependency.getCSharpNamespace()));
	}
	
}
