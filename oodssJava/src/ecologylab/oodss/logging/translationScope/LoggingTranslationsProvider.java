package ecologylab.oodss.logging.translationScope;

import simpl.types.element.ElementTypeTranslationsProvider;
import ecologylab.oodss.logging.Epilogue;
import ecologylab.oodss.logging.LogEvent;
import ecologylab.oodss.logging.LogOps;
import ecologylab.oodss.logging.Logging;
import ecologylab.oodss.logging.LogueMessage;
import ecologylab.oodss.logging.Prologue;
import ecologylab.oodss.logging.SendEpilogue;
import ecologylab.oodss.logging.SendPrologue;
import ecologylab.serialization.SimplTypesScope;

/**
 * Provide XML translation for logging basics.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public class LoggingTranslationsProvider
{
	public static final String							LOGGING_TRANSLATION_SCOPE	= "LOGGING_TRANSLATION_SCOPE";

	private static final SimplTypesScope[]	OTHER_TRANSLATION_SCOPES	=
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
	public static SimplTypesScope get()
	{
		return SimplTypesScope.get(LOGGING_TRANSLATION_SCOPE, OTHER_TRANSLATION_SCOPES, TRANSLATIONS,
				MixedInitiativeOpClassesProvider.STATIC_INSTANCE.provideClasses());
	}
}
