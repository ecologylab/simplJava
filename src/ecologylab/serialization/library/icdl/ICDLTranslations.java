/**
 * 
 */
package ecologylab.serialization.library.icdl;

import ecologylab.generic.Debug;
import ecologylab.serialization.TranslationScope;

/**
 * Translations for parsing International Childrens Digital Library stuff.
 * 
 * @author andruid
 */
public class ICDLTranslations extends Debug
{
	   private static final String TRANSLATION_SPACE_NAME	= "icdl";
	   private static final String PACKAGE_NAME				= "ecologylab.serialization.library.icdl";
	   
	   public static final Class TRANSLATIONS[]	= 
	   {
		   Response.class,
		   Book.class,
	   };
		   
	   /**
	    * Just prevent anyone from new'ing this.
	    */
	   private ICDLTranslations()
	   {
	   }
	   
	   public static TranslationScope get()
	   {
		   return TranslationScope.get(TRANSLATION_SPACE_NAME, TRANSLATIONS);
	   }
}
