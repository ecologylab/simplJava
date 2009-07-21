package ecologylab.services.messages;

import java.util.Hashtable;

import ecologylab.collections.Scope;
import ecologylab.xml.xml_inherit;

@xml_inherit
public class ContinuedHTTPGetRequest extends HttpRequest
{
	@xml_leaf(CDATA)	String	messageFragment;
	
	@xml_attribute		boolean	isLast;
	
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
