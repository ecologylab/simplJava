/**
 * 
 */
package ecologylab.net;

import simpl.annotations.dbal.simpl_scalar;
import simpl.annotations.dbal.simpl_tag;
import simpl.types.element.IMappable;
import ecologylab.serialization.ElementState;

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

	@Override
	public String key()
	{
		return name;
	}
}
