package ecologylab.xml.library.rss;

import java.util.ArrayList;

import java.io.File;
import java.lang.reflect.Field;

import ecologylab.net.ParsedURL;
import ecologylab.xml.*;
import ecologylab.xml.library.feedburner.Feedburner;
import ecologylab.xml.library.media.Media;
import ecologylab.xml.library.media.Thumbnail;

/**
 * {@link ecologylab.xml.ElementState ElementState} for the root element of the RSS parser.
 * In particular, this supports RSS versions such as .91, .92, .93, .94, and 2.0.
 *
 * @author andruid
 */
public class RssState extends ElementState
{
	@xml_attribute	float		version;
   
	public @xml_nested		Channel		channel;

	/**
	 * @return Returns the channel.
	 */
	public Channel getChannel()
	{
		return channel;
	}

	/**
	 * @param channel The channel to set.
	 */
	public void setChannel(Channel channel)
	{
		this.channel = channel;
	}

	/**
	 * @return Returns the version.
	 */
	public float getVersion()
	{
		return version;
	}

	/**
	 * @param version The version to set.
	 */
	public void setVersion(float version)
	{
		this.version = version;
	}
	
	public static final ParsedURL NYT_TECH_FEED	= ParsedURL.getAbsolute("http://www.nytimes.com/services/xml/rss/nyt/Technology.xml");
	public static final ParsedURL CNN_TOP_FEED	= ParsedURL.getAbsolute("http://rss.cnn.com/rss/cnn_topstories.rss");
	
	public static final ParsedURL BBC_FRONT_FEED	= ParsedURL.getAbsolute("http://news.bbc.co.uk/rss/newsonline_world_edition/front_page/rss.xml");
	
	public static final ParsedURL ABC_SPORTS_FEED	= ParsedURL.getAbsolute("http://my.abcnews.go.com/rsspublic/sports_rss20.xml");
	
	public static final ParsedURL FLICKR_FEED	= ParsedURL.getAbsolute("http://www.flickr.com/services/feeds/photos_public.gne?format=rss_200&tags=sunset");
	
	public static final ParsedURL DELICIOUS_FEED	= ParsedURL.getAbsolute("http://del.icio.us/rss/andruid/");

	public static final String ABC_EXAMPLE		=
		"<rss xmlns:media=\"http://search.yahoo.com/mrss/\" xmlns:feedburner=\"http://rssnamespace.org/feedburner/ext/1.0\" version=\"2.0\">\r\n" + 
		"<channel> \r\n" + 
		"\r\n" + 
		"<item>\r\n" + 
		"		        \r\n" + 
		"				<media:group>\r\n" + 
		"				<media:thumbnail url=\"http://a.abcnews.com/images/Sports/nm_patriots_071119_mn.jpg\" width=\"320\" height=\"240\" /> <media:thumbnail url=\"http://a.abcnews.com/images/Sports/nm_patriots_071119_mc.jpg\" width=\"100\" height=\"75\" /> <media:thumbnail url=\"http://a.abcnews.com/images/Sports/nm_patriots_071119_mv.jpg\" width=\"264\" height=\"198\" /> \r\n" + 
		"				</media:group>\r\n" + 
		"\r\n" + 
		"		        <title>Pats Steamroll Bills, Move to 10-0</title>\r\n" + 
		"		        <link>http://feeds.feedburner.com/~r/AbcNews_Sports/~3/186994604/wireStory</link>\r\n" + 
		"		        <guid isPermaLink=\"false\"><![CDATA[http://abcnews.go.com/Sports/wireStory?id=3884508]]></guid>\r\n" + 
		"		        <description>Lead by Tom Brady and Randy Moss, Pats scored TDs on their first 7 possessions.&lt;img src=\"http://feeds.feedburner.com/~r/AbcNews_Sports/~4/186994604\" height=\"1\" width=\"1\"/&gt;</description> \r\n" + 
		"</item>" +
		"\r\n" + 
		"</channel>\r\n" + 
		"</rss>\r\n";
	
	public static final String NS_EXAMPLE = "<rss xmlns:media=\"http://search.yahoo.com/mrss/\" version=\"2.0\">\r\n" + 
			"  <channel> \r\n" + 
			"    <item> \r\n" + 
			"      <media:thumbnail width=\"66\" height=\"49\" url=\"http://newsimg.bbc.co.uk/media/images/44240000/jpg/_44240298_mush66.jpg\"/> \r\n" + 
			"      <title>Musharraf \'will quit army soon\'</title>  \r\n" + 
			"      <description>Pakistan\'s attorney general says he expects President Musharraf to resign as army head before 1 December.</description>  \r\n" + 
			"      <link>http://news.bbc.co.uk/go/rss/-/2/hi/south_asia/7096381.stm</link>  \r\n" + 
			"      <guid isPermaLink=\"false\">http://news.bbc.co.uk/2/hi/south_asia/7096381.stm</guid>  \r\n" + 
			"      <pubDate>Thu, 15 Nov 2007 13:58:24 GMT</pubDate>  \r\n" + 
			"      <category>South Asia</category>  \r\n" + 
			"    </item>  \r\n" + 
			"    <item> \r\n" + 
			"      <title>Canadian stun gun death on video</title>  \r\n" + 
			"      <description>A video is released of a Polish immigrant\'s death after he was stun gunned by police at Vancouver airport.</description>  \r\n" + 
			"      <link>http://news.bbc.co.uk/go/rss/-/2/hi/americas/7095875.stm</link>  \r\n" + 
			"      <guid isPermaLink=\"false\">http://news.bbc.co.uk/2/hi/americas/7095875.stm</guid>  \r\n" + 
			"      <pubDate>Thu, 15 Nov 2007 11:18:54 GMT</pubDate>  \r\n" + 
			"      <category>Americas</category>  \r\n" + 
			"      <media:thumbnail width=\"66\" height=\"49\" url=\"http://newsimg.bbc.co.uk/media/images/44240000/jpg/_44240669_taserpolice66.jpg\"/> \r\n" + 
			"    </item>  \r\n" + 
			"  </channel>\r\n" + 
			"</rss>";
	
	public static final String FEEDBURNER_EXAMPlE = 
			"<rss xmlns:feedburner=\"http://rssnamespace.org/feedburner/ext/1.0\" version=\"2.0\">\r\n" + 
			"<channel>\r\n" + 
			"<title>CNN.com</title>\r\n" + 
			"\r\n" + 
			"<item>\r\n" + 
			"<feedburner:origLink>http://www.cnn.com/2007/POLITICS/11/16/congress.crandallmine/index.html</feedburner:origLink>\r\n" + 
			"<title>Utah mine owner faces Senate panel subpoena</title>\r\n" + 
			"\r\n" + 
			"<guid isPermaLink=\"false\">http://www.cnn.com/2007/POLITICS/11/16/congress.crandallmine/index.html</guid>\r\n" + 
			"<link>http://rss.cnn.com/~r/rss/cnn_topstories/~3/186060716/index.html</link>\r\n" + 
			"<description>A Senate subcommittee probing this summer\'s deadly Utah mine disaster has subpoenaed the mine\'s co-owner, ranking member Sen. Arlen Specter said Friday.</description>\r\n" + 
			"<pubDate>Fri, 16 Nov 2007 19:53:04 EST</pubDate>\r\n" + 
			"</item>\r\n" + 
			"\r\n" + 
			"</channel>\r\n" + 
			"</rss>\r\n";
	
	public static final String FLICKR_EXAMPLE =
		"<rss version=\"2.0\"\r\n" + 
		"        xmlns:media=\"http://search.yahoo.com/mrss/\"\r\n" + 
		"	xmlns:dc=\"http://purl.org/dc/elements/1.1/\"\r\n" + 
		"        >\r\n" + 
		"	<channel>\r\n" + 
		"		<title>Photos from everyone tagged water</title>\r\n" + 
		"		<link>http://www.flickr.com/photos/tags/water/</link>\r\n" + 
		"		<item>\r\n" + 
		"			<title>Fire-Works</title>\r\n" + 
		"			<link>http://www.flickr.com/photos/meemz/2161548299/</link>\r\n" + 
		"			<media:title>Fire-Works</media:title>\r\n" + 
		"\r\n" + 
		"		</item>\r\n" + 
		"</channel>\r\n" + 
		"</rss>\r\n";
	
	public static final File 	outputFile			= new File("/temp/rss.xml");
	public static void main(String[] args)
	{
		ClassDescriptor fdClassDescriptor = ClassDescriptor.getClassDescriptor(FieldDescriptor.class);
		RssState rss	= new RssState();
		Channel channel	= new Channel();
		rss.channel		= channel;
		
		try
		{
			rss.translateToXML(System.out);
		}
		catch (XMLTranslationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void doMain()
	{
		try
		{
//			ParsedURL feedPURL	= ABC_SPORTS_FEED; // CNN_TOP_FEED;
			ParsedURL feedPURL	= DELICIOUS_FEED; // FLICKR_FEED;
			println("Translating RSS feed: " + feedPURL+"\n");

			RssState rssState	= (RssState) ElementState.translateFromXML(feedPURL, RssTranslations.get());
//			RssState rssState	= (RssState) ElementState.translateFromXMLCharSequence(FLICKR_EXAMPLE, RssTranslations.get());

			ArrayList<Item> items	= rssState.getChannel().set(); //rssState.getChannel().getItems();
			println("items: " +  items.size());
			for (Item item : items)
			{
				println("description:\t" + item.description);
				//FIXME! -- how do we make sure the prefix gets propagated through this call into F2XOs?!
				Media media	= (Media) item.getNestedNameSpace("media");
				if (media != null)
				{
					Thumbnail thumbnail	= media.getThumbnail();
					if (thumbnail != null)
						thumbnail.translateToXML(System.err);
//					media.translateToXML(System.err);
					System.err.println('\n');
				}
				Feedburner feedburner = (Feedburner) item.getNestedNameSpace("feedburner");
				if (feedburner != null)
				{
					feedburner.translateToXML(System.err);
					System.err.println('\n');
				}
			}

			println("\n");
			
			rssState.translateToXML(System.err);
			println("\n");
			rssState.writePrettyXML(System.err);
			
//			RssState rssState2	= (RssState) ElementState.translateFromXMLCharSequence(retranslated, RssTranslations.get());
//			rssState2.translateToXML(System.out);

//			rssState.writePrettyXML(outputFile);
			
			println("\n");
		} catch (XMLTranslationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
