package ecologylab.semantics.compiler;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import ecologylab.generic.Debug;
import ecologylab.generic.StringTools;
import ecologylab.semantics.metadata.MetadataClassDescriptor;
import ecologylab.semantics.metadata.MetadataFieldDescriptor;
import ecologylab.semantics.metadata.builtins.MetadataBuiltinsTypesScope;
import ecologylab.semantics.metametadata.MetaMetadata;
import ecologylab.semantics.metametadata.MetaMetadataCompositeField;
import ecologylab.semantics.metametadata.MetaMetadataField;
import ecologylab.semantics.metametadata.MetaMetadataNestedField;
import ecologylab.semantics.metametadata.MetaMetadataRepository;
import ecologylab.semantics.metametadata.MetaMetadataScalarField;
import ecologylab.semantics.metametadata.MmdCompilerService;
import ecologylab.semantics.metametadata.MmdGenericTypeVar;
import ecologylab.semantics.metametadata.exceptions.MetaMetadataException;
import ecologylab.semantics.namesandnums.SemanticsNames;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.FieldTypes;
import ecologylab.serialization.MetaInformation;
import ecologylab.serialization.annotations.simpl_descriptor_classes;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.types.ScalarType;
import ecologylab.translators.java.JavaTranslationException;
import ecologylab.translators.java.JavaTranslator;

public class MetaMetadataJavaTranslator extends JavaTranslator implements MmdCompilerService
{

	public static final String	SCALAR_GETTER_SETTER_SUFFIX			= "Metadata";

	private static String[]			metaMetadataDefaultDependencies	= {
			MetaMetadataCompositeField.class.getName(),
			SemanticsNames.class.getName(),
			MetadataBuiltinsTypesScope.class.getName(),
	};

	public MetaMetadataJavaTranslator()
	{
		super();
	}
	
	@Override
	protected void initGlobalDependencies()
	{
		super.initGlobalDependencies();
		for (String dependency : metaMetadataDefaultDependencies)
			this.addGlobalDependency(dependency);
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
			this.addCurrentClassDependency(simpl_descriptor_classes.class.getName());
			this.addCurrentClassDependency(simpl_inherit.class.getName());
			this.addCurrentClassDependency(MetadataClassDescriptor.class.getName());
			this.addCurrentClassDependency(MetadataFieldDescriptor.class.getName());
		}
		super.appendClassMetaInformationHook(classDesc, appendable);
	}
	
	@Override
	protected void appendClassGenericTypeVariables(Appendable appendable, ClassDescriptor inputClass)
			throws IOException
	{
		MetadataClassDescriptor mdInputClass = (MetadataClassDescriptor) inputClass;
		MetaMetadata mmd = mdInputClass.getDefiningMmd();
		MetaMetadataRepository repository = mmd.getRepository();
		appendGenericTypeVarDefinitions(appendable, mmd.getMetaMetadataGenericTypeVars(), repository);
	}

	@Override
	protected void appendSuperClassGenericTypeVariables(Appendable appendable,
			ClassDescriptor inputClass) throws IOException
	{
		MetadataClassDescriptor mdInputClass = (MetadataClassDescriptor) inputClass;
		MetaMetadata mmd = mdInputClass.getDefiningMmd();
		MetaMetadataRepository repository = mmd.getRepository();
		appendGenericTypeVarParameterizations(appendable, mmd.getMetaMetadataGenericTypeVars(), repository);
	}

	@Override
	protected void appendField(ClassDescriptor contextCd, FieldDescriptor fieldDescriptor, Appendable appendable)
			throws IOException, JavaTranslationException
	{
		((MetadataFieldDescriptor) fieldDescriptor).setCompilerService(this);
		super.appendField(contextCd, fieldDescriptor, appendable);
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
			appendGenericTypeVarParameterizations(appendable, nestedField.getMetaMetadataGenericTypeVars(), repository);
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
					appendable.append(varName).append(" extends ").append(MmdGenericTypeVar.getMdClassNameFromMmdOrNoChange(extendsName, repository, this));
					appendGenericTypeVarParameterizations(appendable, mmdGenericTypeVar.getNestedGenericTypeVars(), repository);
				}
			}
			if (!first)
				appendable.append(">");
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
				if (argName != null && extendsName == null)
				{
					if (first)
					{
						appendable.append("<");
						first = false;
					}
					else
						appendable.append(", ");
					appendable.append(MmdGenericTypeVar.getMdClassNameFromMmdOrNoChange(argName, repository, this));
					appendGenericTypeVarParameterizations(appendable, mmdGenericTypeVar.getNestedGenericTypeVars(), repository);
				}
			}
			if (!first)
				appendable.append(">");
		}
	}

	@Override
	protected void appendFieldMetaInformationHook(ClassDescriptor contextCd,
			FieldDescriptor fieldDesc, Appendable appendable) throws IOException
	{
		super.appendFieldMetaInformationHook(contextCd, fieldDesc, appendable);
		MetadataFieldDescriptor fd = (MetadataFieldDescriptor) fieldDesc;
		MetaMetadataField f = fd.getDefiningMmdField();
		if (f != null)
			f.addAdditionalMetaInformation(fieldDesc.getMetaInformation(), this);
		else
			warning("MetaMetadataField not found for " + fd);
	}

	@Override
	protected void appendConstructorHook(ClassDescriptor inputClass, Appendable appendable,
			String classSimpleName) throws IOException
	{
		super.appendConstructorHook(inputClass, appendable, classSimpleName);
		
		MetaMetadata definingMmd = ((MetadataClassDescriptor) inputClass).getDefiningMmd();
		if (definingMmd.isRootMetaMetadata())
			return;

		appendable.append("\n");
		appendable.append("\tpublic ").append(classSimpleName)
				.append("(MetaMetadataCompositeField mmd) {\n");
		appendable.append("\t\tsuper(mmd);\n");
		appendable.append("\t}\n");
		appendable.append("\n");
	}

	protected void appendLazyEvaluation(String fieldName, String typeName, Appendable appendable)
			throws IOException
	{
		appendable.append("\n");

		// TODO write comments?

		// first line. Start of method name
		appendable.append("\tpublic ").append(typeName).append("\t").append(fieldName)
				.append("()\n\t{\n");

		// second line. Declaration of result variable.
		appendable.append("\t\t").append(typeName).append("\t").append("result = this.")
				.append(fieldName).append(";\n");

		// third line. Start of if statement
		appendable.append("\t\tif (result == null)\n\t\t{\n");

		// fourth line. creation of new result object.
		appendable.append("\t\t\tresult = new ").append(typeName).append("();\n");

		// fifth line. end of if statement
		appendable.append("\t\t\tthis.").append(fieldName).append(" = result;\n\t\t}\n");

		// sixth line. return statement and end of method.
		appendable.append("\t\treturn result;\n\t}\n");
	}

	@Override
	protected void appendGetters(FieldDescriptor fieldDescriptor, Appendable appendable, String suffix)
			throws IOException
	{
		if (fieldDescriptor.getType() == FieldTypes.SCALAR)
		{
			appendValueGetter(fieldDescriptor, appendable);
			super.appendGetters(fieldDescriptor, appendable, SCALAR_GETTER_SETTER_SUFFIX);
		}
		else
		{
			super.appendGetters(fieldDescriptor, appendable, suffix);
		}
	}

	private void appendValueGetter(FieldDescriptor fieldDescriptor, Appendable appendable)
			throws IOException
	{
		MetaMetadataScalarField scalar = (MetaMetadataScalarField) ((MetadataFieldDescriptor) fieldDescriptor)
				.getDefiningMmdField();

		// metadata scalar types!
		ScalarType<?> scalarType = fieldDescriptor.getScalarType();
		if (scalarType == null)
			throw new MetaMetadataException("scalar type not specified!");

		String fieldName = fieldDescriptor.getName();
		String capFieldName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
		String typeName = scalarType.getSimpleName();

		appendLazyEvaluation(fieldName, typeName, appendable);

		ScalarType operativeScalarType = scalarType.operativeScalarType();
		String javaPrimitiveTypeName = operativeScalarType.getSimpleName();
		appendable.append("\n");
		appendable.append("\tpublic ").append(javaPrimitiveTypeName).append(" get")
				.append(capFieldName).append("()\n");
		appendable.append("\t{\n");
		appendable.append("\t\treturn this.").append(fieldName).append(" == null ? ")
				.append(operativeScalarType.defaultValueString()).append(" : ").append(fieldName)
				.append("().getValue();\n");
		appendable.append("\t}\n");
	}

	@Override
	protected void appendSetters(FieldDescriptor fieldDescriptor, Appendable appendable, String suffix)
			throws IOException
	{
		if (fieldDescriptor.getType() == FieldTypes.SCALAR)
		{
			appendValueSetter(fieldDescriptor, appendable);
			super.appendSetters(fieldDescriptor, appendable, SCALAR_GETTER_SETTER_SUFFIX);
		}
		else
		{
			super.appendSetters(fieldDescriptor, appendable, suffix);
		}
	}

	private void appendValueSetter(FieldDescriptor fieldDescriptor, Appendable appendable)
			throws IOException
	{
		// metadata scalar types!
		ScalarType<?> scalarType = fieldDescriptor.getScalarType();
		if (scalarType == null)
			throw new MetaMetadataException("scalar type not specified!");

		String fieldName = fieldDescriptor.getName();
		String capFieldName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);

		ScalarType operativeScalarType = scalarType.operativeScalarType();
		String javaPrimitiveTypeName = operativeScalarType.getSimpleName();
		appendable.append("\n");
		appendable.append("\tpublic void set").append(capFieldName).append("(")
				.append(javaPrimitiveTypeName).append(" ").append(fieldName).append(")\n");
		appendable.append("\t{\n");
		appendable.append("\t\tif (").append(fieldName).append(" != ")
				.append(operativeScalarType.defaultValueString()).append(")\n");
		appendable.append("\t\t\tthis.").append(fieldName).append("().setValue(").append(fieldName)
				.append(");\n");
		appendable.append("\t}\n");
	}

//	@Override
//	protected void appendTranslatedClassList(SimplTypesScope tScope, Appendable appendable)
//			throws IOException
//	{
//		List<String> classes = new ArrayList<String>();
//		MetaMetadataRepository repository = ((CompilerConfig) config).loadRepository();
//		if (repository.values() != null)
//			for (MetaMetadata mmd : repository.values())
//				if (mmd.isNewMetadataClass() && !mmd.isBuiltIn())
//				{
//					ClassDescriptor cd = mmd.getMetadataClassDescriptor();
//					classes.add("\t\t" + cd.getDescribedClassName() + ".class,\n\n");
//				}
//		Collections.sort(classes);
//		for (String classDef : classes)
//			appendable.append(classDef);
//	}

	@Override
	protected void generateLibraryTScopeGetter(Appendable appendable, String tScopeName) throws IOException
	{
		appendable.append("\tpublic static ").append(JAVA_TRANSLATION_SCOPE).append(" get()\n\t{\n");
		appendable.append("\t\treturn ").append(JAVA_TRANSLATION_SCOPE).append(".get(");
		
		CompilerConfig cconfig = (CompilerConfig) config; 
		if (cconfig.getBuiltinDeclarationScopeName() == null)
		{
			appendable.append("SemanticsNames.REPOSITORY_METADATA_TYPE_SCOPE, MetadataBuiltinsTypesScope.get()");
		}
		else
		{
			appendable.append('"').append(cconfig.getBuiltinDeclarationScopeName()).append('"');
		}
		appendable.append(", TRANSLATIONS);\n");
		appendable.append("\t}\n\n");
	}

	@Override
	public void addCurrentClassDependency(ClassDescriptor dependency)
	{
		addCurrentClassDependency(dependency.getDescribedClassName());
	}

}
