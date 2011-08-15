package ecologylab.oodss.messages;


import ecologylab.collections.Scope;
import ecologylab.serialization.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;

@simpl_inherit
public class UrlMessage extends RequestMessage
{
	@simpl_scalar protected String url = "";
//	@xml_attribute protected	ParsedURL	purl;
	@simpl_scalar protected	String collection = "";


	public @Override ResponseMessage performService(Scope objectRegistry) 
	{
//		String datastore = HTTPPostServer.datastore + collection +"//"+(new Date()).getTime() + "//";
//		HTMLDom.downloadHTMLPage(url, collection, datastore);
		return OkResponse.get();
	}
	
}