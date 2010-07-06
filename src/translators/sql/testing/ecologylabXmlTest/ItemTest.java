package translators.sql.testing.ecologylabXmlTest;

import java.util.ArrayList;

import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;
import ecologylab.xml.library.dc.Dc;
import ecologylab.xml.library.feedburner.Feedburner;
import ecologylab.xml.library.media.Media;

/**
 * RSS parser <code>ite</code> element {@link ecologylab.xml.ElementState ElementState} declaration.
 * Used with most (perhaps all) RSS versions.
 * <p/>
 * Includes support for funky included namespace content, such as DublinCore, Yahoo Media, and Feedburner.
 *
 * @author andruid
 */
public class ItemTest extends ElementState
{
   @xml_leaf	String			title;
//   @xml_leaf	String			description;
   @xml_attribute String description; 
   /**
    * This version of link often has a special url with rss in it.
    */
   @xml_leaf	ParsedURL		link;
   /**
    * This seems to be the version of link that users want to see.
    */
   @xml_leaf	ParsedURL		guid;
   @xml_leaf	String			author;

   @xml_nowrap
   @xml_collection("category") ArrayList<String>	categorySet;
   
   public ItemTest()
   {
  	 super();
   }
   public ItemTest(String title)
   {
  	 super();
  	 this.title	= title;
   }
   /**
    * @return Returns the author.
    */
   public String getAuthor()
   {
	   return author;
   }
   
   /**
    * @param author The author to set.
    */
   public void setAuthor(String author)
   {
	   this.author = author;
   }
   
   /**
    * @return Returns the description.
    */
   public String getDescription()
   {
	   return description;
   }
   
   /**
    * @param description The description to set.
    */
   public void setDescription(String description)
   {
	   this.description = description;
   }
   
   /**
    * @return Returns the link.
    */
   public ParsedURL getLink()
   {
	   return link;
   }
   
   /**
    * @param link The link to set.
    */
   public void setLink(ParsedURL link)
   {
	   this.link = link;
   }
   
   /**
    * @return Returns the title.
    */
   public String getTitle()
   {
	   return title;
   }
   
   /**
    * @param title The title to set.
    */
   public void setTitle(String title)
   {
	   this.title = title;
   }
   
   /**
    * Some people put Dublin Core fields into their items. Go figure :-)
	* <p/>
    * Lookup a NestedNameSpace element child of this, in case there is one,
    * declared as xmlns:dc.
    * (Example: del.icio.us)
    * 
    * @return Returns the Dc nested namespace element, or null..
    */
   public Dc lookupDc()
   {
	   return (Dc) lookupNestedNameSpace("dc");
   }
   
   /**
    * Lookup a NestedNameSpace element child of this, in case there is one,
    * declared as xmlns:media.
    * Yahoo Media metadata declarations.
    * 
    * @return Returns the Media nested namespace element, or null..
    */
   public Media lookupMedia()
   {
	   return (Media) lookupNestedNameSpace("media");
   }
   
   
   /**
    * Lookup a NestedNameSpace element child of this, in case there is one,
    * declared as xmlns:feedburner.
    * 
    * @return Returns the Feedburner nested namespace element, or null..
    */
   public Feedburner lookupFeedburner()
   {
	   return (Feedburner) lookupNestedNameSpace("feedburner");
   }

   /**
    * If there is an embedded object handling the feedburner namespace, get the origLink
    * leaf_node from it.
    * 
    * @return
    */
   public ParsedURL getFeedburnerOrigLink()
   {
	   Feedburner feedburner	= lookupFeedburner();
	   return (feedburner == null) ? null : feedburner.getOrigLink();
   }
   
   public ParsedURL getDcIdentifier()
   {
	   Dc dc					= lookupDc();
	   return (dc == null) ? null : dc.getIdentifier();
   }
   
   /**
    * Get what looks like the most direct form of the link URL.
    *  
    * @return
    */
   public ParsedURL getDirectLink()
   {
	   ParsedURL result	= guid;
	   if (result == null)
		   result		= getFeedburnerOrigLink();
	   if (result == null)
		   result		= getDcIdentifier();
	   
	   return (result != null) ? result : link;
   }
}
