package ecologylab.services.logging;

import ecologylab.xml.TranslationScope;
import ecologylab.xml.types.element.ElementTypeTranslations;

/**
 * Provide XML translation mappings for use with sensors.
 * 
 * @author Zachary O. Toups (toupsz@gmail.com)
 */
public class LoggingTranslations
{
	public static final String	PACKAGE_NAME	= "ecologylab.services.logging";

	public static final Class	TRANSLATIONS[]	=
															{ Logging.class, LogOps.class,
			LogEvent.class, LogueMessage.class, Prologue.class,
			SendEpilogue.class, SendPrologue.class, MixedInitiativeOp.class,
			Epilogue.class							};

	/**
	 * This accessor will work from anywhere, in any order, and stay efficient.
	 * 
	 * @return TranslationSpace for basic ecologylab.services
	 */
	public static TranslationScope get()
	{
		return TranslationScope.get(PACKAGE_NAME, TRANSLATIONS,
				ElementTypeTranslations.get());
	}
}
