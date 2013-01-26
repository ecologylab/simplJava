//Item.java

package legacy.tests.rss;

import java.util.ArrayList;

import simpl.annotations.dbal.Hint;
import simpl.annotations.dbal.simpl_collection;
import simpl.annotations.dbal.simpl_hints;
import simpl.annotations.dbal.simpl_nowrap;
import simpl.annotations.dbal.simpl_scalar;
import simpl.core.ElementState;

import ecologylab.net.ParsedURL;

public class Item extends ElementState
{
   @simpl_scalar @simpl_hints(Hint.XML_LEAF) String			title;
   @simpl_scalar @simpl_hints(Hint.XML_LEAF) String			description;
   @simpl_scalar @simpl_hints(Hint.XML_LEAF) ParsedURL		link;
   @simpl_scalar @simpl_hints(Hint.XML_LEAF) ParsedURL		guid;
   @simpl_scalar @simpl_hints(Hint.XML_LEAF) String			author;
   
//   @simpl_nowrap
//   @simpl_collection("category") 
   ArrayList<String>		categorySet;
   
   public Item() {}

   public void setTitle(String string) 
   {
	   title = string;
   }
	
   public void setDescription(String string)
   {
	   description = string;
	   
   }
	
   public void setAuthor(String string) 
   {
	   author = string;
		
   }
	
   public void setLink(ParsedURL parsedURL) 
   {
	  link = parsedURL;	
   }
}
