package ecologylab.services.messages;

import ecologylab.appframework.types.prefs.Pref;
import ecologylab.appframework.types.prefs.PrefSet;
import ecologylab.generic.Debug;
import ecologylab.services.logging.Epilogue;
import ecologylab.services.logging.LogOps;
import ecologylab.services.logging.Prologue;
import ecologylab.services.logging.SendEpilogue;
import ecologylab.services.logging.SendPrologue;
import ecologylab.xml.TranslationSpace;

/**
 * Provide XML translation mappings for use in processing CF services requests.
 * 
 * @author andruid
 */
public class DefaultServicesTranslations extends Debug
{
	public static final String	NAME			= "ecologylab.services";
	
	public static final String	PACKAGE_NAME	= "ecologylab.services.messages";

	public static final Class	TRANSLATIONS[]	= 
	{ 
		RequestMessage.class,
		ResponseMessage.class,
		CloseMessage.class,
		
		StopMessage.class,
		OkResponse.class,
		BadSemanticContentResponse.class,
		ErrorResponse.class,
		Prologue.class,
		Epilogue.class,
		LogOps.class,
		SendEpilogue.class,
		SendPrologue.class,
		
		HttpGetRequest.class,
		PingRequest.class,
		UrlMessage.class,
		
        //TODO -- get rid of these!
		SetPreferences.class,
        PrefSet.class,
        Pref.class
	};
	
	/**
	 * Do not use this accessor.
	 *
	 */
	protected DefaultServicesTranslations()
	{
	}
	/**
	 * This accessor will work from anywhere, in any order, and stay efficient.
	 * @return	TranslationSpace for basic ecologylab.services
	 */
	public static TranslationSpace get()
	{
		return TranslationSpace.get(NAME, PACKAGE_NAME, TRANSLATIONS);
	}
}
