package ecologylab.xml.rss;

import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;
import ecologylab.xml.dc.Dc;
import ecologylab.xml.media.Media;

/**
 * RSS 1 item element declaration.
 *
 * @author andruid
 */
public class Item extends ElementState
{
   public String				title;
  // public ElementState			title;
   //public ElementState			link;
   public String				description;
   public ParsedURL				link;
   //public ElementState		description;
   public String				author;
   
   /**
    * Some people put Dublin Core fields into their items. Go figure :-)
    */
   public Dc					dc;
   
   public Media					media;
   
   static final String[]		LEAF_ELEMENT_FIELD_NAMES	= {"title", "link", "description", "author"};
   
   /**
    * The array of Strings with the names of the leaf elements.
    * 
    * @return
    */
   protected String[] leafElementFieldNames()
   {
	   return LEAF_ELEMENT_FIELD_NAMES;
   }
}
