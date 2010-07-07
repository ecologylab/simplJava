package ecologylab.services.messages;


import ecologylab.collections.Scope;
import ecologylab.xml.simpl_inherit;

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