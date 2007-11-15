package ecologylab.xml.library.rss;

import java.util.ArrayList;

import java.io.File;

import ecologylab.net.ParsedURL;
import ecologylab.xml.*;
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
   
	@xml_nested		Channel		channel;

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
	
	public static final String NS_EXAMPLE = "<rss xmlns:media=\"http://search.yahoo.com/mrss/\" version=\"2.0\">\r\n" + 
			"  <channel> \r\n" + 
			"    <item> \r\n" + 
			"      <title>Musharraf \'will quit army soon\'</title>  \r\n" + 
			"      <description>Pakistan\'s attorney general says he expects President Musharraf to resign as army head before 1 December.</description>  \r\n" + 
			"      <link>http://news.bbc.co.uk/go/rss/-/2/hi/south_asia/7096381.stm</link>  \r\n" + 
			"      <guid isPermaLink=\"false\">http://news.bbc.co.uk/2/hi/south_asia/7096381.stm</guid>  \r\n" + 
			"      <pubDate>Thu, 15 Nov 2007 13:58:24 GMT</pubDate>  \r\n" + 
			"      <category>South Asia</category>  \r\n" + 
			"      <media:thumbnail width=\"66\" height=\"49\" url=\"http://newsimg.bbc.co.uk/media/images/44240000/jpg/_44240298_mush66.jpg\"/> \r\n" + 
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
	
	public static final File 	outputFile			= new File("/temp/rss.xml");
	public static void main(String[] args)
	{
//		ElementState.setUseDOMForTranslateTo(true);
		try
		{
			ParsedURL feedPURL	= BBC_FRONT_FEED;
			println("Translating RSS feed: " + feedPURL+"\n");

//			RssState rssState	= (RssState) ElementState.translateFromXMLSAX(feedPURL, RssTranslations.get());
//			RssState rssState	= (RssState) ElementState.translateFromXML(feedPURL, RssTranslations.get());
			RssState rssState	= (RssState) ElementState.translateFromXMLCharSequence(NS_EXAMPLE, RssTranslations.get());

			ArrayList<Item> items	= rssState.getChannel().set(); //rssState.getChannel().getItems();
			println("items: " +  items.size());
			for (Item item : items)
			{
				println("description:\t" + item.description);
				Media media	= (Media) item.getNestedNameSpace("media");
				media.translateToXML(System.err);
				System.err.println('\n');
			}

			StringBuilder retranslated	= rssState.translateToXML();
			println(retranslated);
//			println("\n");
//			RssState rssState2	= (RssState) ElementState.translateFromXMLCharSequence(retranslated, RssTranslations.get());
//			rssState2.translateToXML(System.out);

			rssState.writePrettyXML(outputFile);
		} catch (XMLTranslationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
