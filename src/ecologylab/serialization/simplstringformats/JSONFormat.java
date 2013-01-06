package ecologylab.serialization.simplstringformats;

import org.json.simple.JSONValue;

import ecologylab.serialization.ISimplStringFormat;

// TODO: MAY need to move to platform specifics. :\ 
public class JSONFormat implements ISimplStringFormat{

	@Override
	public String escape(String unescapedString) {
		if(unescapedString == null)
		{
			return "";
		}
		
		// TODO: CONSIDER CHANGING THIS TO ANOTHER LIBRARY? 
		return JSONValue.escape(unescapedString); // This is what the String scalar type does to escape. 
		// Not sure if this is sufficient,b ut we've been relying on it thus far at least for strings.
	}

	@Override
	public String unescape(String escapedString) {
		if(escapedString == null)
		{
			return "";
		}
		
		try
		{
			// We add the "'s to make it parse-able as a string. 
			// This could be improved, works for now.
			Object o = JSONValue.parseWithException("\""+escapedString+"\"");
			return o.toString();
		}
		catch(Exception e)
		{
			throw new RuntimeException("Couldn't unescape the string:" + escapedString);
		}
	}

}
