package ecologylab.xml.library.rss;

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
public class Item extends ElementState
{
   @xml_leaf	String			title;
   @xml_leaf	String			description;
   @xml_leaf	ParsedURL		link;
   @xml_leaf	String			author;
   
   /**
    * Some people put Dublin Core fields into their items. Go figure :-)
    */
   @xml_nested	Dc				dc;
   
   @xml_nested	Media			media;
   
   @xml_nested	Feedburner		feedburner;

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
    * @return Returns the dc.
    */
   public Dc getDc()
   {
	   return dc;
   }
   
   /**
    * @param dc The dc to set.
    */
   public void setDc(Dc dc)
   {
	   this.dc = dc;
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
    * @return Returns the media.
    */
   public Media getMedia()
   {
	   return media;
   }
   
   /**
    * @param media The media to set.
    */
   public void setMedia(Media media)
   {
	   this.media = media;
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
    * If there is an embedded object handling the feedburner namespace, get the origLink
    * leaf_node from it.
    * 
    * @return
    */
   public ParsedURL getFeedburnerOrigLink()
   {
	   return (feedburner == null) ? null : feedburner.getOrigLink();
   }
   
   /**
    * Get what looks like the most direct form of the link URL.
    *  
    * @return
    */
   public ParsedURL getDirectLink()
   {
	   ParsedURL result	= getFeedburnerOrigLink();
	   return (result != null) ? result : link;
   }
}
