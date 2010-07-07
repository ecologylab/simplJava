package ecologylab.serialization.library.rest;

import ecologylab.generic.Debug;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.library.dc.Dc;

public class RESTTranslationSpace extends Debug
{
	public static final String	NAME			= "ecologylab.serialization.library.rest";
	public static final String	PACKAGE_NAME	= "ecologylab.serialization.library.rest";

	protected static final Class TRANSLATIONS[]	= 
	{ 
		RestSearchResult.class,
		SearchResults.class,
		Record.class
	};
		
	protected static final TranslationScope INHERITED[] = {Dc.get()};
	
	public static TranslationScope get()
	{
		return TranslationScope.get(PACKAGE_NAME, INHERITED, TRANSLATIONS);
	}
}
