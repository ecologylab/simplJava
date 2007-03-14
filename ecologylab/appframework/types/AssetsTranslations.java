/**
 * 
 */
package ecologylab.appframework.types;

import ecologylab.generic.Debug;
import ecologylab.xml.TranslationSpace;

/**
 * Simple translations used just for loading the assets.xml file.
 * 
 * @author robinson
 * @author andruid
 */
public class AssetsTranslations extends Debug 
{
	public static final String	PACKAGE_NAME	= "ecologylab.assets.types";
	
	public static final Class	TRANSLATIONS[]	= 
	{
		AssetsState.class,
		AssetState.class,
		
	};
	
	/**
	 * Get existing TranslationSpace with this name, or create a new one, and map it.
	 * 
	 * @return	TranslationSpace with simple translations used just for loading the assets.xml file.
	 */
	public static TranslationSpace get()
	{
		return TranslationSpace.get(PACKAGE_NAME, TRANSLATIONS);
	}
}
