package ecologylab.xml.library.media;

import java.lang.reflect.Field;
import java.util.ArrayList;

import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;
import ecologylab.xml.ElementState.xml_leaf;

/**
 * Primary element of the media XML name space. As in <media:content>
 *
 * @author andruid
 */
public class Content extends ElementState
{
	@xml_attribute	ParsedURL		url;
	@xml_attribute	String			type;
	@xml_attribute	int				width;
	@xml_attribute	int				height;
	
	@xml_leaf		String			title;
	@xml_nested 	Description		description;
	@xml_attribute	String			keywords;
	@xml_nested 	Thumbnail		thumbnail;
	
	// there can be 0 or more elements of tag "category"
	// we will add these to a collection automatically by overriding setField(Field, String)
	//TODO confirm if this is correct.
	@xml_collection	ArrayList<String>		categoryStrings;
	
	@xml_nested		Credit			credit;
	
	//public String 		text -- actually there can be many of these
	//public String			restriction; // a leaf node
	//
	

	public Content()
	{
		super();
	}

	/**
	 * Lazy evaluation avoids unnecessary allocations.
	 * 
	 * Note: a different accessor is needed for external calls --
	 * one that won't allocate on demand.
	 * 
	 * @return Returns the categoryStrings.
	 */
	protected ArrayList<String> evalCategoryStrings()
	{
		ArrayList<String> result	= categoryStrings;
		if (categoryStrings == null)
		{
			result			= new ArrayList<String>(5);
			categoryStrings	= result;
		}
		return result;
	}

	/**
	 * @return Returns the credit.
	 */
	public Credit getCredit()
	{
		return credit;
	}

	/**
	 * @param credit The credit to set.
	 */
	public void setCredit(Credit credit)
	{
		this.credit = credit;
	}

	/**
	 * @return Returns the description.
	 */
	public Description getDescription()
	{
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(Description description)
	{
		this.description = description;
	}

	/**
	 * @return Returns the height.
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * @param height The height to set.
	 */
	public void setHeight(int height)
	{
		this.height = height;
	}

	/**
	 * @return Returns the keywords.
	 */
	public String getKeywords()
	{
		return keywords;
	}

	/**
	 * @param keywords The keywords to set.
	 */
	public void setKeywords(String keywords)
	{
		this.keywords = keywords;
	}

	/**
	 * @return Returns the thumbnail.
	 */
	public Thumbnail getThumbnail()
	{
		return thumbnail;
	}

	/**
	 * @param thumbnail The thumbnail to set.
	 */
	public void setThumbnail(Thumbnail thumbnail)
	{
		this.thumbnail = thumbnail;
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
	 * @return Returns the type.
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * @return Returns the url.
	 */
	public ParsedURL getUrl()
	{
		return url;
	}

	/**
	 * @param url The url to set.
	 */
	public void setUrl(ParsedURL url)
	{
		this.url = url;
	}

	/**
	 * @return Returns the width.
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * @param width The width to set.
	 */
	public void setWidth(int width)
	{
		this.width = width;
	}

	
}
