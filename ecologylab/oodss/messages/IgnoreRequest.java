package ecologylab.oodss.messages;

import ecologylab.collections.Scope;
import ecologylab.serialization.simpl_inherit;

/**
 * This is a message for the case when the server wants to ignore certain requests and send nothing to the client.
 * 
 * @author eunyee
 *
 */
@simpl_inherit
public class IgnoreRequest extends RequestMessage
{
	static final IgnoreRequest reusableInstance = new IgnoreRequest();

	@Override
	public ResponseMessage performService(Scope objectRegistry) 
	{
		return OkResponse.get();
	}
	
	public static IgnoreRequest get()
	{
		return reusableInstance;
	}
}