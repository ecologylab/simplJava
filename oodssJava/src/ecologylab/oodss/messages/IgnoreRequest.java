package ecologylab.oodss.messages;

import simpl.annotations.dbal.simpl_inherit;
import ecologylab.collections.Scope;

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