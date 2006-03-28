package ecologylab.services.messages;

import java.applet.AppletContext;
import java.net.MalformedURLException;
import java.net.URL;

import netscape.javascript.JSObject;

import ecologylab.generic.Debug;
import ecologylab.generic.ObjectRegistry;

/**
 * A message sent to the browser server to utilize services
 * 
 * @author blake
 */
public class BrowseCommand 
extends RequestMessage
implements MessageTypes
{
	public String name;
	public String argument;
	
	public BrowseCommand() {}
	
	public BrowseCommand(String name, String argument)
	{
		this.name 		= name;
		this.argument 	= argument;
	}

	public ResponseMessage performService(ObjectRegistry objectRegistry)
	{
		if (name == null)
		{
			System.err.println("The BrowserServer requires a command");
			return ResponseMessage.BADResponse();
		}
		if (name.equals(NAVIGATE))
		{
			if (argument == null)
			{
				System.err.println("Incoming \"navigate\" packets require a URL argument");
				return ResponseMessage.BADResponse();
			}
			
			try
			{
				//grab the objects we need.
			    AppletContext 	appletContext 	= (AppletContext) objectRegistry.lookupObject("appletContext");
			    JSObject 		jsWindow		= (JSObject) objectRegistry.lookupObject("jsWindow");
			    String			goPrefix		= (String) objectRegistry.lookupObject("goPrefix");
			    String			frame			= (String) objectRegistry.lookupObject("frame");
				
			    if (jsWindow != null)
			    {
//			    	create a URL so that we KNOW the address is well-formed.
			    	URL url = new URL(argument);
				   String purlString			= url.toString();
				   Debug.println("!!!use JSObject to navigate to " + purlString);
				   jsWindow.call("cmPopup", new Object[] {purlString});
			    }
			    else if (appletContext != null)
			    {
	//			          if (goPrefix != null)	   // popup.html?href=
	//			     		 purl = ParsedURL.getRelative((relToDoc? docBaseURL : codeBaseURL), (goPrefix+purl),
	//			     									   "Cant find " + (goPrefix+purl) +
	//			     									   " relative to " + docBaseURL.toExternalForm() + " "); 		 Debug.println("Playlet.go() USING popup.html cause JSObject=null");
			    	URL url = new URL(goPrefix + argument);
					Debug.println("!!!use goPrefix to navigate to " + url);
					appletContext.showDocument(url, frame);
			    }
	
				System.out.println("finished navigating");
			}
			catch (MalformedURLException e)
			{
				System.err.println("BrowserServer: Malformed URL received: " + argument);
				e.printStackTrace();
			}
			catch (Exception e)
			{
				System.out.println("BrowserServer error: ");
				e.printStackTrace();
			}
		}
		return ResponseMessage.OKResponse();
	}
}
