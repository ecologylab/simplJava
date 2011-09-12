/**
 * 
 */
package ecologylab.net;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;
import ecologylab.serialization.types.element.IMappable;

/**
 * @author awebb
 * 
 */
public class UserAgent extends ElementState implements IMappable<String>
{
	@simpl_scalar
	String	name;

	@simpl_tag("string")
	@simpl_scalar
	String	userAgentString;

	@simpl_tag("default")
	@simpl_scalar
	boolean	defaultAgent;

	public UserAgent()
	{
	}
	
	public String name()
	{
		return name;
	}

	public String userAgentString()
	{
		return userAgentString;
	}

	public boolean isDefaultAgent()
	{
		return defaultAgent;
	}

	public String key()
	{
		return name;
	}
}
