package ecologylab.xml.library.rss;

import java.util.ArrayList;
import java.util.Collection;

import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;
import ecologylab.xml.XMLTranslationException;
import ecologylab.xml.xml_inherit;
import ecologylab.xml.types.element.ArrayListState;

/**
 * RSS parser <code>channel</code> element {@link ecologylab.xml.ElementState
 * ElementState} declaration. Used with most RSS versions.
 * 
 * @author andruid
 */
public @xml_inherit
class Channel extends ArrayListState<Item>
{
	/*
	 * @xml_leaf specifies that these scalar types should be
	 */
	@xml_leaf
	String				title;

	@xml_leaf
	String				description;

	/**
	 * Could point to an HTML rendering of the feed.
	 */
	@xml_leaf
	ParsedURL			link;

	/*
	 *  @xml_collection overrides the default tag name for the collection to item
	 *  instead of items
	 */
	@xml_collection("item")
	
	/* 
	 * @xml_nowrap specifies that items' elements will appear directly in channel
	 * no in a sub element
	 */
	@xml_nowrap
	ArrayList<Item>	items;

	/**
	 * @return Returns the description.
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description
	 *           The description to set.
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
	 *           The title to set.
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
	 *           The link to set.
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

}
