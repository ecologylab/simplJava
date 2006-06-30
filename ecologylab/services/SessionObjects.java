package ecologylab.services;

import ecologylab.xml.TranslationSpace;

/**
 * Constants that define general ecologylab objects that get stored in the Session ObjectRegistry.
 * @author andruid
 *
 */
public interface SessionObjects
{
	public static final	String		MAIN_START_AND_STOPPABLE	= "main_start_and_stoppable";
	
	public static final TranslationSpace	BROWSER_SERVICES_TRANSLATIONS = TranslationSpace.get("Browse", "ecologylab.services.messages");

	public static final String	BROWSER_SERVICES_CLIENT	= "browser_services_client";
	
	
}
