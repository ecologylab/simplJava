package ecologylab.services.messages;

import ecologylab.xml.xml_inherit;

/**
 * Base class for all ResponseMessages that were processed successfully.
 * 
 * @author andruid
 */
@xml_inherit
public class OkResponse extends ResponseMessage
{
	public static final OkResponse reusableInstance	= new OkResponse();
	
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
