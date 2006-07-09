package ecologylab.xml.media;

import ecologylab.xml.ElementState;

/**
 * Media leaf node, but not marked up as such cause it has its own attribute.
 *
 * @author andruid
 */
public class Description extends ElementState
{
/**
 * Can be plain or html. Plain is the default.
 */
	String			type;
	
	/**
	 * A text node will be here with the actual description.
	 */
}
