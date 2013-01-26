package ecologylab.serialization.library.rss;

import java.util.ArrayList;

import simpl.annotations.dbal.Hint;
import simpl.annotations.dbal.simpl_collection;
import simpl.annotations.dbal.simpl_hints;
import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_nowrap;
import simpl.annotations.dbal.simpl_scalar;
import simpl.exceptions.SIMPLTranslationException;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.formatenums.StringFormat;

/**
 * RSS parser <code>channel</code> element {@link ecologylab.serialization.ElementState
 * ElementState} declaration. Used with most RSS versions.
 * 
 * @author andruid
 */
public @simpl_inherit
class Channel2 extends ElementState // ArrayListState<Item>
{
	@simpl_scalar
	@simpl_hints(Hint.XML_LEAF)
	String					title;

	@simpl_scalar
	@simpl_hints(Hint.XML_LEAF)
	String					description;

	/**
	 * Could point to an HTML rendering of the feed.
	 */
	@simpl_scalar
	@simpl_hints(Hint.XML_LEAF)
	ParsedURL				link;

	@simpl_nowrap
	@simpl_collection("item")
	ArrayList<Item>	items;				// = new ArrayList<Item>();

	/**
	 * @return Returns the description.
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description
	 *          The description to set.
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
	 * @param title
	 *          The title to set.
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
	 * @param link
	 *          The link to set.
	 */
	public void setLink(ParsedURL link)
	{
		this.link = link;
	}

	public ArrayList<Item> getItems()
	{
		return items;
	}

	public void setItems(ArrayList<Item> items)
	{
		this.items = items;
	}

	public int size()
	{
		return items == null ? 0 : items.size();
	}

	public Item get(int i)
	{
		return items == null ? null : items.get(i);
	}

	// @Override
	// protected Collection<? extends ElementState> getCollection(Class thatClass)
	// {
	// return items;
	// }

	public static void main(String[] s)
	{
		Channel2 c = new Channel2();
		Item i1 = new Item();
		i1.author = "zach";
		i1.title = "it is called rogue!";
		i1.link = ParsedURL.getAbsolute("http://ecologylab.cs.tamu.edu/rogue/");
		i1.description = "its a game";
		Item i2 = new Item();
		i2.author = "andruid";
		i2.title = "it is called cf!";
		i2.description = "its a creativity support tool";
		c.items = new ArrayList<Item>();
		c.items.add(i1);
		c.items.add(i2);
		// c.add(i1);
		// c.add(i2);
		try
		{

			SimplTypesScope.serialize(c, System.out, StringFormat.XML);

		}
		catch (SIMPLTranslationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
