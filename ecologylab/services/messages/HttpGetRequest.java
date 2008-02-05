package ecologylab.services.messages;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.net.ParsedURL;
import ecologylab.xml.xml_inherit;

/**
 * A message to be sent to a ServicesServer through HTTP that the Services will 
 * respond with an HTTP redirect URL based on whether there was an error or not.
 * 
 * @author andrew
 * @deprecated
 */

public @xml_inherit class HttpGetRequest extends HttpRequest 
{

}
