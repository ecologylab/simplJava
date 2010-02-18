package ecologylab.tutorials.rss;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationScope;
import ecologylab.xml.XMLTranslationException;

public class MonomorphicTutorial {
	
	/* Read in content from a URL */
	public static String readURL(URL url) throws IOException
	{
		StringBuilder buf = new StringBuilder();
		BufferedReader in = new BufferedReader(new InputStreamReader(url
				.openStream()));

		String inputLine;

		while ((inputLine = in.readLine()) != null)
		{
			buf.append(inputLine);
		}

		in.close();
		return buf.toString();
	}

	public static void main(String[] args) throws IOException,
			XMLTranslationException
	{
		/*
		 * Get the translation scope. This references all of the classes that we
		 * are considering for translation.
		 */
		TranslationScope rssTranslations = RssTranslations.get();

		/*
		 * Read in RSS feed from URL
		 */
		URL url = new URL("http://www.xkcd.com/rss.xml");
		String rssContent = readURL(url);

		System.out.println("Raw RSS Feed:");
		System.out.println(rssContent);

		/*
		 * Instantiate RssState by translating the xml to java objects. Take a
		 * look at RssState, Channel, and Item to see how they are annotated to
		 * facilitate translation. Note that RssState's class tag is rss. This is
		 * an inherent rule: all classes that subclass ElementState have a class
		 * tag of just the class name. Normally ecolobylab.xml uses a camel-case
		 * translation, so InitConnectionResponse has a class tag of
		 * "init_connection_response". Fields that are translated into attributes
		 * and sub elements use a similar convention for determining identifiers.
		 */
		RssState feed = (RssState) ElementState.translateFromXML(url,
				rssTranslations);

		/*
		 * Notice that, translated back to xml, not all attributes and elements
		 * still remain. If an attribute or element is not annotated in the
		 * corresponding java class it is simply ignored.
		 */
		System.out.println("\nFeed translated back to xml by ecologylab.xml:");
		System.out.println(feed.translateToXML());

		/*
		 * Create our own item to add to the channel
		 */
		Item ecologylabItem = new Item();
		ecologylabItem.setTitle("The Interface Ecology Lab");
		ecologylabItem
				.setDescription("Highlights the cool research going on at the lab.");
		ecologylabItem.setAuthor("Dr. Andruid Kerne");
		ecologylabItem
				.setLink(new ParsedURL(new URL("http://www.ecologylab.net")));

		/*
		 * Add our item to the front of the channel.
		 */
		feed.getChannel().getItems().add(0, ecologylabItem);

		System.out.println("\nFeed translated to xml with our added item:");
		System.out.println(feed.translateToXML());
	}

}
