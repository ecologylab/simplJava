/*
 * Created on Jan 2, 2005 at the Interface Ecology Lab.
 */
package ecologylab.xml.types.scalar;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Type system entry for {@link java.net.URL URL}.
 * This should almost never be used.
 * ParsedURLType is preferred!
 * 
 * @author andruid
 */
public class URLType extends ScalarType<URL>
{
	public URLType()
	{
		super(URL.class);
	}

	/**
	 * @param value is interpreted as an absolute internet address.
	 * 
	 * @see ecologylab.xml.types.scalar.ScalarType#getInstance(java.lang.String)
	 */
	public URL getInstance(String value)
	{
	   URL result	= null;
	   try
	   {
		  result		= new URL(value);
	   } catch (MalformedURLException e)
	   {
		   debug("Got " + e + " while setting getting instance of URL from "
				 + value);
	   }
	   return result;
	}
	/**
	 * For editing: these are the valid delimiters for separating tokens that make up a field
	 * of this type.
	 * 
	 * @return
	 */
	public String delimeters()
	{
		return ParsedURLType.URL_DELIMS;
	}
	
	
}
