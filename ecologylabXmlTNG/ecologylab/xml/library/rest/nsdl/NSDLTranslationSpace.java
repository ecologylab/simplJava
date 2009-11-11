package ecologylab.xml.library.rest.nsdl;

import ecologylab.generic.Debug;
import ecologylab.xml.TranslationScope;
import ecologylab.xml.library.rest.RESTTranslationSpace;
import ecologylab.xml.library.rest.SearchResults;

public class NSDLTranslationSpace extends Debug
{
	public static final String	PACKAGE_NAME			= "ecologylab.xml.library.rest.nsdl";
	
	protected static final Class	TRANSLATIONS[]		= 
	{ 
		NSDLSearchService.class,
		SearchResults.class
	};
	
	protected static final TranslationScope INHERITED[]= {RESTTranslationSpace.get()};
	
	public static TranslationScope get()
	{
		return TranslationScope.get(PACKAGE_NAME, TRANSLATIONS, INHERITED);
	}
}
