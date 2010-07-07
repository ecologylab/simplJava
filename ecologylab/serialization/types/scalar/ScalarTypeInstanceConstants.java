/**
 * 
 */
package ecologylab.serialization.types.scalar;

import ecologylab.net.ParsedURL;

/**
 * Easy to access and use instances of popular ScalarTypes.
 * 
 * @author andruid
 *
 */
public interface ScalarTypeInstanceConstants
{

	public static final ScalarType<String> STRING_SCALAR_TYPE	= TypeRegistry.getType(String.class);
	public static final ScalarType<ParsedURL> URL_SCALAR_TYPE	= TypeRegistry.getType(ParsedURL.class);

}
