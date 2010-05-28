/**
 * 
 */
package ecologylab.services.messages.cf;

import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;
import ecologylab.xml.ElementState.xml_attribute;

/**
 * Specification of a directive to the agent or otherwise to compositon space services.
 * 
 * Version for client only:
 * 	<li>data slot definitions only with no other functionality.</li>
 * 
 * @author andruid
 */
public class Crawler extends Seed
{
	/**
	 * URL that the action operates on.
	 */
	@xml_attribute
	protected					ParsedURL	url;
	
	/**
	 * The domain -- for reject actions only.
	 */
	@xml_attribute
	protected					String		domain;
	
	/**
	 * What is the web crawler being told to do?
	 * 		traversable, untraversable, or reject.
	 */
	@xml_attribute
	protected					String		action;

	/**
	 * @return the url
	 */
	public ParsedURL getUrl()
	{
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(ParsedURL url)
	{
		this.url = url;
	}

	/**
	 * @return the domain
	 */
	public String getDomain()
	{
		return domain;
	}

	/**
	 * @param domain the domain to set
	 */
	public void setDomain(String domain)
	{
		this.domain = domain;
	}

	/**
	 * @return the action
	 */
	public String getAction()
	{
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action)
	{
		this.action = action;
	}
	
}
