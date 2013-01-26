package ecologylab.serialization.library.media;

import simpl.annotations.dbal.simpl_scalar;
import simpl.core.ElementState;
import ecologylab.net.ParsedURL;

/**
 * Thumbnail element. Optionally used to provide representative images
 * for a rich media object.
 *
 * @author andruid
 */
public class Thumbnail extends ElementState
{
	@simpl_scalar ParsedURL			url;
	@simpl_scalar  int					width;
	@simpl_scalar  int					height;
	
	
	/**
	 * @return the url
	 */
	public ParsedURL getUrl()
	{
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(ParsedURL url)
	{
		this.url = url;
	}
	/**
	 * @return the width
	 */
	public int getWidth()
	{
		return width;
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(int width)
	{
		this.width = width;
	}
	/**
	 * @return the height
	 */
	public int getHeight()
	{
		return height;
	}
	/**
	 * @param height the height to set
	 */
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	//time -- represents offset into time-based media object. for key frames.
	//http://www.ietf.org/rfc/rfc2326.txt
}
