/**
 * 
 */
package ecologylab.services.messages.cf;

import ecologylab.xml.ElementState.xml_leaf;

/**
 * 
 *
 * @author andruid 
 */
public class InlineSeed extends Seed
{
	@xml_leaf(CDATA) protected String			content;

	/**
	 * 
	 */
	public InlineSeed()
	{
		// TODO Auto-generated constructor stub
	}

}
