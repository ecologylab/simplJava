/**
 * 
 */
package ecologylab.serialization.library.feedburner;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.annotations.Hint;
import ecologylab.serialization.annotations.simpl_hints;
import ecologylab.serialization.annotations.simpl_scalar;

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
