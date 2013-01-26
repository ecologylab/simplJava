/**
 * 
 */
package ecologylab.serialization.library.feedburner;

import simpl.annotations.dbal.Hint;
import simpl.annotations.dbal.simpl_hints;
import simpl.annotations.dbal.simpl_scalar;
import simpl.core.ElementState;
import simpl.core.SimplTypesScope;
import ecologylab.net.ParsedURL;

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
