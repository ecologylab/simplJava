package ecologylab.services.messages;

import java.util.Hashtable;

import ecologylab.collections.Scope;
import ecologylab.xml.Hint;
import ecologylab.xml.simpl_inherit;

@simpl_inherit
public class ContinuedHTTPGetRequest extends HttpRequest
{
	@simpl_scalar @simpl_hints(Hint.XML_LEAF_CDATA)	String	messageFragment;
	
	@simpl_scalar		boolean	isLast;
	
	static final		Hashtable<String, String>	partialMessages	= new Hashtable<String, String>();
	
	public ContinuedHTTPGetRequest()
	{
		super();
	}

	@Override
	public ResponseMessage performService(Scope objectRegistry) 
	{
		if (!isLast)
		{
			// concatenate fragments into Hashtable entry. for now, key is IP number
			// in a later revision, key may be a UID we generate and pass back to the caller
		}
		else
		{
			// concatenate latest messageFragment with enty in the Hashtable for this IP number
			
			// translate this String into XML and call performService().
		}
		return OkResponse.get();
	}

}
