package ecologylab.serialization.library.yahoo;

import simpl.annotations.dbal.Hint;
import simpl.annotations.dbal.simpl_hints;
import simpl.annotations.dbal.simpl_scalar;
import simpl.annotations.dbal.simpl_tag;
import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;

/**
 * Yahoo image search Thumbnail image XML description.
 *
 * @author andruid
 */
public 
@simpl_tag("Thumbnail")
class ThumbnailState extends ElementState
{
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	@simpl_tag("Url")		ParsedURL		url;
	
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	@simpl_tag("Width")	int				width;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	@simpl_tag("Height")	int				height;

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
