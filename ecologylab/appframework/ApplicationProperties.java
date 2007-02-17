package ecologylab.appframework;

import ecologylab.appframework.types.Preference;


/**
 * General set of reusable String constants for getting properties from the environment.
 * 
 * @author andruid
 * @author blake
 */
public interface ApplicationProperties
extends ApplicationPropertyNames
{
	public static final String DEFAULT_INTERFACE  = "mistrot_interface";

	/**
	 * The name of the user interface currently in use, and its path in /config/interface.
	 */
//	public static final String	USERINTERFACE			= Generic.parameter("userinterface");
	public static final String	USERINTERFACE			= Preference.lookupString(USERINTERFACE_PREF, DEFAULT_INTERFACE);

	public static final boolean	USE_ASSETS_CACHE		= Preference.lookupBoolean("use_assets_cache", true);
	
}
