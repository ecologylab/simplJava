/**
 * 
 */
package ecologylab.xml.types.element;

import ecologylab.generic.Debug;
import ecologylab.xml.TranslationScope;

/**
 * Translations for built-in element types.
 * 
 * @author andruid
 */
public class ElementTypeTranslationsProvider extends Debug 
{

	/**
	 * Prevent external calls. Use get().
	 */
	private ElementTypeTranslationsProvider() 
	{
	}

    /**
     * Package name
     */
	private static final String PACKAGE_NAME			= "ecologylab.xml.types.element";

    /**
     * What we should be translating to/from xml
     */
	private static final Class TRANSLATIONS[]	= 
	{	
		IntState.class,
		StringState.class,
		
	};


    /**
     * Get the translation space
     */
	public static TranslationScope get()
	{
		return TranslationScope.get(PACKAGE_NAME, TRANSLATIONS);
	}

}
