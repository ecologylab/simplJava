/*
 * Created on Jan 2, 2005 at the Interface Ecology Lab.
 */
package ecologylab.xml.types.scalar;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Type system entry for {@link java.net.URL URL}.
 * 
 * @author andruid
 */
public class URLType extends Type 
{
	public URLType()
	{
		super(URL.class);
	}

	/**
	 * @param value is interpreted as an absolute internet address.
	 * 
	 * @see ecologylab.xml.types.scalar.Type#getInstance(java.lang.String)
	 */
	public Object getInstance(String value)
	{
	   Object result	= null;
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
}
