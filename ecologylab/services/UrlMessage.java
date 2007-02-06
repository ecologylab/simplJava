package ecologylab.services;


import java.util.Date;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.net.ParsedURL;
import ecologylab.services.messages.OkResponse;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.xml_inherit;
import ecologylab.xml.ElementState.xml_attribute;

@xml_inherit
public class UrlMessage extends RequestMessage
{
	@xml_attribute protected String url = "";
//	@xml_attribute protected	ParsedURL	purl;
	@xml_attribute protected	String collection = "";


	public @Override ResponseMessage performService(ObjectRegistry objectRegistry) 
	{
		String datastore = HTTPPostServer.datastore + collection +"//"+(new Date()).getTime() + "//";
//		HTMLDom.downloadHTMLPage(url, collection, datastore);
		return OkResponse.get();
	}
	
}