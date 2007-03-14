package ecologylab.appframework.types;

import ecologylab.appframework.types.prefs.PrefTranslations;
import ecologylab.generic.Debug;
import ecologylab.services.messages.DefaultServicesTranslations;
import ecologylab.xml.TranslationSpace;

/**
 * Base translations for applications that use the ecologylab appframework and services.
 * 
 * @author andruid
 * @author andrew
 */
public class AppFrameworkTranslations extends Debug
{
	public static final String	PACKAGE_NAME	= "ecologylab.appframework.types";
	
	public static final TranslationSpace inheritedTranslations[]	=
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
	public static TranslationSpace get()
	{
		TranslationSpace translationSpace = TranslationSpace.get(PACKAGE_NAME, inheritedTranslations);

		return translationSpace;
	}
}
