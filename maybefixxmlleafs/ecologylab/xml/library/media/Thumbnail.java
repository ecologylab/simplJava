package ecologylab.xml.library.media;

import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;

/**
 * Thumbnail element. Optionally used to provide representatitve images
 * for a rich media object.
 *
 * @author andruid
 */
public class Thumbnail extends ElementState
{
	public ParsedURL			url;
	public int					width;
	public int					height;
	
	//time -- represents offset into time-based media object. for key frames.
	//http://www.ietf.org/rfc/rfc2326.txt
}
