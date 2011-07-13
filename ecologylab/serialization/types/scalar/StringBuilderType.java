/*
 * Created on Dec 31, 2004 at the Interface Ecology Lab.
 */
package ecologylab.serialization.types.scalar;

import java.io.IOException;

import ecologylab.serialization.ScalarUnmarshallingContext;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.XMLTools;

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
	 * @see ecologylab.serialization.types.scalar.ScalarType#getInstance(java.lang.String, String[], ScalarUnmarshallingContext)
	 */
	@Override public StringBuilder getInstance(String value, String[] formatStrings, ScalarUnmarshallingContext scalarUnmarshallingContext)
	{
		return new StringBuilder(value);
	}

	@Override
	public void appendValue(StringBuilder instance, StringBuilder buffy, boolean needsEscaping, TranslationContext serializationContext)
	{
		if (needsEscaping)
			XMLTools.escapeXML(buffy, instance);
		else
			buffy.append(instance);
	}
	public void appendValue(StringBuilder instance, Appendable appendable, boolean needsEscaping, TranslationContext serializationContext)
	throws IOException
	{
		if (needsEscaping)
			XMLTools.escapeXML(appendable, instance);
		else
			appendable.append(instance);

	}

	/**
	 * When editing, determines whether delimiters can be included in token strings.
	 * 
	 * @return
	 */
	// FIXME -- Add String delimitersAfter to TextChunk -- interleaved with TextTokens, and
	// get rid of this!!!
	public boolean allowDelimitersInTokens()
	{
		return true;
	}
	@Override
	public String getCSharptType()
	{
		return MappingConstants.DOTNET_STRING_BUILDER;
	}
	
	@Override
	public String getJavaType()
	{
		return MappingConstants.JAVA_STRING_BUILDER;
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
