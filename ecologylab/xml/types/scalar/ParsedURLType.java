/*
 * Created on Dec 31, 2004 at the Interface Ecology Lab.
 */
package ecologylab.xml.types.scalar;

import java.io.File;

import ecologylab.net.ParsedURL;

/**
 * Type system entry for java.awt.Color. Uses a hex string as initialization.
 * 
 * @author andruid
 */
public class ParsedURLType extends ScalarType<ParsedURL>
{
/**
 * This constructor should only be called once per session, through
 * a static initializer, typically in TypeRegistry.
 * <p>
 * To get the instance of this type object for use in translations, call
 * <code>TypeRegistry.get("cm.generic.ParsedURL")</code>.
 * 
 */
	public ParsedURLType()
	{
		super(ParsedURL.class);
	}

	/**
	 * Looks for file in value, and creates a ParsedURL with file set if appropriate.
	 * Otherwise, calls ParsedURL.getAbsolute().
	 * 
	 * @param value 	String to marshall into a typed instance.
	 * 
	 * @see ecologylab.xml.types.scalar.ScalarType#getInstance(java.lang.String)
	 */
	public ParsedURL getInstance(String value)
	{
	   File file	= null;
	   if (value.startsWith("file://"))
	   {
		   int startIndex	= value.startsWith("file:///") ? 8 : 7;
		   value	= value.substring(startIndex);
		   file		= ecologylab.io.Files.newFile(value);
	   }
	   else if (value.indexOf(':') == 1)
	   {
		   file		= ecologylab.io.Files.newFile(value);
	   }
	   return (file != null) ? new ParsedURL(file)
		   : ParsedURL.getAbsolute(value, " getInstance()");
	}
}
