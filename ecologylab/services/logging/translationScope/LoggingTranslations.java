package ecologylab.services.logging.translationScope;

import ecologylab.services.logging.Epilogue;
import ecologylab.services.logging.LogEvent;
import ecologylab.services.logging.LogOps;
import ecologylab.services.logging.Logging;
import ecologylab.services.logging.LogueMessage;
import ecologylab.services.logging.Prologue;
import ecologylab.services.logging.SendEpilogue;
import ecologylab.services.logging.SendPrologue;
import ecologylab.xml.TranslationScope;
import ecologylab.xml.types.element.ElementTypeTranslationsProvider;

/**
 * Provide XML translation mappings for use with sensors.
 * 
 * @author Zachary O. Toups (toupsz@gmail.com)
 */
public class LoggingTranslations
{
	public static final String							LOGGING_TRANSLATION_SCOPE	= "LOGGING_TRANSLATION_SCOPE";

	private static final TranslationScope[]	OTHER_TRANSLATION_SCOPES	=
																																		{
			MixedInitiativeOpTranslationScope.get(), ElementTypeTranslationsProvider.get() };

	public static final Class								TRANSLATIONS[]						=
																																		{ Logging.class, LogOps.class,
			LogEvent.class, LogueMessage.class, Prologue.class, SendEpilogue.class, SendPrologue.class,
			Epilogue.class																								};

	/**
	 * This accessor will work from anywhere, in any order, and stay efficient.
	 * 
	 * @return TranslationSpace for basic ecologylab.services
	 */
	public static TranslationScope get()
	{
		return TranslationScope.get(LOGGING_TRANSLATION_SCOPE, OTHER_TRANSLATION_SCOPES, TRANSLATIONS);
	}
}
