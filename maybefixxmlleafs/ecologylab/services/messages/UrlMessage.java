package ecologylab.services.messages;


import ecologylab.appframework.ObjectRegistry;
import ecologylab.xml.xml_inherit;

@xml_inherit
public class UrlMessage extends RequestMessage
{
	@xml_attribute protected String url = "";
//	@xml_attribute protected	ParsedURL	purl;
	@xml_attribute protected	String collection = "";


	public @Override ResponseMessage performService(ObjectRegistry objectRegistry) 
	{
//		String datastore = HTTPPostServer.datastore + collection +"//"+(new Date()).getTime() + "//";
//		HTMLDom.downloadHTMLPage(url, collection, datastore);
		return OkResponse.get();
	}
	
}