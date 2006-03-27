package ecologylab.xml.rss;

import java.util.HashMap;

import ecologylab.generic.ParsedURL;
import ecologylab.xml.*;

/**
 * RSS 1 item element declaration.
 *
 * @author andruid
 */
public class Item extends ElementStateWithLeafElements
{
   public String				title;
  // public ElementState			title;
   //public ElementState			link;
   public String				description;
   public ParsedURL				link;
   //public ElementState		description;
   public String				author;
   
   static final String[]		LEAF_ELEMENT_FIELD_NAMES	= {"title", "link", "description", "author"};
   
   static
   {
	   defineLeafElementFieldNames(LEAF_ELEMENT_FIELD_NAMES);
   }
   
}
