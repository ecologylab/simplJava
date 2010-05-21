package ecologylab.services.logging.translationScope;

import ecologylab.services.logging.MixedInitiativeOp;
import ecologylab.xml.TranslationScope;

/**
 * Provide XML translation mappings for use with sensors.
 * 
 * @author Zachary O. Toups (toupsz@gmail.com)
 */
public class MixedInitiativeOpTranslationScope
{
	public static final String	MIXED_INITIATIVE_OP_TRANSLATION_SCOPE	= "MIXED_INITIATIVE_OP_TRANSLATION_SCOPE";

	public static final Class		TRANSLATIONS[]												=
																																		{ MixedInitiativeOp.class };

	/**
	 * This accessor will work from anywhere, in any order, and stay efficient.
	 * 
	 * @return TranslationSpace for basic ecologylab.services
	 */
	public static TranslationScope get()
	{
		return TranslationScope.get(MIXED_INITIATIVE_OP_TRANSLATION_SCOPE, TRANSLATIONS);
	}
}
