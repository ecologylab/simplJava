package ecologylab.serialization.library.media;

import java.util.ArrayList;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.Hint;

/**
 * Primary element of the media XML name space. As in <media:content>
 *
 * @author andruid
 */
public class Content extends ElementState
{
	@simpl_scalar	ParsedURL		url;
	@simpl_scalar	String			type;
	@simpl_scalar	int				width;
	@simpl_scalar	int				height;
	
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)		String			title;
	@simpl_composite 	Description		description;
	@simpl_scalar	String			keywords;
	@simpl_composite 	Thumbnail		thumbnail;
	
	// there can be 0 or more elements of tag "category"
	// we will add these to a collection automatically by overriding setField(Field, String)
	//TODO confirm if this is correct.
	@simpl_collection	ArrayList<String>		categoryStrings;
	
	@simpl_composite		Credit			credit;
	
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
