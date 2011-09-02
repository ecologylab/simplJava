package ecologylab.oodss.distributed.common;

import ecologylab.oodss.messages.DefaultServicesTranslations;
import ecologylab.serialization.TranslationScope;

/**
 * Constants that define general ecologylab objects that get stored in the
 * Session ObjectRegistry.
 * 
 * @author andruid
 * 
 */
public interface SessionObjects
{
    public static final String           MAIN_START_AND_STOPPABLE      = "main_start_and_stoppable";

    public static final String           MAIN_SHUTDOWNABLE             = "main_shutdownable";

    public static final TranslationScope BROWSER_SERVICES_TRANSLATIONS = DefaultServicesTranslations.get();
    	/*TranslationScope
                                                                               .get(
                                                                                       "Browse",
                                                                                       "ecologylab.oodss.messages"); */

//    public static final String           BROWSER_SERVICES_CLIENT       = "browser_services_client";

    public static final String           LOGGING                       = "logging";

    public static final String           TOP_LEVEL                     = "top_level";

    public static final String           NAMED_STYLES_MAP              = "named_styles_map";
    
    public static final String			INTEREST_MODEL_SOURCE 			= "interest_model_source";
    
    public static final String					GRAPHICS_CONFIGURATION	= "graphics_configuration";
    
    public static final String				SESSIONS_MAP					= "sessions_map";

	public static final String															SESSION_HANDLE					= "SESSION_HANLDE";
	
	public static final String							APPLICATION_ENVIRONMENT				= "application_environment";
	
	public static final String SESSION_ID = "session_id";
	
	public static final String SESSIONS_MAP_BY_SESSION_ID = "sessions_map_by_session_id";
	
	public static final String OODSS_WEBSOCKET_SERVER = "oodss_websocket_server";
}
