package ecologylab.xml.library.rest;

import ecologylab.generic.Debug;
import ecologylab.xml.TranslationScope;
import ecologylab.xml.library.dc.Dc;

public class RESTTranslationSpace extends Debug
{
	public static final String	NAME			= "ecologylab.xml.library.rest";
	public static final String	PACKAGE_NAME	= "ecologylab.xml.library.rest";

	protected static final Class TRANSLATIONS[]	= 
	{ 
		RestSearchResult.class,
		SearchResults.class,
		Record.class
	};
		
	protected static final TranslationScope INHERITED[] = {Dc.get()};
	
	public static TranslationScope get()
	{
		return TranslationScope.get(PACKAGE_NAME, TRANSLATIONS, INHERITED);
	}
}
