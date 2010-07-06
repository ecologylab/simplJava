/*
 * Created on Dec 31, 2004 at the Interface Ecology Lab.
 */
package ecologylab.xml.types.scalar;

import java.io.IOException;

import ecologylab.xml.ScalarUnmarshallingContext;
import ecologylab.xml.XMLTools;

/**
 * Type system entry for {@link java.lang.String String}. A very simple case.
 * 
 * @author andruid
 */
public class StringBuilderType extends ReferenceType<StringBuilder>
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
	 * @see ecologylab.xml.types.scalar.ScalarType#getInstance(java.lang.String, String[], ScalarUnmarshallingContext)
	 */
	@Override public StringBuilder getInstance(String value, String[] formatStrings, ScalarUnmarshallingContext scalarUnmarshallingContext)
	{
		return new StringBuilder(value);
	}

	@Override
	public void appendValue(StringBuilder instance, StringBuilder buffy, boolean needsEscaping)
	{
		if (needsEscaping)
			XMLTools.escapeXML(buffy, instance);
		else
			buffy.append(instance);
	}
	public void appendValue(StringBuilder instance, Appendable appendable, boolean needsEscaping)
	throws IOException
	{
		if (needsEscaping)
			XMLTools.escapeXML(appendable, instance);
		else
			appendable.append(instance);

	}

	@Override
	public String getCSharptType()
	{
		return MappingConstants.DOTNET_STRING_BUILDER;
	}

	@Override
	public String getDbType()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getObjectiveCType()
	{
		return MappingConstants.DOTNET_STRING_BUILDER;
	}
}
