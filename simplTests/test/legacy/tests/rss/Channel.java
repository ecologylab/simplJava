//Channel.java

package legacy.tests.rss;

import java.util.ArrayList;

import simpl.annotations.dbal.Hint;
import simpl.annotations.dbal.simpl_collection;
import simpl.annotations.dbal.simpl_hints;
import simpl.annotations.dbal.simpl_nowrap;
import simpl.annotations.dbal.simpl_scalar;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;

public class Channel extends ElementState
{
  @simpl_nowrap
  @simpl_collection("item") 
  ArrayList<Item> 		items;
  
  @simpl_scalar @simpl_hints(Hint.XML_LEAF) String		title;
  @simpl_scalar @simpl_hints(Hint.XML_LEAF) String		description;
  @simpl_scalar @simpl_hints(Hint.XML_LEAF) ParsedURL	link;
     
   public Channel() { }

   public ArrayList<Item> getItems() 
   {
	   return items;
   }
}
