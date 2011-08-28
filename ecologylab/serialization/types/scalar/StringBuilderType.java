/*
 * Created on Dec 31, 2004 at the Interface Ecology Lab.
 */
package ecologylab.serialization.types.scalar;

import java.io.IOException;

import org.json.simple.JSONObject;

import ecologylab.serialization.ScalarUnmarshallingContext;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.XMLTools;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.serializers.Format;
import ecologylab.serialization.types.CrossLanguageTypeConstants;

/**
 * Type system entry for {@link java.lang.String String}. A very simple case.
 * 
 * @author andruid
 */
@simpl_inherit
public class StringBuilderType extends ReferenceType<StringBuilder> implements
		CrossLanguageTypeConstants
{
	/**
	 * This constructor should only be called once per session, through a static initializer,
	 * typically in TypeRegistry.
	 * <p>
	 * To get the instance of this type object for use in translations, call
	 * <code>TypeRegistry.get("java.lang.String")</code>.
	 * 
	 */
	public StringBuilderType()
	{
		super(StringBuilder.class, JAVA_STRING_BUILDER, DOTNET_STRING_BUILDER, OBJC_STRING_BUILDER,
				null);
	}

	/**
	 * Return the value wrapped inside a StringBuilder. A call to avoid!
	 * 
	 * @see ecologylab.serialization.types.ScalarType#getInstance(java.lang.String, String[],
	 *      ScalarUnmarshallingContext)
	 */
	@Override
	public StringBuilder getInstance(String value, String[] formatStrings,
			ScalarUnmarshallingContext scalarUnmarshallingContext)
	{
		return new StringBuilder(value);
	}

	@Override
	public void appendValue(StringBuilder instance, StringBuilder buffy, boolean needsEscaping,
			TranslationContext serializationContext)
	{
		if (needsEscaping)
			XMLTools.escapeXML(buffy, instance);
		else
			buffy.append(instance);
	}

	@Override
	public void appendValue(StringBuilder instance, Appendable appendable, boolean needsEscaping,
			TranslationContext serializationContext, Format format) throws IOException
	{
		if (needsEscaping)
		{
			switch (format)
			{
			case JSON:
				appendable.append(JSONObject.escape(instance.toString()));
				break;
			case XML:
				XMLTools.escapeXML(appendable, instance);
				break;
			default:
				XMLTools.escapeXML(appendable, instance);
				break;
			}

		}
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

}
