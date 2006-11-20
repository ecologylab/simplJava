/**
 * 
 */
package ecologylab.xml.feedburner;

import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;

/**
 * @author andruid
 *
 */
public class Feedburner extends ElementState
{
	@xml_leaf	ParsedURL	origLink;
	
	/**
	 * 
	 */
	public Feedburner()
	{
		super();
	}

	public ParsedURL getOrigLink()
	{
		return origLink;
	}

	public void setOrigLink(ParsedURL origLink)
	{
		this.origLink = origLink;
	}

}
