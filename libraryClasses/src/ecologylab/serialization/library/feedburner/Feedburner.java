/**
 * 
 */
package ecologylab.serialization.library.feedburner;

import simpl.annotations.dbal.Hint;
import simpl.annotations.dbal.simpl_hints;
import simpl.annotations.dbal.simpl_scalar;
import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.SimplTypesScope;

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
	public static SimplTypesScope get()
	{
		return SimplTypesScope.get("feedburner", Feedburner.class);
	}

}
