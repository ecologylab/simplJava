package ecologylab.serialization.deserializers.pullhandlers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import ecologylab.generic.Debug;
import ecologylab.net.ConnectionAdapter;
import ecologylab.net.PURLConnection;
import ecologylab.net.ParsedURL;
import ecologylab.serialization.BinaryFormat;
import ecologylab.serialization.DeserializationHookStrategy;
import ecologylab.serialization.FieldDescriptor;
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

public abstract class PullDeserializer extends Debug implements FieldTypes
{

	protected TranslationScope																													translationScope;

	protected TranslationContext																												translationContext;

	protected DeserializationHookStrategy<? extends Object, ? extends FieldDescriptor>	deserializationHookStrategy;

	static final ConnectionAdapter																											connectionAdapter	= new ConnectionAdapter();

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
	public PullDeserializer(
			TranslationScope translationScope,
			TranslationContext translationContext,
			DeserializationHookStrategy<? extends Object, ? extends FieldDescriptor> deserializationHookStrategy)
	{
		this.translationScope = translationScope;
		this.translationContext = translationContext;
		this.deserializationHookStrategy = deserializationHookStrategy;
	}

	public Object parse(File file) throws SIMPLTranslationException
	{
		try
		{
			FileInputStream fileInputStream = new FileInputStream(file);
			BufferedInputStream bufferedStream = new BufferedInputStream(fileInputStream);

			Object object = parse(bufferedStream);
			bufferedStream.close();
			return object;
		}
		catch (FileNotFoundException e)
		{
			throw new SIMPLTranslationException("Can't open file " + file.getAbsolutePath(), e);
		}
		catch (IOException e)
		{
			throw new SIMPLTranslationException("Can't close file " + file.getAbsolutePath(), e);
		}

	}

	public Object parse(ParsedURL purl) throws SIMPLTranslationException
	{
		if (purl.isFile())
			return parse(purl.file());

		PURLConnection purlConnection = purl.connect(connectionAdapter);
		Object result = parse(purlConnection.inputStream());
		purlConnection.recycle();
		return result;
	}

	public Object parse(URL url) throws SIMPLTranslationException
	{
		return parse(new ParsedURL(url));
	}

	public abstract Object parse(InputStream inputStream) throws SIMPLTranslationException;

	public static PullDeserializer getDeserializer(TranslationScope translationScope,
			TranslationContext translationContext, Format format) throws SIMPLTranslationException
	{
		return getDeserializer(translationScope, translationContext, null, format);
	}

	public static PullDeserializer getDeserializer(
			TranslationScope translationScope,
			TranslationContext translationContext,
			DeserializationHookStrategy<? extends Object, ? extends FieldDescriptor> deserializationHookStrategy,
			Format format) throws SIMPLTranslationException
	{
		switch (format)
		{
		case XML:
			return new XMLPullDeserializer(translationScope, translationContext,
					deserializationHookStrategy);
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

	public static StringPullDeserializer getStringDeserializer(
			TranslationScope translationScope,
			TranslationContext translationContext,
			DeserializationHookStrategy<? extends Object, ? extends FieldDescriptor> deserializationHookStrategy,
			StringFormat stringFormat) throws SIMPLTranslationException
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

	public static BinaryPullDeserializer getBinaryDeserializer(
			TranslationScope translationScope,
			TranslationContext translationContext,
			DeserializationHookStrategy<? extends Object, ? extends FieldDescriptor> deserializationHookStrategy,
			BinaryFormat binaryFormat) throws SIMPLTranslationException
	{
		switch (binaryFormat)
		{
		case TLV:
		default:
			throw new SIMPLTranslationException(binaryFormat + " format not supported");
		}
	}

}
