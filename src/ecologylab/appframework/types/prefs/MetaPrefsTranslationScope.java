/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.generic.Debug;
import ecologylab.platformspecifics.FundamentalPlatformSpecifics;
import ecologylab.serialization.SimplTypesScope;

/**
 * Translations used inside of MetaPrefSet.
 * 
 * @author andruid
 */
public class MetaPrefsTranslationScope extends Debug
{
	public static final String	NAME	= "meta_prefs_translations";

	public static SimplTypesScope get()
	{
		return SimplTypesScope.get(NAME, MetaPref.class, MetaPrefSet.class,  MetaPrefBoolean.class, MetaPrefFloat.class,
				MetaPrefInt.class, MetaPrefString.class);
	}

}
