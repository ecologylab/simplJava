package ecologylab.semantics.compiler;

import ecologylab.io.Files;
import ecologylab.semantics.collecting.MetaMetadataRepositoryInit;
import ecologylab.semantics.metametadata.MetaMetadataRepository;

public class DefaultCompilerConfig implements CompilerConfig
{

	private static final String	DEFAULT_GENERATED_SEMANTICS_LOCATION	= ".." + Files.sep + "ecologylabGeneratedSemantics";
	
	@Override
	public String getGeneratedSemanticsLocation()
	{
		return DEFAULT_GENERATED_SEMANTICS_LOCATION;
	}

	@Override
	public MetaMetadataRepository loadRepository()
	{
		return MetaMetadataRepositoryInit.getRepository();
	}

	@Override
	public MetaMetadataJavaTranslator createJavaTranslator()
	{
		return new MetaMetadataJavaTranslator();
	}

}
