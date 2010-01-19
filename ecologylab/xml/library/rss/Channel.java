package ecologylab.xml.library.rss;

import java.util.ArrayList;
import java.util.Collection;

import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;
import ecologylab.xml.XMLTranslationException;
import ecologylab.xml.xml_inherit;
import ecologylab.xml.types.element.ArrayListState;

/**
 * RSS parser <code>channel</code> element {@link ecologylab.xml.ElementState ElementState} declaration.
 * Used with most RSS versions.
 *
 * @author andruid
 */
public @xml_inherit class Channel extends ElementState
{
   @xml_leaf	String			title;
   @xml_leaf	String			description;
   /**
    * Could point to an HTML rendering of the feed.
    */
   @xml_leaf	ParsedURL		link;
   
   @xml_collection("item") ArrayList<Item> items;
   
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

	public ArrayList<Item> getItems()
	{
		return items;
	}

	public void add(Item item)
	{
		if (items == null)
			items	= new ArrayList<Item>();
		items.add(item);
	}

	public static void main(String[] s)
	{
		testTranslateTo();
	}
	private static void testTranslateTo() 
	{
		Channel c	= new Channel();
		Item i1		= new Item();
		i1.author	= "zach";
		i1.title	= "it is called rogue!";
		i1.link		= ParsedURL.getAbsolute("http://ecologylab.cs.tamu.edu/rogue/");
		i1.description = "its a game";
		Item i2 = new Item();
		i2.author = "andruid";
		i2.title = "it is called cf!";
		i2.description	= "its a creativity support tool";
		c.items		= new ArrayList<Item>();
		c.items. add(i1);
		c.items.add(i2);
		try
		{
			StringBuilder buffy	= new StringBuilder();
			c.translateToXML(buffy);
			System.out.println(buffy);
			System.out.println('\n');
			ElementState c2	= ElementState.translateFromXMLCharSequence(buffy, RssTranslations.get());
			c2.translateToXML(System.out);
//			println(c.translateToXML());
		} catch (XMLTranslationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
