/**
 * 
 */
package ecologylab.oodss.messages;


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

//	/**
//	 * @param requestBuffer
//	 */
//	StringBuilder serialize(StringBuilder requestBuffer)	throws SIMPLTranslationException;

	/**
	 * @return
	 */
	boolean isDisposable();

}
