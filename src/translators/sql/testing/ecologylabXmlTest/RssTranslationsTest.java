package translators.sql.testing.ecologylabXmlTest;

import ecologylab.generic.Debug;
import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationScope;
import ecologylab.xml.SIMPLTranslationException;

/**
 * Translations for all RSS parsing.
 *
 * @author andruid
 */
public class RssTranslationsTest
extends Debug
{
   private static final String TRANSLATION_SPACE_NAME	= "rss";
   
   public static final Class TRANSLATIONS[]	= 
   {
	   RssStateTest.class,
	   ChannelTest.class,
	   ItemTest.class,
	   
//	   Dc.class,
//
//	   
//	   RDFState.class,
   };
	   /*
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
   */
   /**
    * Just prevent anyone from new'ing this.
    */
   private RssTranslationsTest()
   {
   }
   
   public static TranslationScope get()
   {
	   return TranslationScope.get(TRANSLATION_SPACE_NAME, /* NAME_SPACE_DECLS,INHERITED_TRANSLATIONS, */
	  		 TRANSLATIONS);
   }
   
   public static final String OUT	= "<translation_scope name=\"rss\"><entries_by_tag><class_descriptor described_class=\"ecologylab.xml.library.rss.Item\" tag_name=\"item\" decribed_class_simple_name=\"Item\" described_class_package_name=\"ecologylab.xml.library.rss\"><field_descriptor field=\"title\" tag_name=\"title\" type=\"2\" scalar_type=\"StringType\" needs_escaping=\"true\"></field_descriptor><field_descriptor field=\"description\" tag_name=\"description\" type=\"2\" scalar_type=\"StringType\" needs_escaping=\"true\"></field_descriptor><field_descriptor field=\"link\" tag_name=\"link\" type=\"2\" scalar_type=\"ParsedURLType\" needs_escaping=\"true\"></field_descriptor><field_descriptor field=\"guid\" tag_name=\"guid\" type=\"2\" scalar_type=\"ParsedURLType\" needs_escaping=\"true\"></field_descriptor><field_descriptor field=\"author\" tag_name=\"author\" type=\"2\" scalar_type=\"StringType\" needs_escaping=\"true\"></field_descriptor><field_descriptor field=\"categorySet\" tag_name=\"category_set\" type=\"5\" scalar_type=\"StringType\" needs_escaping=\"true\"></field_descriptor></class_descriptor><class_descriptor described_class=\"ecologylab.xml.library.rss.RssState\" tag_name=\"rss\" decribed_class_simple_name=\"RssState\" described_class_package_name=\"ecologylab.xml.library.rss\"><field_descriptor field=\"version\" tag_name=\"version\" type=\"1\" scalar_type=\"FloatType\"></field_descriptor><field_descriptor field=\"channel\" tag_name=\"channel\" type=\"3\"></field_descriptor></class_descriptor><class_descriptor described_class=\"ecologylab.xml.library.rss.Channel\" tag_name=\"channel\" decribed_class_simple_name=\"Channel\" described_class_package_name=\"ecologylab.xml.library.rss\"><field_descriptor field=\"title\" tag_name=\"title\" type=\"2\" scalar_type=\"StringType\" needs_escaping=\"true\"></field_descriptor><field_descriptor field=\"description\" tag_name=\"description\" type=\"2\" scalar_type=\"StringType\" needs_escaping=\"true\"></field_descriptor><field_descriptor field=\"link\" tag_name=\"link\" type=\"2\" scalar_type=\"ParsedURLType\" needs_escaping=\"true\"></field_descriptor><field_descriptor field=\"items\" tag_name=\"items\" type=\"4\"></field_descriptor></class_descriptor></entries_by_tag></translation_scope>";
	
	static final ParsedURL FLICKR_PURL	= ParsedURL.getAbsolute("http://www.flickr.com/services/feeds/photos_public.gne?format=rss_200&tags=freedom", "");

	public static void main(String[] args)
	{
		TranslationScope tScope	= get();
		
		try
		{
			StringBuilder buffy	= new StringBuilder();
			tScope.serialize(buffy);
			System.out.println('\n');
			
			ElementState translated	= TranslationScope.getBasicTranslations().deserializeCharSequence(buffy);
				//ElementState.translateFromXMLCharSequence(OUT, TranslationScope.getBasicTranslations());
			
			translated.serialize(System.out);
			System.out.println('\n');
		}
		catch (SIMPLTranslationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("\n");
	}
  
}
