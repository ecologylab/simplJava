/**
 * 
 */
package ecologylab.net;

import ecologylab.xml.ElementState;
import ecologylab.xml.types.element.Mappable;

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
