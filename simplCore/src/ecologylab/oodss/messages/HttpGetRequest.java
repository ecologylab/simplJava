package ecologylab.oodss.messages;

import ecologylab.serialization.annotations.simpl_inherit;

/**
 * A message to be sent to a ServicesServer through HTTP that the Services will 
 * respond with an HTTP redirect URL based on whether there was an error or not.
 * 
 * @author andrew
 * @deprecated
 */

@Deprecated
public @simpl_inherit class HttpGetRequest extends HttpRequest 
{

}
