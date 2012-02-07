/**
 * 
 */
package ecologylab.appframework.types.prefs;

import java.util.ArrayList;
import java.util.Collection;

import ecologylab.generic.Debug;
import ecologylab.platformspecifics.FundamentalPlatformSpecifics;
import ecologylab.serialization.SimplTypesScope;

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
		return SimplTypesScope.get(SCOPE_NAME, inheritedScope, TRANSLATIONS, FundamentalPlatformSpecifics.get().addtionalPrefOpTranslations());
	}

}
