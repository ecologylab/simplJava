package ecologylab.serialization.deserializers.pullhandlers;

import java.io.File;

import ecologylab.generic.Debug;
import ecologylab.net.ParsedURL;
import ecologylab.serialization.BinaryFormat;
import ecologylab.serialization.DeserializationHookStrategy;
import ecologylab.serialization.FieldTypes;
import ecologylab.serialization.Format;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.ScalarUnmarshallingContext;
import ecologylab.serialization.StringFormat;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.deserializers.pullhandlers.binaryformats.BinaryPullDeserializer;
import ecologylab.serialization.deserializers.pullhandlers.stringformats.JSONPullDeserializer;
import ecologylab.serialization.deserializers.pullhandlers.stringformats.StringPullDeserializer;
import ecologylab.serialization.deserializers.pullhandlers.stringformats.XMLPullDeserializer;

public abstract class PullDeserializer extends Debug implements ScalarUnmarshallingContext,
		FieldTypes
{

	protected TranslationScope						translationScope;

	protected TranslationContext					translationContext;

	protected DeserializationHookStrategy	deserializationHookStrategy;

	/**
	 * Constructs that creates a JSON deserialization handler
	 * 
	 * @param translationScope
	 *          translation scope to use for de/serializing subsequent char sequences
	 * @param translationContext
	 *          used for graph handling
	 */
	public PullDeserializer(TranslationScope translationScope, TranslationContext translationContext)
	{
		this.translationScope = translationScope;
		this.translationContext = translationContext;
		this.deserializationHookStrategy = null;
	}

	/**
	 * Constructs that creates a JSON deserialization handler
	 * 
	 * @param translationScope
	 *          translation scope to use for de/serializing subsequent char sequences
	 * @param translationContext
	 *          used for graph handling
	 */
	public PullDeserializer(TranslationScope translationScope, TranslationContext translationContext,
			DeserializationHookStrategy deserializationHookStrategy)
	{
		this.translationScope = translationScope;
		this.translationContext = translationContext;
		this.deserializationHookStrategy = deserializationHookStrategy;
	}

	/**
	 * The main parse method accepts a CharSequence and creates a corresponding object model. Sets up
	 * the root object and creates instances of the root object before calling a recursive method that
	 * creates the complete object model
	 * 
	 * @param charSequence
	 * @return
	 * @throws SIMPLTranslationException
	 */
	public abstract Object parse(File file);

	/**
	 * The main parse method accepts a CharSequence and creates a corresponding object model. Sets up
	 * the root object and creates instances of the root object before calling a recursive method that
	 * creates the complete object model
	 * 
	 * @param charSequence
	 * @return
	 * @throws SIMPLTranslationException
	 */
	public abstract Object parse(ParsedURL purl);

	public static PullDeserializer getDeserializer(TranslationScope translationScope,
			TranslationContext translationContext, Format format) throws SIMPLTranslationException
	{
		return getDeserializer(translationScope, translationContext, null, format);
	}

	public static PullDeserializer getDeserializer(TranslationScope translationScope,
			TranslationContext translationContext,
			DeserializationHookStrategy deserializationHookStrategy, Format format)
			throws SIMPLTranslationException
	{
		switch (format)
		{
		case XML:
		case JSON:
			return new JSONPullDeserializer(translationScope, translationContext,
					deserializationHookStrategy);
		case TLV:
		case BIBTEX:
		default:
			throw new SIMPLTranslationException(format + " format not supported");
		}
	}

	public static StringPullDeserializer getStringDeserializer(TranslationScope translationScope,
			TranslationContext translationContext, StringFormat stringFormat)
			throws SIMPLTranslationException
	{
		return getStringDeserializer(translationScope, translationContext, null, stringFormat);
	}

	public static StringPullDeserializer getStringDeserializer(TranslationScope translationScope,
			TranslationContext translationContext,
			DeserializationHookStrategy deserializationHookStrategy, StringFormat stringFormat)
			throws SIMPLTranslationException
	{
		switch (stringFormat)
		{
		case XML:
			return new XMLPullDeserializer(translationScope, translationContext,
					deserializationHookStrategy);
		case JSON:
			return new JSONPullDeserializer(translationScope, translationContext,
					deserializationHookStrategy);
		case BIBTEX:
		default:
			throw new SIMPLTranslationException(stringFormat + " format not supported");
		}
	}

	public static BinaryPullDeserializer getBinaryDeserializer(TranslationScope translationScope,
			TranslationContext translationContext, BinaryFormat binaryFormat)
			throws SIMPLTranslationException
	{
		return getBinaryDeserializer(translationScope, translationContext, null, binaryFormat);
	}

	public static BinaryPullDeserializer getBinaryDeserializer(TranslationScope translationScope,
			TranslationContext translationContext,
			DeserializationHookStrategy deserializationHookStrategy, BinaryFormat binaryFormat)
			throws SIMPLTranslationException
	{
		switch (binaryFormat)
		{
		case TLV:
		default:
			throw new SIMPLTranslationException(binaryFormat + " format not supported");
		}
	}

	@Override
	public File fileContext()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParsedURL purlContext()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
