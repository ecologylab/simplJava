package ecologylab.xml.library.rss;

import ecologylab.generic.Debug;
import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XmlTranslationException;
import ecologylab.xml.library.dc.Dc;

/**
 * Translations for all RSS parsing.
 *
 * @author andruid
 */
public class RssTranslations
extends Debug
{
   private static final String TRANSLATION_SPACE_NAME	= "rss";
   private static final String PACKAGE_NAME				= "ecologylab.xml.library.rss";
   
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
   
	
	static final ParsedURL FLICKR_PURL	= ParsedURL.getAbsolute("http://www.flickr.com/services/feeds/photos_public.gne?format=rss_200&tags=freedom", "");

	public static void main(String[] args)
	{
		try
		{
			RssState rssState	= (RssState) ElementState.translateFromXML(FLICKR_PURL, get());
			
			String xml			= rssState.translateToXML(false);
			
			println(xml);
			
		} catch (XmlTranslationException e)
		{
			e.printStackTrace();
		}
	}
  
}
