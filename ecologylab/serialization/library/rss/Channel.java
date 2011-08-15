package ecologylab.serialization.library.rss;

import java.util.ArrayList;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.simpl_inherit;
import ecologylab.serialization.annotations.Hint;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_hints;
import ecologylab.serialization.annotations.simpl_nowrap;
import ecologylab.serialization.annotations.simpl_scalar;

/**
 * RSS parser <code>channel</code> element {@link ecologylab.serialization.ElementState ElementState} declaration.
 * Used with most RSS versions.
 *
 * @author andruid
 */
public @simpl_inherit class Channel extends ElementState
{
   @simpl_scalar @simpl_hints(Hint.XML_LEAF)	String			title;
   @simpl_scalar @simpl_hints(Hint.XML_LEAF)	String			description;
   /**
    * Could point to an HTML rendering of the feed.
    */
   @simpl_scalar @simpl_hints(Hint.XML_LEAF)	ParsedURL		link;
   
   @simpl_nowrap @simpl_collection("item") ArrayList<Item> items;
   
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
	
	public Item get(int i)
	{
		return items == null ? null : items.get(i);
	}
	public int size()
	{
		return items == null ? 0 : items.size();
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
			c.serialize(buffy);
			System.out.println(buffy);
			System.out.println('\n');
			ElementState c2	= RssTranslations.get().deserializeCharSequence(buffy);
			c2.serialize(System.out);
//			println(c.translateToXML());
		} catch (SIMPLTranslationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
