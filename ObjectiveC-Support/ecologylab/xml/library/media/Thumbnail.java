package ecologylab.xml.library.media;

import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;

/**
 * Thumbnail element. Optionally used to provide representative images
 * for a rich media object.
 *
 * @author andruid
 */
public class Thumbnail extends ElementState
{
	@xml_attribute ParsedURL			url;
	@xml_attribute  int					width;
	@xml_attribute  int					height;
	
	
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
