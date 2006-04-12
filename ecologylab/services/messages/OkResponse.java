package ecologylab.services.messages;

/**
 * Base class for all ResponseMessages that were processed successfully.
 * 
 * @author andruid
 */
public class OkResponse extends ResponseMessage
{

	static final OkResponse reusableInstance	= new OkResponse();
	
	public OkResponse()
	{
		super();
	}

	public boolean isOK()
	{
		return true;
	}
	
	public static OkResponse get()
	{
		return reusableInstance;
	}
}
