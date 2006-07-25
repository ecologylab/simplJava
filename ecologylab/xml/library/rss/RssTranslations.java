package ecologylab.xml.rss;

import ecologylab.generic.Debug;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.dc.Dc;

/**
 * Translations for RSS version 2.0 style, with no namespace: style identifiers.
 *
 * @author andruid
 */
public class RssTranslations
{
   private static final String TRANSLATION_SPACE_NAME	= "rss";
   private static final String PACKAGE_NAME				= "ecologylab.xml.rss";
   
   public static final Class TRANSLATIONS[]	= 
   {
	   RssState.class,
	   Channel.class,
	   Item.class,
	   
	   Dc.class,
	   
	   RDFState.class,
   };
	   
   /**
    * Just prevent anyone from new'ing this.
    */
   private RssTranslations()
   {
   }
   
   public static TranslationSpace get()
   {
	   return TranslationSpace.get(TRANSLATION_SPACE_NAME, PACKAGE_NAME, TRANSLATIONS);
   }
}
