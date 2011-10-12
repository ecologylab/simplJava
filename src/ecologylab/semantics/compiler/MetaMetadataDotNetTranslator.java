package ecologylab.semantics.compiler;

import static ecologylab.translators.net.DotNetTranslationConstants.SPACE;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import ecologylab.semantics.metadata.MetadataFieldDescriptor;
import ecologylab.semantics.metametadata.MetaMetadata;
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
	};

	public MetaMetadataDotNetTranslator()
	{
		for (String importTarget : metaMetadataDefaultImports)
		{
			addGlobalDependency(importTarget);
		}
		
		addLibraryTScopeDependency("ecologylab.semantics.metadata.builtins");
	}

	@Override
	protected void appendFieldMetaInformationHook(ClassDescriptor contextCd, FieldDescriptor fieldDesc, Appendable appendable) throws IOException
	{
		MetadataFieldDescriptor fd = (MetadataFieldDescriptor) fieldDesc;
		List<MetaInformation> metaInfoBuf = fd.getMetaInformation();
		MetaMetadataField f = fd.getDefiningMmdField();
		f.addAdditionalMetaInformation(metaInfoBuf, this);
		
		super.appendFieldMetaInformationHook(contextCd, fieldDesc, appendable);
	}

	@Override
	protected void appendConstructorHook(ClassDescriptor inputClass, Appendable appendable) throws IOException
	{
		// currently C# version Metadata has only one constructor. but we may need this later.
		
//		appendable.append("\n");
//		appendable.append("\tpublic ").append(inputClass.getDescribedClassSimpleName())
//				.append("(MetaMetadataCompositeField mmd) : base(mmd) { }\n");
//		appendable.append("\n");
//		addCurrentClassDependency(MetaMetadataCompositeField.class.getPackage().getName());
	}

	@Override
	protected void appendGettersAndSettersHook(ClassDescriptor context, FieldDescriptor fieldDescriptor, Appendable appendable)
	{
		// don't need this right now.
	}
	
	@Override
	protected void appendLibraryTScopeGetter(SimplTypesScope tScope, Appendable appendable) throws IOException
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
//		appendable.append("SemanticNames.REPOSITORY_METADATA_TRANSLATIONS"); // FIXME
		appendable.append(QUOTE);
		
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
		appendable.append(CLOSING_BRACE);
		appendable.append(END_LINE);
		appendable.append(SINGLE_LINE_BREAK);
		appendable.append(DOUBLE_TAB);
		appendable.append(CLOSING_CURLY_BRACE);
		appendable.append(SINGLE_LINE_BREAK);
	}
	
}
