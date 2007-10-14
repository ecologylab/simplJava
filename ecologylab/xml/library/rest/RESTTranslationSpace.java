package ecologylab.xml.library.rest;

import ecologylab.xml.TranslationSpace;
import ecologylab.xml.library.dc.Dc;

public class RESTTranslationSpace extends TranslationSpace
{
	public static final String	NAME			= "ecologylab.xml.library.rest";
	public static final String	PACKAGE_NAME	= "ecologylab.xml.library.rest";

	protected static final Class TRANSLATIONS[]	= 
	{ 
		RestSearchResult.class,
		SearchResults.class,
		Record.class
	};
	
	private static TranslationSpace translationSpace = null;
	
	protected static final TranslationSpace BASE_SPACE[] = {Dc.get()};
	
	public RESTTranslationSpace()
	{
		super(NAME, PACKAGE_NAME, BASE_SPACE, TRANSLATIONS);
	}
	protected RESTTranslationSpace(String name, String defaultPackgeName, TranslationSpace[] inheritedTranslationsSet,
			   Class[] translations)
	{
		super(name, defaultPackgeName, inheritedTranslationsSet, translations);
	}
	
	public static TranslationSpace get()
	{
		if (translationSpace == null)
			translationSpace = new RESTTranslationSpace();
		
		return translationSpace;
	}
}
