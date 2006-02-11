package ecologylab.xml.rss;

import java.util.HashMap;

import ecologylab.generic.ParsedURL;
import ecologylab.xml.*;

public class Item extends ElementState
{
   public String				title;
  // public ElementState			title;
   //public ElementState			link;
   public String				description;
   public ParsedURL				link;
   //public String				description;
   public ElementState			author;
   
   static HashMap				elementsWithOneTextChild	= new HashMap();
   
   static
   {
	   elementsWithOneTextChild.put("title", "title");
	   elementsWithOneTextChild.put("link", "link");
	   elementsWithOneTextChild.put("description", "description");
   }
   
   public HashMap fieldsAsElementWithOneTextChild()
   {
	   return elementsWithOneTextChild;
   }
}
