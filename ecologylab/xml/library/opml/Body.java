/**
 * 
 */
package ecologylab.xml.library.opml;

import ecologylab.xml.ElementState;
import ecologylab.xml.ElementState.xml_nested;

/**
 * OPML <body> element
 * 
 * @author andruid
 */
public class Body extends ElementState
{
	@xml_nested	Outline		outline;
	
	/**
	 * 
	 */
	public Body()
	{
		super();

	}

}
