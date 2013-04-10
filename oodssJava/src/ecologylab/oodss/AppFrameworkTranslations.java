package ecologylab.oodss;

import simpl.core.SimplTypesScope;
import ecologylab.appframework.types.prefs.PrefsTranslationsProvider;
import ecologylab.generic.Debug;
import ecologylab.oodss.messages.DefaultServicesTranslations;

/**
 * Base translations for applications that use the ecologylab appframework and services.
 * 
 * @author andruid
 * @author andrew
 */
public class AppFrameworkTranslations extends Debug
{
	public static final String	PACKAGE_NAME	= "ecologylab.appframework.types";
	
	public static final SimplTypesScope inheritedTranslations[]	=
	{
		DefaultServicesTranslations.get(),
		PrefsTranslationsProvider.get()
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
	public static SimplTypesScope get()
	{
		return SimplTypesScope.get(PACKAGE_NAME, inheritedTranslations);
	}
}
