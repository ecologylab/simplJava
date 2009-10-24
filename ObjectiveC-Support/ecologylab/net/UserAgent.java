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
						@xml_attribute 	String name;
	@xml_tag("string")	@xml_attribute 	String userAgentString;
	@xml_tag("default") @xml_attribute	boolean defaultAgent;
	
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
