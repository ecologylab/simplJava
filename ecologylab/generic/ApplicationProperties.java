package ecologylab.generic;


/**
 * General set of reusable String constants for getting properties from the environment.
 * 
 * @author andruid
 * @author blake
 */
public interface ApplicationProperties
extends ApplicationPropertyNames
{
	/**
	 * The name of the user interface currently in use, and its path in /config/interface.
	 */
	public static final String	USERINTERFACE			= Generic.parameter("userinterface");

	public static final boolean	USE_ASSETS_CACHE		= Generic.parameterBool("use_assets_cache", true);
	
}
