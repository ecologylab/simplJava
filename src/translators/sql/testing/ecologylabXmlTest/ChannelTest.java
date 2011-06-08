package ecologylab.translators.sql.testing.ecologylabXmlTest;

import java.util.ArrayList;

import ecologylab.net.ParsedURL;
import ecologylab.semantics.metadata.builtins.Document;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.Hint;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.simpl_inherit;
import ecologylab.serialization.library.rss.RssTranslations;

/**
 * RSS parser <code>channel</code> element {@link ecologylab.serialization.ElementState ElementState} declaration.
 * Used with most RSS versions.
 *
 * @author andruid
 */
public @simpl_inherit class ChannelTest extends ElementState
{
   @simpl_scalar @simpl_hints(Hint.XML_LEAF)	@simpl_db(references="ItemTest", value =
 	{DbHint.NOT_NULL, DbHint.PRIMARY_KEY}) String			title;
   
   @simpl_scalar @simpl_hints(Hint.XML_LEAF)	@simpl_db({DbHint.NOT_NULL, DbHint.UNIQUE}) String			description;
   /**
    * Could point to an HTML rendering of the feed.
    */
   @simpl_scalar @simpl_hints(Hint.XML_LEAF)	@simpl_db({DbHint.UNIQUE}) ParsedURL		link;
   
   @simpl_nowrap @simpl_collection("item") @simpl_db({DbHint.NOT_NULL}) ArrayList<ItemTest> items;
   
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

	public ArrayList<ItemTest> getItems()
	{
		return items;
	}

	public void add(ItemTest item)
	{
		if (items == null)
			items	= new ArrayList<ItemTest>();
		items.add(item);
	}
	
	public ItemTest get(int i)
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
		ChannelTest c	= new ChannelTest();
		ItemTest i1		= new ItemTest();
		i1.author	= "zach";
		i1.title	= "it is called rogue!";
		i1.link		= ParsedURL.getAbsolute("http://ecologylab.cs.tamu.edu/rogue/");
		i1.description = "its a game";
		ItemTest i2 = new ItemTest();
		i2.author = "andruid";
		i2.title = "it is called cf!";
		i2.description	= "its a creativity support tool";
		c.items		= new ArrayList<ItemTest>();
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
