package ecologylab.appframework.types;

import ecologylab.appframework.types.prefs.PrefSet;
import ecologylab.appframework.types.prefs.PrefTranslations;
import ecologylab.generic.Debug;
import ecologylab.services.messages.DefaultServicesTranslations;
import ecologylab.xml.TranslationScope;

/**
 * Base translations for applications that use the ecologylab appframework and services.
 * 
 * @author andruid
 * @author andrew
 */
public class AppFrameworkTranslations extends Debug
{
	public static final String	PACKAGE_NAME	= "ecologylab.appframework.types";
	
	public static final TranslationScope inheritedTranslations[]	=
	{
		DefaultServicesTranslations.get(),
		PrefTranslations.get(),
	};
	

	
	/**
	 * Do not use this accessor.
	 */
	private AppFrameworkTranslations()
	{
	}
	/**
	 * This accessor will work from anywhere, in any order, and stay efficient.
	 * @return
	 */
	public static TranslationScope get()
	{
		TranslationScope translationSpace = TranslationScope.get(PACKAGE_NAME, inheritedTranslations);
		
		// add the prefs translations to the scopes map
		TranslationScope.get(PrefSet.PREFS_TRANSLATION_SCOPE, inheritedTranslations);
		
		Debug.println("added "+PrefSet.PREFS_TRANSLATION_SCOPE+" to the scopes map");

		return translationSpace;
	}
}
