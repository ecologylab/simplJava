/**
 * 
 */
package ecologylab.xml.library.feedburner;

import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationSpace;

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
	public static TranslationSpace get()
	{
		return TranslationSpace.get("feedburner", Feedburner.class);
	}

}
