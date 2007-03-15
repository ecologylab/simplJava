/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.generic.Debug;
import ecologylab.xml.TranslationSpace;
/**
 * Translations for the pref/meta_pref system.
 * 
 * @author Cae
 *
 */
public class PrefTranslations extends Debug 
{
	private static final String PACKAGE_NAME			= "ecologylab.appframework.types.prefs";

	private static final Class TRANSLATIONS[]	= 
	{
		MetaPref.class,
		MetaPrefSet.class,
		MetaPrefBoolean.class,
		MetaPrefFloat.class,
		MetaPrefInt.class,
		MetaPrefString.class,
		
		Pref.class,
        PrefSet.class,
        PrefBoolean.class,
        PrefFloat.class,
        PrefInt.class,
        PrefString.class,
        PrefElementState.class,
        
        RangeState.class,
        RangeIntState.class,
        
        Choice.class,
        ChoiceInt.class,
        ChoiceBoolean.class,
        
		
	};

	/**
	 * Just prevent anyone from new'ing this.
	 */
	private PrefTranslations()
	{
	}

	public static TranslationSpace get()
	{
		return TranslationSpace.get(PACKAGE_NAME, TRANSLATIONS);
	}

}
