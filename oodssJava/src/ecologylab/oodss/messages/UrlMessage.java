package ecologylab.oodss.messages;

import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_scalar;
import ecologylab.collections.Scope;

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