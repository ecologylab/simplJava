package ecologylab.translators.metametadata.compiler;

import ecologylab.io.Files;

public class DefaultCompilerConfig implements CompilerConfig
{

	private static final String	DEFAULT_GENERATED_SEMANTICS_LOCATION	= ".." + Files.sep + "ecologylabGeneratedSemantics";
	
	@Override
	public String getGeneratedSemanticsLocation()
	{
		return DEFAULT_GENERATED_SEMANTICS_LOCATION;
	}

}
