package ecologylab.translators.sql.testing.ecologylabXmlTest;

import java.io.File;
import java.util.ArrayList;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.annotations.DbHint;
import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_db;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.library.feedburner.Feedburner;
import ecologylab.serialization.library.media.Media;
import ecologylab.serialization.library.media.Thumbnail;
import ecologylab.serialization.library.rss.RssTranslations;

/**
 * {@link ecologylab.serialization.ElementState ElementState} for the root element of the RSS parser.
 * In particular, this supports RSS versions such as .91, .92, .93, .94, and 2.0.
 *
 * @author andruid
 */
public class RssStateTest extends ElementState
{
	@simpl_scalar	@simpl_db({DbHint.NOT_NULL}) float		version;
   
	@simpl_composite	@simpl_db({DbHint.PRIMARY_KEY})	ChannelTest		channel;

	/**
	 * @return Returns the channel.
	 */
	public ChannelTest getChannel()
	{
		return channel;
	}

	/**
	 * @param channel The channel to set.
	 */
	public void setChannel(ChannelTest channel)
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
		"			<media:title>Fire-Worksing</media:title>\r\n" + 
		"			<title>Fire-Works</title>\r\n" + 
		"			<link>http://www.flickr.com/photos/meemz/2161548299/</link>\r\n" + 
		"			<media:title>Fire-Works</media:title>\r\n" + 
		"\r\n" + 
		"		</item>\r\n" + 
		"</channel>\r\n" + 
		"</rss>\r\n";
	
	public static final String ITEM_EXAMPLE = 
		"	<channel>\r\n" + 
		"			<foo>Fire-Works</foo>\r\n" + 
		"		<item>\r\n" + 
		"			<title>Fire-Works</title>\r\n" + 
		"			<link>http://www.flickr.com/photos/meemz/2161548299/</link>\r\n" + 
//		"			<media:title>Fire-Works</media:title>\r\n" + 
		"\r\n" + 
		"		</item>\r\n" + 
		"		<title>Photos from everyone tagged water</title>\r\n" + 
		"		<link>http://www.flickr.com/photos/tags/water/</link>\r\n" + 
		"</channel>\r\n";
	
	public static final String NABEEL_TEST = 
	"<rss version=\"1\">"+
	"<channel>"+
	"	<title>title.Nabeel</title>"+
	"	<description>description.Student</description>"+
	"	<link>http://www.google.com/ig</link>"+
	"	<items>"+
	"		<item>"+
	"			<title>Item.title.Nabeel</title>"+
	"			<description>Item.description.Nabeel</description>"+
	"			<link>http://www.google.com/ig</link>"+
	"			<guid>http://www.google.com/ig</guid>"+
	"			<author>Item.author.Nabeel</author>"+
	"			<category>newCat 0</category>"+
	"			<category>newCat 1</category>"+
	"		</item>"+
	"		<item>"+
	"			<title>Item.title.Nabeel</title>"+
	"			<description>Item.description.Nabeel</description>"+
	"			<link>http://www.google.com/ig</link>"+
	"			<guid>http://www.google.com/ig</guid>"+
	"			<author>Item.author.Nabeel</author>"+
	"			<category>newCat 0</category>"+
	"			<category>newCat 1</category>"+
	"		</item>"+
	"	</items>"+
	"</channel>"+
	"</rss>";

	public static final File 	outputFile			= new File("/temp/rss.xml");
	
	
}
