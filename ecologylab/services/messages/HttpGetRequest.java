package ecologylab.services.messages;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.net.ParsedURL;
import ecologylab.xml.xml_inherit;

/**
 * A message to be sent to a ServicesServer through HTTP that the Services will 
 * respond with an HTTP redirect URL based on whether there was an error or not.
 * 
 * @author andrew
 */

public @xml_inherit class HttpGetRequest extends RequestMessage 
{
	/**
	 * Redirect URL if no errors or problems occured.
	 */
	protected @xml_attribute ParsedURL okResponseUrl;
	
	/**
	 * Redirect URL if an error or problem occured.
	 */
	protected @xml_attribute ParsedURL errorResponseUrl;

	@Override
	public ResponseMessage performService(ObjectRegistry objectRegistry, String sessionId) {
		// TODO Auto-generated method stub
		return OkResponse.get();
	}
	
	public ParsedURL okResponseUrl()
	{
		return okResponseUrl;
	}
	
	public ParsedURL errorResponseUrl()
	{
		return errorResponseUrl;
	}

}
