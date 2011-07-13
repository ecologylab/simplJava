package ecologylab.translators.metametadata.compiler;

import ecologylab.io.Files;

public class ORMCompilerConfig implements CompilerConfig
{

	private static final String	ORM_GENERATED_SEMANTICS_LOCATION	= ".." + Files.sep + "ecologylabGeneratedSemanticsORM" + Files.sep + "src";
	
	@Override
	public String getGeneratedSemanticsLocation()
	{
		return ORM_GENERATED_SEMANTICS_LOCATION;
	}

}
