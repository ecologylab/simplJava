/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.generic.Debug;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.library.dc.Dc;
import ecologylab.xml.library.rss.Channel;
import ecologylab.xml.library.rss.Item;
import ecologylab.xml.library.rss.RDFState;
import ecologylab.xml.library.rss.RssState;
import ecologylab.services.messages.*;
/**
 * Translations for the pref/meta_pref system.
 * 
 * @author Cae
 *
 */
public class PrefTranslations extends Debug 
{
	private static final String PACKAGE_NAME			= "ecologylab.appframework.types.prefs";

	public static final Class TRANSLATIONS[]	= 
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
