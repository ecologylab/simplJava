/**
 * 
 */
package ecologylab.appframework.types;

import ecologylab.generic.Debug;
import ecologylab.xml.TranslationSpace;

/**
 * @author robinson
 *
 */
public class AssetsTranslations extends Debug 
{
	public static final String	NAME			= "ecologylab.assets.types";
	
	public static final String	PACKAGE_NAME	= "ecologylab.assets.types";
	
	public static final Class	TRANSLATIONS[]	= 
	{
		AssetsState.class,
		AssetState.class,
		
	};
	
	
	public static TranslationSpace get()
	{
		return TranslationSpace.get(NAME, PACKAGE_NAME, TRANSLATIONS);
	}

}
