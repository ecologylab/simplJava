/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.generic.Debug;
import ecologylab.services.authentication.AuthenticationList;
import ecologylab.services.authentication.AuthenticationListEntry;
import ecologylab.xml.TranslationScope;
import ecologylab.xml.types.element.ElementTypeTranslations;
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
        MetaPrefColor.class,
		
		Pref.class,
        PrefSet.class,
        PrefBoolean.class,
        PrefFloat.class,
        PrefInt.class,
        PrefLong.class,
        PrefString.class,
        PrefElementState.class,
        PrefColor.class,
        PrefFile.class,
        PrefOp.class,
        PrefDelayedOp.class,
        
        PrefAuthList.class,
        AuthenticationList.class,
        AuthenticationListEntry.class,
        
        RangeState.class,
        RangeIntState.class,
        RangeFloatState.class,
        
        
        Choice.class,
        ChoiceInt.class,
        ChoiceBoolean.class,
        ChoiceFloat.class,
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
	public static TranslationScope get()
	{
		return TranslationScope.get(PACKAGE_NAME, TRANSLATIONS, ElementTypeTranslations.get());
	}
}
