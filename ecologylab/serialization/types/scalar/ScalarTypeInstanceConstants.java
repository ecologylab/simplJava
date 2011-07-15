/**
 * 
 */
package ecologylab.serialization.types.scalar;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.types.ScalarType;
import ecologylab.serialization.types.TypeRegistry;

/**
 * Easy to access and use instances of popular ScalarTypes.
 * 
 * @author andruid
 *
 */
public interface ScalarTypeInstanceConstants
{

	public static final ScalarType<String> STRING_SCALAR_TYPE	= TypeRegistry.getScalarType(String.class);
	public static final ScalarType<ParsedURL> URL_SCALAR_TYPE	= TypeRegistry.getScalarType(ParsedURL.class);

}
