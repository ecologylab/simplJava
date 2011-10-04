package ecologylab.oodss.messages;

import ecologylab.oodss.logging.Epilogue;
import ecologylab.oodss.logging.LogOps;
import ecologylab.oodss.logging.Prologue;
import ecologylab.oodss.logging.SendEpilogue;
import ecologylab.oodss.logging.SendPrologue;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.types.element.ElementTypeTranslationsProvider;

/**
 * Provide XML translation mappings for use in processing CF services requests.
 * 
 * @author andruid
 */
public class DefaultServicesTranslations
{
	public static final String	PACKAGE_NAME		= "ecologylab.oodss.messages";

	public static final Class		TRANSLATIONS[]	=
																							{ RequestMessage.class, ResponseMessage.class,
			CloseMessage.class,

			OkResponse.class, BadSemanticContentResponse.class, ErrorResponse.class,

			Prologue.class, Epilogue.class, LogOps.class, SendEpilogue.class, SendPrologue.class,

			HttpRequest.class, HttpGetRequest.class, PingRequest.class, Ping.class, Pong.class,
			UrlMessage.class,

			CfCollaborationGetSurrogate.class, ContinuedHTTPGetRequest.class, IgnoreRequest.class,

			InitConnectionRequest.class, InitConnectionResponse.class, DisconnectRequest.class,

			ServiceMessage.class, UrlMessage.class, UpdateMessage.class };

	/**
	 * This accessor will work from anywhere, in any order, and stay efficient.
	 * 
	 * @return TranslationSpace for basic ecologylab.oodss
	 */
	public static TranslationScope get()
	{
		return TranslationScope.get(PACKAGE_NAME, ElementTypeTranslationsProvider.get(), TRANSLATIONS);
	}
}
