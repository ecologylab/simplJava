package ecologylab.appframework;

import ecologylab.appframework.types.prefs.Pref;


/**
 * General set of reusable String constants for getting properties from the environment.
 * 
 * @author andruid
 * @author blake
 */
public interface ApplicationProperties
extends ApplicationPropertyNames
{
	public static final boolean	USE_ASSETS_CACHE		= Pref.lookupBoolean("use_assets_cache", true);
	
}
