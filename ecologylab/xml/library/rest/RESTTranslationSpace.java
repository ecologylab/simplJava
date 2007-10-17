package ecologylab.xml.library.rest;

import ecologylab.generic.Debug;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.library.dc.Dc;
import ecologylab.xml.library.rest.nsdl.NSDLTranslationSpace;

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
		
	protected static final TranslationSpace BASE_SPACE[] = {Dc.get()};
	
	public static TranslationSpace get()
	{
		return TranslationSpace.get(PACKAGE_NAME, BASE_SPACE, TRANSLATIONS);
	}
}
