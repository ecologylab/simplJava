package ecologylab.appframework.types.prefs;

import simpl.core.SimplTypesScope;
import simpl.core.SimplTypesScopeFactory;
import ecologylab.generic.Debug;

/**
 * Translations used inside of MetaPrefSet.
 * 
 * @author andruid
 */
public class MetaPrefsTranslationScope
{
	public static final String NAME = "meta_prefs_translations";
	public static SimplTypesScope get()
	{
		return (SimplTypesScope) SimplTypesScopeFactory.name(NAME).translations(MetaPref.class, MetaPrefSet.class,  MetaPrefBoolean.class, MetaPrefFloat.class,
				MetaPrefInt.class, MetaPrefString.class).create();
	}

}
