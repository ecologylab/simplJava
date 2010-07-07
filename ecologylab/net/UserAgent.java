/**
 * 
 */
package ecologylab.net;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.types.element.Mappable;

/**
 * @author awebb
 *
 */
public class UserAgent extends ElementState implements Mappable<String>
{
						@simpl_scalar 	String name;
	@xml_tag("string")	@simpl_scalar 	String userAgentString;
	@xml_tag("default") @simpl_scalar	boolean defaultAgent;
	
	public UserAgent() { }
	
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
