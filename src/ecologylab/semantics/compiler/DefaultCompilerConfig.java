package ecologylab.semantics.compiler;

import java.io.File;

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
		return MetaMetadataRepository.loadXmlFromDir(new File(MetaMetadataRepositoryInit.DEFAULT_REPOSITORY_LOCATION));
	}

	@Override
	public MetaMetadataJavaTranslator createJavaTranslator()
	{
		return new MetaMetadataJavaTranslator();
	}

}
