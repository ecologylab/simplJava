package ecologylab.services.messages;

/**
 * Base class for all ResponseMessages that were processed successfully.
 * 
 * @author andruid
 */
public class OKResponse extends ResponseMessage
{

	static final OKResponse reusableInstance	= new OKResponse();
	
	public OKResponse()
	{
		super();
	}

	public boolean isOK()
	{
		return true;
	}
	
	public static OKResponse get()
	{
		return reusableInstance;
	}
}
