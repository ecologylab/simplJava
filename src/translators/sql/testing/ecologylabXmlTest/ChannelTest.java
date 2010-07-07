package translators.sql.testing.ecologylabXmlTest;

import java.util.ArrayList;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.Hint;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.simpl_inherit;

/**
 * RSS parser <code>channel</code> element {@link ecologylab.serialization.ElementState ElementState} declaration.
 * Used with most RSS versions.
 *
 * @author andruid
 */
public @simpl_inherit class ChannelTest extends ElementState
{
   @simpl_scalar @simpl_hints(Hint.XML_LEAF)	String			title;
   @simpl_scalar @simpl_hints(Hint.XML_LEAF)	String			description;
   /**
    * Could point to an HTML rendering of the feed.
    */
   @simpl_collection("thisLinkTag") @simpl_scalar @simpl_hints(Hint.XML_LEAF)	ParsedURL		link;
   
   @simpl_nowrap @simpl_collection("item") ArrayList<ItemTest> items;
   
   @simpl_db({DbHint.PRIMARY_KEY, DbHint.UNIQUE}) String primaryUniqueKey; 
   
   
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
			ElementState c2	= RssTranslationsTest.get().deserializeCharSequence(buffy);
			System.out.println("retranslated by ElementState");
			System.out.println(c2.getClass().getCanonicalName());
			System.out.println(c2.getClass().getSuperclass().getCanonicalName()); 
			Class<?>[] thisClasses = c2.getClass().getClasses();
			for (Class<?> class1 : thisClasses)
			{
				System.out.println(class1.getCanonicalName()); 
			}
			c2.serialize(System.out);
//			println(c.translateToXML());
		} catch (SIMPLTranslationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
