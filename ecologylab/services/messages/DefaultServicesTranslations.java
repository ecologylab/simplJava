package ecologylab.services.messages;

import ecologylab.services.logging.Epilogue;
import ecologylab.services.logging.LogOps;
import ecologylab.services.logging.Prologue;
import ecologylab.services.logging.SendEpilogue;
import ecologylab.services.logging.SendPrologue;
import ecologylab.xml.TranslationScope;
import ecologylab.xml.types.element.ElementTypeTranslations;

/**
 * Provide XML translation mappings for use in processing CF services requests.
 * 
 * @author andruid
 */
public class DefaultServicesTranslations
{
	public static final String	PACKAGE_NAME	= "ecologylab.services.messages";

	public static final Class	TRANSLATIONS[]	=
															{ RequestMessage.class, ResponseMessage.class, CloseMessage.class,

															StopMessage.class, OkResponse.class, BadSemanticContentResponse.class,
			ErrorResponse.class, 
			
			Prologue.class, Epilogue.class, LogOps.class, SendEpilogue.class, SendPrologue.class,

			HttpRequest.class, HttpGetRequest.class, PingRequest.class, Ping.class, Pong.class, UrlMessage.class,

			CfCollaborationGetSurrogate.class, ContinuedHTTPGetRequest.class, IgnoreRequest.class,
			
			InitConnectionRequest.class, InitConnectionResponse.class, DisconnectRequest.class,			
			
			ServiceMessage.class, StopMessage.class,
			UrlMessage.class, UpdateMessage.class};

	/**
	 * This accessor will work from anywhere, in any order, and stay efficient.
	 * 
	 * @return TranslationSpace for basic ecologylab.services
	 */
	public static TranslationScope get()
	{
		return TranslationScope.get(PACKAGE_NAME, ElementTypeTranslations.get(), TRANSLATIONS);
	}
}
