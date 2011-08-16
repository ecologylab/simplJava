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
import ecologylab.serialization.types.CrossLanguageTypeConstants;

/**
 * Type system entry for {@link java.lang.String String}. A very simple case.
 * 
 * @author andruid
 */
@simpl_inherit
public class StringType extends ReferenceType<String> implements CrossLanguageTypeConstants
{
	/**
	 * This constructor should only be called once per session, through a static initializer,
	 * typically in TypeRegistry.
	 * <p>
	 * To get the instance of this type object for use in translations, call
	 * <code>TypeRegistry.get("java.lang.String")</code>.
	 * 
	 */
	public StringType()
	{
		super(String.class, JAVA_STRING, DOTNET_STRING, OBJC_STRING, null);
	}

	/**
	 * Just return the value itself. A transparent pass-through.
	 * 
	 * @see ecologylab.serialization.types.ScalarType#getInstance(java.lang.String, String[],
	 *      ScalarUnmarshallingContext)
	 */
	@Override
	public String getInstance(String value, String[] formatStrings,
			ScalarUnmarshallingContext scalarUnmarshallingContext)
	{
		return value;
	}

	/**
	 * Get a String representation of the instance, which is simply this.
	 * 
	 * @param instance
	 * @return
	 */
	@Override
	public String marshall(String instance, TranslationContext serializationContext)
	{
		return instance;
	}

	/**
	 * Append the String directly, unless it needs escaping, in which case, call escapeXML.
	 * 
	 * @param instance
	 * @param buffy
	 * @param needsEscaping
	 */
	@Override
	public void appendValue(String instance, StringBuilder buffy, boolean needsEscaping,
			TranslationContext serializationContext)
	{

		if (needsEscaping)
			XMLTools.escapeXML(buffy, instance);
		else
			buffy.append(instance);
	}

	/**
	 * Append the String directly, unless it needs escaping, in which case, call escapeXML.
	 * 
	 * @param instance
	 * @param appendable
	 * @param needsEscaping
	 * @throws IOException
	 */
	@Override
	public void appendValue(String instance, Appendable appendable, boolean needsEscaping,
			TranslationContext serializationContext, FORMAT format) throws IOException
	{
		if (needsEscaping)
		{
			switch (format)
			{
			case JSON:
				appendable.append(JSONObject.escape(instance));
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
