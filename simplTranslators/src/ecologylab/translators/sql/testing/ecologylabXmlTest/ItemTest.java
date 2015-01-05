package ecologylab.translators.sql.testing.ecologylabXmlTest;

import java.util.ArrayList;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.Hint;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_hints;
import ecologylab.serialization.annotations.simpl_nowrap;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.library.dc.Dc;
import ecologylab.serialization.library.feedburner.Feedburner;
import ecologylab.serialization.library.media.Media;

/**
 * RSS parser <code>ite</code> element {@link ecologylab.serialization.ElementState ElementState} declaration.
 * Used with most (perhaps all) RSS versions.
 * <p/>
 * Includes support for funky included namespace content, such as DublinCore, Yahoo Media, and Feedburner.
 *
 * @author andruid
 */
public class ItemTest extends ElementState
{
   @simpl_scalar @simpl_hints(Hint.XML_LEAF)	String			title;
   @simpl_scalar @simpl_hints(Hint.XML_LEAF)	String			description;
   /**
    * This version of link often has a special url with rss in it.
    */
   @simpl_scalar @simpl_hints(Hint.XML_LEAF)	ParsedURL		link;
   /**
    * This seems to be the version of link that users want to see.
    */
   @simpl_scalar @simpl_hints(Hint.XML_LEAF)	ParsedURL		guid;
   @simpl_scalar @simpl_hints(Hint.XML_LEAF)	String			author;

   @simpl_nowrap
   @simpl_collection("category") ArrayList<String>	categorySet;
   
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
 
}
