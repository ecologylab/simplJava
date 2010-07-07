/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.generic.Debug;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.types.element.ElementTypeTranslationsProvider;

/**
 * Translations for the pref/meta_pref system.
 * 
 * @author Cae
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public class PrefsTranslationsProvider extends Debug
{
	/**
	 * Package name
	 */
	private static final String										PREFS_TRANSLATIONS_NAME	= "PREFS_TRANSLATIONS_NAME";

	/**
	 * Additional classes needed to do Prefs translations. Most are provided by
	 * PrefSetBaseClassProvider.
	 */
	private static final Class[]									translations						=
																																				{ RangeState.class,
			RangeIntState.class, RangeFloatState.class,

			Choice.class, ChoiceInt.class, ChoiceBoolean.class, ChoiceFloat.class };

	public static final PrefsTranslationsProvider	STATIC_INSTANCE					= new PrefsTranslationsProvider();

	private PrefsTranslationsProvider()
	{
	}

	/**
	 * Get the translation space
	 */
	public static TranslationScope get()
	{
		return TranslationScope.get(PREFS_TRANSLATIONS_NAME, ElementTypeTranslationsProvider.get(),
				translations, PrefSetBaseClassProvider.STATIC_INSTANCE.provideClasses());
	}
}
