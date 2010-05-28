/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.generic.Debug;
import ecologylab.xml.TranslationScope;

/**
 * Translations used inside of MetaPrefSet.
 * 
 * @author andruid
 */
public class MetaPrefsTranslationScope extends Debug
{
	public static final String	NAME	= "meta_prefs_translations";

	public static TranslationScope get()
	{
		return TranslationScope.get(NAME, MetaPref.class, MetaPrefBoolean.class, MetaPrefFloat.class,
				MetaPrefInt.class, MetaPrefString.class, MetaPrefColor.class, 
				/* this must be last or not here at all!!! */ MetaPrefSet.class);
	}

}
