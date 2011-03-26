package ecologylab.serialization.library.yahoo;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.ElementState.xml_tag;
import ecologylab.serialization.Hint;

/**
 * Yahoo image search Thumbnail image XML description.
 *
 * @author andruid
 */
public 
@xml_tag("Thumbnail")
class ThumbnailState extends ElementState
{
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	@xml_tag("Url")		ParsedURL		url;
	
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	@xml_tag("Width")	int				width;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	@xml_tag("Height")	int				height;

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
