package ecologylab.oodss.logging.translationScope;

import ecologylab.oodss.logging.Epilogue;
import ecologylab.oodss.logging.LogEvent;
import ecologylab.oodss.logging.LogOps;
import ecologylab.oodss.logging.Logging;
import ecologylab.oodss.logging.LogueMessage;
import ecologylab.oodss.logging.Prologue;
import ecologylab.oodss.logging.SendEpilogue;
import ecologylab.oodss.logging.SendPrologue;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.types.element.ElementTypeTranslationsProvider;

/**
 * Provide XML translation for logging basics.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public class LoggingTranslationsProvider
{
	public static final String							LOGGING_TRANSLATION_SCOPE	= "LOGGING_TRANSLATION_SCOPE";

	private static final TranslationScope[]	OTHER_TRANSLATION_SCOPES	=
																																		{ ElementTypeTranslationsProvider
																																				.get() };

	public static final Class								TRANSLATIONS[]						=
																																		{ Logging.class, LogOps.class,
			LogEvent.class, LogueMessage.class, Prologue.class, SendEpilogue.class, SendPrologue.class,
			Epilogue.class																								};

	/**
	 * This accessor will work from anywhere, in any order, and stay efficient.
	 * 
	 * @return TranslationSpace for basic ecologylab.oodss
	 */
	public static TranslationScope get()
	{
		return TranslationScope.get(LOGGING_TRANSLATION_SCOPE, OTHER_TRANSLATION_SCOPES, TRANSLATIONS,
				MixedInitiativeOpClassesProvider.STATIC_INSTANCE.provideClasses());
	}
}
