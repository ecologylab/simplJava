/*
 * Created on Dec 31, 2004 at the Interface Ecology Lab.
 */
package cm.generic;

import ecologylab.types.Type;

/**
 * Type system entry for java.awt.Color. Uses a hex string as initialization.
 * 
 * @author andruid
 */
public class ParsedURLType extends Type
{
	public ParsedURLType()
	{
		super("cm.generic.ParsedURL", /*TYPE_PARSED_URL, */ false);
	}

	/**
	 * @param value is interpreted as hex-encoded RGB value, in the
	 * same style as HTML & CSS. A # character at the start is unneccesary,
	 * but acceptable.
	 * 
	 * @see ecologylab.types.Type#getInstance(java.lang.String)
	 */
	public Object getInstance(String value)
	{
	   return ParsedURL.getAbsolute(value, " getInstance()");
	}
}
