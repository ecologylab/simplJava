/**
 * 
 */
package ecologylab.xml.library.icdl;

import ecologylab.generic.Debug;
import ecologylab.xml.TranslationSpace;

/**
 * Translations for parsing International Childrens Digital Library stuff.
 * 
 * @author andruid
 */
public class ICDLTranslations extends Debug
{
	   private static final String TRANSLATION_SPACE_NAME	= "icdl";
	   private static final String PACKAGE_NAME				= "ecologylab.xml.library.icdl";
	   
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
	   
	   public static TranslationSpace get()
	   {
		   return TranslationSpace.get(TRANSLATION_SPACE_NAME, TRANSLATIONS, PACKAGE_NAME);
	   }
}
