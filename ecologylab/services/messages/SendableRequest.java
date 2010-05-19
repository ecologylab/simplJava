/**
 * 
 */
package ecologylab.services.messages;

import ecologylab.xml.XMLTranslationException;

/**
 * Interface to indicate that a message can be sent over the network. Used for type checking in
 * client send methods, to allow other, more specific interfaces to be used (such as
 * AuthenticationRequest).
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 * 
 */
public interface SendableRequest
{

	/**
	 * @param requestBuffer
	 */
	StringBuilder translateToXML(StringBuilder requestBuffer)	throws XMLTranslationException;

	/**
	 * @return
	 */
	boolean isDisposable();

}
