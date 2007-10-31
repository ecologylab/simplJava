/*
 * Created on Dec 31, 2004 at the Interface Ecology Lab.
 */
package ecologylab.xml.types.scalar;

/**
 * Type system entry for {@link java.lang.String String}. A very simple case.
 * 
 * @author andruid
 */
public class StringBuilderType extends ScalarType<StringBuilder>
{
/**
 * This constructor should only be called once per session, through
 * a static initializer, typically in TypeRegistry.
 * <p>
 * To get the instance of this type object for use in translations, call
 * <code>TypeRegistry.get("java.lang.String")</code>.
 * 
 */
	public StringBuilderType()
	{
		super(StringBuilder.class);
	}

	/**
	 * Return the value wrapped inside a StringBuilder. A call to avoid!
	 * 
	 * @see ecologylab.xml.types.scalar.ScalarType#getInstance(java.lang.String)
	 */
	@Override public StringBuilder getInstance(String value)
	{
		return new StringBuilder(value);
	}

	@Override protected void appendValue(StringBuilder instance, StringBuilder buffy, boolean needsEscaping)
    {
    	buffy.append(instance);
    }

}
