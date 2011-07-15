/**
 * 
 */
package ecologylab.serialization.types.scalar;

import java.io.IOException;

import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.XMLTools;
import ecologylab.serialization.types.ScalarType;

/**
 * 
 * @author andruid
 */
abstract public class ReferenceType<T> extends ScalarType<T>
{

	/**
	 * @param thatClass
	 */
	public ReferenceType(Class<T> thatClass)
	{
		super(thatClass);
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

	
	public void appendValue(T instance, Appendable buffy, boolean needsEscaping, TranslationContext serializationContext) throws IOException
	{
		String instanceString = marshall(instance, serializationContext); // andruid 1/4/10 instance.toString();
		if (needsEscaping)
			XMLTools.escapeXML(buffy, instanceString);
		else
			buffy.append(instanceString);
	}

}
