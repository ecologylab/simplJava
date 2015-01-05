package ecologylab.serialization.library.rest.nsdl;

import ecologylab.generic.Debug;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.library.rest.RESTTranslationSpace;
import ecologylab.serialization.library.rest.SearchResults;

public class NSDLTranslationSpace extends Debug
{
	public static final String	PACKAGE_NAME			= "ecologylab.serialization.library.rest.nsdl";
	
	protected static final Class	TRANSLATIONS[]		= 
	{ 
		NSDLSearchService.class,
		SearchResults.class
	};
	
	protected static final SimplTypesScope INHERITED[]= {RESTTranslationSpace.get()};
	
	public static SimplTypesScope get()
	{
		return SimplTypesScope.get(PACKAGE_NAME, INHERITED, TRANSLATIONS);
	}
}
