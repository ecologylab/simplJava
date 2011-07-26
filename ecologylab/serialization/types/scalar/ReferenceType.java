/**
 * 
 */
package ecologylab.serialization.types.scalar;

import java.io.IOException;

import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.XMLTools;
import ecologylab.serialization.simpl_inherit;
import ecologylab.serialization.types.ScalarType;

/**
 * All non-primitive ScalarType subclasses should extend this class.
 * 
 * @author andruid
 */
@simpl_inherit
abstract public class ReferenceType<T> extends ScalarType<T>
{

	/**
	 * @param thatClass
	 * @param javaTypeName TODO
	 * @param cSharpTypeName TODO
	 * @param objectiveCTypeName TODO
	 * @param dbTypeName TODO
	 */
	public ReferenceType(Class<T> thatClass, String javaTypeName, String cSharpTypeName, String objectiveCTypeName, String dbTypeName)
	{
		super(thatClass, cSharpTypeName, objectiveCTypeName, dbTypeName);
	}

	/**
	 * Append the String directly, unless it needs escaping, in which case, call escapeXML.
	 * 
	 * @param instance
	 * @param buffy
	 * @param needsEscaping
	 */
	@Override
	public void appendValue(T instance, StringBuilder buffy, boolean needsEscaping, TranslationContext serializationContext)
	{
		String instanceString = marshall(instance, serializationContext);// instance.toString();
		if (needsEscaping)
			XMLTools.escapeXML(buffy, instanceString);
		else
			buffy.append(instanceString);
	}

	@Override
	public void appendValue(T instance, Appendable buffy, boolean needsEscaping, TranslationContext serializationContext) throws IOException
	{
		String instanceString = marshall(instance, serializationContext); // andruid 1/4/10 instance.toString();
		if (needsEscaping)
			XMLTools.escapeXML(buffy, instanceString);
		else
			buffy.append(instanceString);
	}

}
