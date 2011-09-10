package ecologylab.serialization.library.rss;

import java.io.File;
import java.util.ArrayList;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.Format;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.StringFormat;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_nowrap;
import ecologylab.serialization.annotations.simpl_tag;
import ecologylab.serialization.library.dc.Dc;

/**
 * Alternative root element
 * {@link ecologylab.serialization.ElementState ElementState} declarations for RSS parser:
 * that nasty RSS versions: .90 and 1.0.
 * <p/>
 * This is a bit of a hack, in that it makes no attempt to handle general RDF, or to
 * support namespace definitions with great depth.
 * <p/>
 * Those things can be done with this framework. One of these days, an application will
 * drive someone to develop such extensions.
 *
 * @author andruid
 */
//@xml_inherit
@simpl_tag("rdf:RDF")
public class RDFState extends ElementState // ArrayListState<Item>
{
	@simpl_nowrap 
	@simpl_collection("item") ArrayList<Item>		items;
	
	public RDFState()
	{
		super();
	}
	public int size()
	{
		return (items == null) ? 0 : items.size();
	}
	
	/**
	 * Get Item elements associated with a feed.
	 * 
	 * @param i	Index of the item in this collection.
	 * @return	The ith Item object directly collected in this, or null if there are no Items.
	 */
	public Item get(int i)
	{
		return items == null ? null : items.get(i);
	}
//	protected Collection getCollection(Class thatClass)
//	{
//  		return Item.class.equals(thatClass) ?
//		   super.getCollection(thatClass) : null;
//  	}
	
	static final String DELICIOUS_EXAMPLE	=
		"<rdf:RDF\r\n" + 
		" xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\r\n" + 
		" xmlns=\"http://purl.org/rss/1.0/\"\r\n" + 
		" xmlns:content=\"http://purl.org/rss/1.0/modules/content/\"\r\n" + 
		" xmlns:taxo=\"http://purl.org/rss/1.0/modules/taxonomy/\"\r\n" + 
		" xmlns:dc=\"http://purl.org/dc/elements/1.1/\"\r\n" + 
		" xmlns:syn=\"http://purl.org/rss/1.0/modules/syndication/\"\r\n" + 
		" xmlns:admin=\"http://webns.net/mvcb/\"\r\n" + 
		">\r\n" + 
		"<description></description>\r\n" + 
		"<items>\r\n" + 
		" <rdf:Seq>\r\n" + 
		"  <rdf:li rdf:resource=\"http://mashable.com/2007/11/18/10-template-generators/\" />\r\n" + 
		"  <rdf:li rdf:resource=\"http://digital-photography-school.com/blog/how-to-shoot-in-direct-sunlight/\" />\r\n" + 
		"  <rdf:li rdf:resource=\"http://linuxgames07.blogspot.com/2007/11/top-ubuntu-linux-games.html\" />\r\n" + 
		"  <rdf:li rdf:resource=\"http://www.post-gazette.com/pg/07322/834852-298.stm\" />\r\n" + 
		"  <rdf:li rdf:resource=\"http://blogs.photopreneur.com/how-to-keep-your-job-and-be-a-part-time-photographer\" />\r\n" + 
		"  <rdf:li rdf:resource=\"http://www.linkinn.com/_Unique_Photography_Amzing_HDR_Collection-martin2106\" />\r\n" + 
		"  <rdf:li rdf:resource=\"http://www.debuntu.org/how-to-find-files-on-your-computer-with-find\" />\r\n" + 
		"  <rdf:li rdf:resource=\"http://blogoscoped.com/archive/2007-11-19-n27.html\" />\r\n" + 
		"  <rdf:li rdf:resource=\"http://www.themorningnews.org/archives/galleries/the_laptop_club/\" />\r\n" + 
		"  <rdf:li rdf:resource=\"http://ecoble.com/2007/11/18/250000-bottles-amazing-recycled-mexican-island-paradise/\" />\r\n" + 
		"  <rdf:li rdf:resource=\"http://www.shredderchess.com/play-chess-online.html\" />\r\n" + 
		"  <rdf:li rdf:resource=\"http://www.posterwhore.com/\" />\r\n" + 
		"  <rdf:li rdf:resource=\"http://voshy.com/videos/view.php?id=t6pnxn4k\" />\r\n" + 
		"  <rdf:li rdf:resource=\"http://bukowski.net/vault/display_man.php?show=poem1984-07-19-this_is_free_take_it_and_feel_better.jpg&#x26;inbook=&#x26;inmag=The%20Wormwood%20Review%20Vol.%2026%20No.%202%20Issue%20102&#x26;onpage=78\" />\r\n" + 
		"  <rdf:li rdf:resource=\"http://www.pinktentacle.com/2007/11/top-60-japanese-buzzwords-of-2007/\" />\r\n" + 
		"  <rdf:li rdf:resource=\"http://beta.seatquest.com/\" />\r\n" + 
		"  <rdf:li rdf:resource=\"http://www.sobercircle.com/index.asp?node=resources&#x26;section=articles&#x26;fileid=8\" />\r\n" + 
		"  <rdf:li rdf:resource=\"http://del.icio.us/B_RL_STAR\" />\r\n" + 
		"  <rdf:li rdf:resource=\"http://weblogs.asp.net/scottgu/archive/2007/11/19/visual-studio-2008-and-net-3-5-released.aspx\" />\r\n" + 
		"  <rdf:li rdf:resource=\"http://www.wikihow.com/Make-a-Christmas-Tree-Pop-up-Card\" />\r\n" + 
		"  <rdf:li rdf:resource=\"http://www.grokdotcom.com/2007/11/13/copywriting-101-part-2/\" />\r\n" + 
		"  <rdf:li rdf:resource=\"http://www.zappatic.net/safarimicroformats/\" />\r\n" + 
		" </rdf:Seq>\r\n" + 
		"\r\n" + 
		"</items>\r\n" + 
		"\r\n" + 
		"<item rdf:about=\"http://209.85.165.104/search?q=cache:hQMa26l1za0J:www.ftponline.com/javapro/2003_06/online/namespace_kjones_06_23_03/+namespace+declaration+java+xml&#x26;hl=en&#x26;ct=clnk&#x26;cd=4&#x26;gl=us&#x26;client=firefox-a\">\r\n" + 
		"<title>Java Pro Online - Output Namespace-Aware XML Documents</title>\r\n" + 
		"<link>http://209.85.165.104/search?q=cache:hQMa26l1za0J:www.ftponline.com/javapro/2003_06/online/namespace_kjones_06_23_03/+namespace+declaration+java+xml&#x26;hl=en&#x26;ct=clnk&#x26;cd=4&#x26;gl=us&#x26;client=firefox-a</link>\r\n" + 
		"\r\n" + 
		"<description>.createElementNS</description>\r\n" + 
		"<dc:creator>andruid</dc:creator>\r\n" + 
		"<dc:date>2007-11-19T05:56:11Z</dc:date>\r\n" + 
		"<dc:subject>ecologylab java namespace xml</dc:subject>\r\n" + 
		"<taxo:topics>\r\n" + 
		"  <rdf:Bag>\r\n" + 
		"    <rdf:li resource=\"http://del.icio.us/tag/ecologylab\" />\r\n" + 
		"    <rdf:li resource=\"http://del.icio.us/tag/xml\" />\r\n" + 
		"    <rdf:li resource=\"http://del.icio.us/tag/java\" />\r\n" + 
		"    <rdf:li resource=\"http://del.icio.us/tag/namespace\" />\r\n" + 
		"  </rdf:Bag>\r\n" + 
		"</taxo:topics>\r\n" + 
		"</item>\r\n" + 
		"\r\n" + 
		"</rdf:RDF>";
	
	static final ParsedURL DELICIOUS_POPULAR	= ParsedURL.getAbsolute("http://del.icio.us/rss/popular/");

	public static final ParsedURL DELICIOUS_FEED	= ParsedURL.getAbsolute("http://del.icio.us/rss/andruid/");

	public static final File 	outputFile			= new File("/temp/rss.xml");
	public static void main(String[] args)
	{
//		ElementState.setUseDOMForTranslateTo(true);
		try
		{
//			ParsedURL feedPURL	= CNN_TOP_FEED;
			ParsedURL feedPURL	= DELICIOUS_FEED; // DELICIOUS_POPULAR;
			println("Translating RSS feed: " + feedPURL+"\n");

			RDFState rdfState	= (RDFState) RssTranslations.get().deserialize(feedPURL, Format.XML);
//			RDFState rdfState	= (RDFState) ElementState.translateFromXMLCharSequence(DELICIOUS_EXAMPLE, RssTranslations.get());

			ArrayList<Item> items	= rdfState.items; //rssState.getChannel().getItems();
			println("items: " +  rdfState.size());
			if (items != null)
			{
				for (Item item : items)
				{
					println("description:\t" + item.description);
					//FIXME! -- how do we make sure the prefix gets propagated through this call into F2XOs?!
					Dc dc	= (Dc) item.getNestedNameSpace("dc");
					if (dc != null)
					{
						dc.serialize(System.err);
						System.err.println('\n');
					}
				}
			}
			println("\n");
			
			rdfState.serialize(System.err);
			println("\n");
			rdfState.serialize(System.err);
			
//			RssState rssState2	= (RssState) ElementState.translateFromXMLCharSequence(retranslated, RssTranslations.get());
//			rssState2.translateToXML(System.out);

//			rssState.translateToXML(outputFile);
			
			println("\n");
		} catch (SIMPLTranslationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
