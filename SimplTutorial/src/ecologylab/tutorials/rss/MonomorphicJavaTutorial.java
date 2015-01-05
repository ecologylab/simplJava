package ecologylab.tutorials.rss;
import ecologylab.tutorials.rss.*;
import java.io.IOException;
import ecologylab.serialization.*;
import ecologylab.serialization.formatenums.StringFormat;

import java.net.URL;
import ecologylab.net.*;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.net.MalformedURLException;

public class MonomorphicJavaTutorial {
	 public static void main(String[] args) throws Exception
     {
   
		  SimplTypesScope rssTranslations = RssTranslations.get(); 
		  URL url = new URL("http://www.xkcd.com/rss.xml");
		  String rssContent = readURL(url);

		  System.out.println("Raw RSS Feed:");
		  System.out.println(rssContent);
		  
		  Rss feed = (Rss) rssTranslations.deserialize(rssContent, StringFormat.XML);
		   
		  System.out.println("\nFeed translated back to xml by s.im.pl serialization:");
		  System.out.println(SimplTypesScope.serialize(feed, StringFormat.XML));
		  
		  Item ecologylabItem = new Item();
		  ecologylabItem.setTitle("The Interface Ecology Lab");
		  ecologylabItem.setDescription("Highlights the cool research going on at the lab.");
		  ecologylabItem.setAuthor("Dr. Andruid Kerne");
		  ecologylabItem.setLink(new ParsedURL(new URL("http://www.ecologylab.net")));

//		  feed.getChannel().getItems().add(0, ecologylabItem);

	//	  System.out.println("\nFeed translated to xml with our added item:");
		//  System.out.println(feed.serialize());
	}
	 
	public static String readURL(URL earl) throws MalformedURLException, IOException{
		InputStream is = null;
		DataInputStream dis;
		String line;

		StringBuilder sb = new StringBuilder();
		
		try {
		    is = earl.openStream();  // throws an IOException
		    dis = new DataInputStream(new BufferedInputStream(is));

		    while ((line = dis.readLine()) != null) {
		        sb.append(line);
		    }
		    
		} catch (MalformedURLException mue) {
		     mue.printStackTrace();
		} catch (IOException ioe) {
		     ioe.printStackTrace();
		} finally {
		    try {
		        is.close();
		    } catch (IOException ioe) {
		        // nothing to see here
		    }
		}
		return sb.toString();
	}
}
