package ecologylab.oodss.messages;

import ecologylab.serialization.simpl_inherit;

/**
 * Base class for all ResponseMessages that were processed successfully.
 * 
 * @author andruid
 */
@simpl_inherit
public class OkResponse extends ResponseMessage
{
	public static final OkResponse reusableInstance	= new OkResponse();
	
	public OkResponse()
	{
		super();
	}

	@Override public boolean isOK()
	{
		return true;
	}
	
	public static OkResponse get()
	{
		return reusableInstance;
	}
}
