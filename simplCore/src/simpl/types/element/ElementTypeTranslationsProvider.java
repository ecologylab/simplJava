/**
 * 
 */
package simpl.types.element;

import simpl.core.SimplTypesScope;
import simpl.core.SimplTypesScopeFactory;
import ecologylab.generic.Debug;

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
	private static final String PACKAGE_NAME			= "ecologylab.serialization.types.element";

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
	public static SimplTypesScope get()
	{
		return SimplTypesScopeFactory.name(PACKAGE_NAME).translations(TRANSLATIONS).create();
	}

}
