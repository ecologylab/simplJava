/**
 * 
 */
package ecologylab.xml.library.feedburner;

import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;
import ecologylab.xml.Hint;
import ecologylab.xml.TranslationScope;

/**
 * @author andruid
 *
 */
public class Feedburner extends ElementState
{
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	ParsedURL	origLink;
	
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
	public static TranslationScope get()
	{
		return TranslationScope.get("feedburner", Feedburner.class);
	}

}
