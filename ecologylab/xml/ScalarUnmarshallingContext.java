/**
 * 
 */
package ecologylab.xml;

import java.io.File;

import ecologylab.net.ParsedURL;

/**
 * Provides context, such as source document location, that can be used for
 * unmarshalling scalar values, i.e., conversion from strings to typed representations.
 * 
 * @author andruid
 */
public interface ScalarUnmarshallingContext
{
	public ParsedURL purlContext();
	
	public File fileContext();
}
