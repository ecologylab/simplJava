/**
 * 
 */
package ecologylab.appframework.types.prefs;

import simpl.core.SimplTypesScope;
import simpl.core.SimplTypesScopeFactory;
import simpl.platformspecifics.SimplPlatformSpecifics;
import ecologylab.generic.Debug;

/**
 * @author andrew
 *
 */
public class PrefOpTranslations extends Debug
{
	public static final String 	SCOPE_NAME 		= "pref_op_translations";
	
	public static final Class[] TRANSLATIONS 	= 
	{
		PrefOp.class,
	};
	
	/**
	 * Do not use this accessor.
	 */
	private PrefOpTranslations()
	{
	}
	
	public static SimplTypesScope get(SimplTypesScope inheritedScope)
	{
		return SimplTypesScopeFactory.name(SCOPE_NAME).inherits(inheritedScope).translations(TRANSLATIONS, SimplPlatformSpecifics.get().addtionalPrefOpTranslations()).create();
	}

}
