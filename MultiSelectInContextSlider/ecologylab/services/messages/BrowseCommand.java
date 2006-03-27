package ecologylab.services.messages;

import ecologylab.xml.ElementState;

/**
 * A message sent to the browser server to utilize services
 * 
 * @author blake
 */
public class BrowseCommand extends ElementState 
{
	public String name;
	public String argument;
	
	public BrowseCommand() {}
	
	public BrowseCommand(String name, String argument)
	{
		this.name 		= name;
		this.argument 	= argument;
	}
}
