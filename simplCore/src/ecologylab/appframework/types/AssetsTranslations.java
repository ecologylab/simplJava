/**
 * 
 */
package ecologylab.appframework.types;

import simpl.core.SimplTypesScope;
import simpl.core.SimplTypesScopeFactory;
import ecologylab.generic.Debug;

/**
 * Simple translations used just for loading the assets.xml file.
 * 
 * @author robinson
 * @author andruid
 */
public class AssetsTranslations extends Debug 
{
	public static final String	PACKAGE_NAME	= "ecologylab.assets.types";
	
	public static final Class<?>	TRANSLATIONS[]	= 
	{
		AssetsState.class,
		AssetState.class,
		
	};
	
	/**
	 * Get existing TranslationSpace with this name, or create a new one, and map it.
	 * 
	 * @return	TranslationSpace with simple translations used just for loading the assets.xml file.
	 */
	public static SimplTypesScope get()
	{
		return SimplTypesScopeFactory.name(PACKAGE_NAME).translations(TRANSLATIONS).create();
	}
}
