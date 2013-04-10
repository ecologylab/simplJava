package simpl.formats.string;

import simpl.core.ISimplStringFormat;
import simpl.tools.XMLTools;

// TODO: May need to move to Platform Specifics in the future. 
public class XMLFormat implements ISimplStringFormat{

	@Override
	public String escape(String unescapedString) {
		// TODO: Replace with a standard library for escaping XML
		StringBuilder ourStringBuilder = new StringBuilder();
		XMLTools.escapeXML(ourStringBuilder, unescapedString); // TODO: Change this interface. My god that's terrible.
		return ourStringBuilder.toString();
	}

	@Override
	public String unescape(String escapedString) {
		//TODO: Replace with a standard library for escaping XML
		return XMLTools.unescapeXML(escapedString);
	}
}
