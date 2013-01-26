/**
 * 
 */
package ecologylab.appframework.types.prefs;

import simpl.core.SimplTypesScope;
import ecologylab.generic.Debug;

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
