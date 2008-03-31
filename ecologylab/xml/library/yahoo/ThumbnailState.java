package ecologylab.xml.library.yahoo;

import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;
import ecologylab.xml.ElementState.xml_leaf;
import ecologylab.xml.ElementState.xml_tag;

/**
 * Yahoo image search Thumbnail image XML description.
 *
 * @author andruid
 */
public 
@xml_tag("Thumbnail")
class ThumbnailState extends ElementState
{
	@xml_leaf	@xml_tag("Url")		ParsedURL		url;
	
	@xml_leaf	@xml_tag("Width")	int				width;
	@xml_leaf	@xml_tag("Height")	int				height;

	public ParsedURL getUrl()
	{
		return url;
	}

	public void setUrl(ParsedURL url)
	{
		this.url = url;
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}
}
