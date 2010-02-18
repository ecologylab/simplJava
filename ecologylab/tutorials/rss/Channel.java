//Channel.java

package ecologylab.tutorials.rss;

import java.util.ArrayList;
import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;

public class Channel extends ElementState
{
   @xml_leaf String		title;
   @xml_leaf String		description;
   @xml_leaf ParsedURL	link;
   
   @xml_nowrap
   @xml_collection("item") 
   ArrayList<Item> 		items;
   
   public Channel() { }

   public ArrayList<Item> getItems() 
   {
	   return items;
   }
}
