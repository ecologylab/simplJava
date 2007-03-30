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
    /**
     * Package name
     */
	private static final String PACKAGE_NAME			= "ecologylab.appframework.types.prefs";

    /**
     * What we should be translating to/from xml
     */
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

    /**
     * Get the translation space
     */
	public static TranslationSpace get()
	{
		return TranslationSpace.get(PACKAGE_NAME, TRANSLATIONS);
	}

}
