package ecologylab.services.messages.studies;

import netscape.javascript.JSObject;
import ecologylab.generic.ObjectRegistry;
import ecologylab.services.messages.OkResponse;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;

public class ReadyMessage extends RequestMessage 
{
	public ResponseMessage performService(ObjectRegistry objectRegistry) 
	{
		JSObject jsWindow = (JSObject) objectRegistry.lookupObject("jsWindow");
		
		jsWindow.eval("parent.core.startTimer();");
		//jsWindow.eval("parent.focus();");
		
		return OkResponse.get();
	}

}
