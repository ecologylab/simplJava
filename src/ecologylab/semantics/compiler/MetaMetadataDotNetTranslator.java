package ecologylab.semantics.compiler;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import ecologylab.semantics.metadata.MetadataFieldDescriptor;
import ecologylab.semantics.metametadata.MetaMetadata;
import ecologylab.semantics.metametadata.MetaMetadataCompositeField;
import ecologylab.semantics.metametadata.MetaMetadataField;
import ecologylab.semantics.metametadata.MetaMetadataRepository;
import ecologylab.semantics.metametadata.MmdCompilerService;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.MetaInformation;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.translators.net.DotNetTranslator;

public class MetaMetadataDotNetTranslator extends DotNetTranslator implements MmdCompilerService
{
	
	public static final String	SCALAR_GETTER_SETTER_SUFFIX	= "Metadata";

	private static String[]			metaMetadataDefaultImports	= {
		"ecologylab.semantics.metametadata",
		"ecologylab.semantics.metadata.builtins",
	};

	public MetaMetadataDotNetTranslator()
	{
		for (String importTarget : metaMetadataDefaultImports)
		{
			addGlobalDependency(importTarget);
		}
		
//		addLibraryTScopeDependency("ecologylab.semantics.metadata.builtins");
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
	protected void appendTranslatedClassList(SimplTypesScope tScope, Appendable appendable) throws IOException
	{
		appendable.append(", MetadataBuiltinsTranslationScope.Get(),\n");
		
		CompilerConfig config = (CompilerConfig) this.config;
		MetaMetadataRepository repository = config.loadRepository();
		Collection<MetaMetadata> mmds = repository.values();
		if (mmds != null && mmds.size() > 0)
		{
			int i = 0;
			for (MetaMetadata mmd : mmds)
			{
				if (mmd.isNewMetadataClass())
				{
					ClassDescriptor cd = mmd.getMetadataClassDescriptor();
					if (i > 0)
						appendable.append(",\n");
					appendable.append("\t\t\t\ttypeof(" + cd.getDescribedClassSimpleName() + ")");
				}
				++i;
			}
		}
		appendable.append("\n\t\t\t");
	}
	
}
