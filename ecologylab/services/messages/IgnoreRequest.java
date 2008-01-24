package ecologylab.services.messages;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.xml.xml_inherit;

/**
 * This is a message for the case when the server wants to ignore certain requests and send nothing to the client.
 * 
 * @author eunyee
 *
 */
@xml_inherit
public class IgnoreRequest extends RequestMessage
{
	static final IgnoreRequest reusableInstance = new IgnoreRequest();

	@Override
	public ResponseMessage performService(ObjectRegistry objectRegistry, String sessionId) 
	{
		return OkResponse.get();
	}
	
	public static IgnoreRequest get()
	{
		return reusableInstance;
	}
}