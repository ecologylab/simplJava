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

/**
 * Translations for the pref/meta_pref system.
 * 
 * @author Cae
 *
 */
public class PrefTranslations extends Debug 
{

	private static final String TRANSLATION_SPACE_NAME	= "prefs";
	private static final String PACKAGE_NAME			= "ecologylab.appframework.types.prefs";

	public static final Class TRANSLATIONS[]	= 
	{
		MetaPref.class,
		MetaPrefSet.class,
		Pref.class,
        PrefSet.class,
		
	};

	/**
	 * Just prevent anyone from new'ing this.
	 */
	private PrefTranslations()
	{
	}

	public static TranslationSpace get()
	{
		return TranslationSpace.get(TRANSLATION_SPACE_NAME, PACKAGE_NAME, TRANSLATIONS);
	}

}
