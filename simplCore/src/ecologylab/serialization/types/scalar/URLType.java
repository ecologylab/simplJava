/*
 * Created on Jan 2, 2005 at the Interface Ecology Lab.
 */
package ecologylab.serialization.types.scalar;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import ecologylab.serialization.ScalarUnmarshallingContext;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.types.CrossLanguageTypeConstants;

/**
 * Type system entry for {@link java.net.URL URL}.
 * This should almost never be used.
 * ParsedURLType is preferred!
 * 
 * @author andruid
 */
@simpl_inherit
public class URLType extends ReferenceType<URL>
implements CrossLanguageTypeConstants
{
	public URLType()
	{
		super(URL.class, JAVA_URL, DOTNET_URL, OBJC_URL, null);
	}

	/**
	 * @param value is interpreted as an absolute internet address.
	 * 
	 * @see ecologylab.serialization.types.ScalarType#getInstance(java.lang.String, String[], ScalarUnmarshallingContext)
	 */
	@Override
	public URL getInstance(String value, String[] formatStrings, ScalarUnmarshallingContext scalarUnmarshallingContext)
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
	@Override
	public Pattern delimitersTokenizer()
	{
		return ParsedURLType.URL_DELIMS_TOKENIZER;
	}
	@Override
	public String delimeters()
	{
		return ParsedURLType.URL_DELIMS;
	}
	
	/**
	 * The most basic and fundamental delimiter to use between characters.
	 * 
	 * @return	The URL implementation, here, returns a slash.
	 */
	@Override
	public String primaryDelimiter()
	{
		return ParsedURLType.PRIMARY_URL_DELIM;
	}
	
}
