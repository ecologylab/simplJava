package ecologylab.xml.library.rss;

import ecologylab.net.ParsedURL;
import ecologylab.xml.*;

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
	
	public static void main(String[] args)
	{
		try
		{
			RssState rssState	= (RssState) ElementState.translateFromXML(NYT_TECH_FEED, RssTranslations.get());
			println("items: " + rssState.getChannel()/*.getItems() */.size());
			
			String retranslated	= rssState.translateToXML();
			println(retranslated);
			println("\n");
			RssState rssState2	= (RssState) ElementState.translateFromXMLString(retranslated, RssTranslations.get());
			String reretranslated	= rssState2.translateToXML();
			println(reretranslated);
		} catch (XmlTranslationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
