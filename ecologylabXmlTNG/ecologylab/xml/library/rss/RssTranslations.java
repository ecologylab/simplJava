package ecologylab.xml.library.rss;

import ecologylab.generic.Debug;
import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;
import ecologylab.xml.NameSpaceDecl;
import ecologylab.xml.TranslationScope;
import ecologylab.xml.XMLTranslationException;
import ecologylab.xml.library.dc.Dc;
import ecologylab.xml.library.feedburner.Feedburner;
import ecologylab.xml.library.media.Media;

/**
 * Translations for all RSS parsing.
 *
 * @author andruid
 */
public class RssTranslations
extends Debug
{
   private static final String TRANSLATION_SPACE_NAME	= "rss";
   
   public static final Class TRANSLATIONS[]	= 
   {
	   RssState.class,
	   Channel.class,
	   Item.class,
	   
	   Dc.class,

	   
	   RDFState.class,
   };
	   
   public static final TranslationScope INHERITED_TRANSLATIONS[]	= 
   {
	   Media.getTranslations(),
	   
   };
   
   public static final NameSpaceDecl[] NAME_SPACE_DECLS				=
   {
	   new NameSpaceDecl("http://search.yahoo.com/mrss/", Media.class, Media.getTranslations()),
	   new NameSpaceDecl("http://rssnamespace.org/feedburner/ext/1.0", Feedburner.class, Feedburner.get()),
	   new NameSpaceDecl("http://purl.org/dc/elements/1.1/", Dc.class, Dc.get()),
	   
   };
   
   /**
    * Just prevent anyone from new'ing this.
    */
   private RssTranslations()
   {
   }
   
   public static TranslationScope get()
   {
	   return TranslationScope.get(TRANSLATION_SPACE_NAME, TRANSLATIONS, INHERITED_TRANSLATIONS, null, NAME_SPACE_DECLS);
   }
   
	
	static final ParsedURL FLICKR_PURL	= ParsedURL.getAbsolute("http://www.flickr.com/services/feeds/photos_public.gne?format=rss_200&tags=freedom", "");

	public static void main(String[] args)
	{
		try
		{
			RssState rssState	= (RssState) ElementState.translateFromXML(FLICKR_PURL, get());
			
			rssState.translateToXML(System.out);
		} catch (XMLTranslationException e)
		{
			e.printStackTrace();
		}
	}
  
}
