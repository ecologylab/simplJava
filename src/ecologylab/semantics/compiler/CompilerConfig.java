package ecologylab.semantics.compiler;

import ecologylab.semantics.metametadata.MetaMetadataRepository;

public interface CompilerConfig
{

	String getGeneratedSemanticsLocation();
	
	MetaMetadataRepository loadRepository();
	
	MetaMetadataJavaTranslator createJavaTranslator();
	
}
