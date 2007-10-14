package ecologylab.xml.library.rest.nsdl;

import ecologylab.xml.TranslationSpace;
import ecologylab.xml.library.rest.RESTTranslationSpace;
import ecologylab.xml.library.rest.SearchResults;

public class NSDLTranslationSpace extends TranslationSpace
{
	public static final String	NAME					= "ecologylab.xml.library.rest.nsdl";
	public static final String	PACKAGE_NAME			= "ecologylab.xml.library.rest.nsdl";
	
	private static 		TranslationSpace translationSpace = null;

	
	protected static final Class	TRANSLATIONS[]		= 
	{ 
		NSDLSearchService.class,
		SearchResults.class
	};
	
	protected static final TranslationSpace BASE_SPACE[]= {new RESTTranslationSpace()};
	
	public NSDLTranslationSpace()
	{
		super(NAME, PACKAGE_NAME, BASE_SPACE, TRANSLATIONS);
	}
	
	public static TranslationSpace get()
	{
		if (translationSpace == null)
			translationSpace = new NSDLTranslationSpace();
		
		return translationSpace;
	}
}
