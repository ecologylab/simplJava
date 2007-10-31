/**
 * 
 */
package ecologylab.xml.types.scalar;

import ecologylab.xml.XmlTools;

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
	protected void appendValue(T instance, StringBuilder buffy, boolean needsEscaping)
    {
		String instanceString	= instance.toString();
		if (needsEscaping)
			XmlTools.escapeXML(buffy, instanceString);
		else
			buffy.append(instanceString);
    }
	
}
