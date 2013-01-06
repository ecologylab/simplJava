package ecologylab.serialization.serializers;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.SimplTypesScope.GRAPH_SWITCH;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.formatenums.BinaryFormat;
import ecologylab.serialization.formatenums.Format;
import ecologylab.serialization.formatenums.StringFormat;
import ecologylab.serialization.serializers.binaryformats.TLVSerializer;
import ecologylab.serialization.serializers.stringformats.BibtexSerializer;
import ecologylab.serialization.serializers.stringformats.JSONSerializer;
import ecologylab.serialization.serializers.stringformats.StringSerializer;
import ecologylab.serialization.serializers.stringformats.XMLSerializer;

/**
 * FormatSerializer. an abstract base class from where format-specific serializers derive. Its main
 * use is for exposing the API for serialization methods. It contains helper functions and wrapper
 * serialization functions, allowing software developers to use different types of objects for
 * serialization, such as System.out, File, StringBuilder, or return serialized data as
 * StringBuilder
 * 
 * @author nabeel
 * 
 */
public abstract class FormatSerializer
{
	/**
	 * 
	 * @param object
	 * @param outputStream
	 * @throws SIMPLTranslationException
	 */
	public void serialize(Object object, OutputStream outputStream) throws SIMPLTranslationException
	{
		TranslationContext translationContext = new TranslationContext();
		serialize(object, outputStream, translationContext);
	}

	public abstract void serialize(Object object, OutputStream outputStream,
			TranslationContext translationContext) throws SIMPLTranslationException;

	/**
	 * 
	 * @param object
	 * @param outputFile
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	public void serialize(Object object, File outputFile) throws SIMPLTranslationException
	{		
		TranslationContext translationContext = new TranslationContext();
		serialize(object, outputFile, translationContext);
	}

	public abstract void serialize(Object object, File outputFile,
			TranslationContext translationContext) throws SIMPLTranslationException;

	/**
	 * 
	 * @param object
	 * @return
	 */
	protected ClassDescriptor<? extends FieldDescriptor> getClassDescriptor(Object object)
	{
		return ClassDescriptor.getClassDescriptor(object.getClass());
	}

	/**
	 * 
	 * @param object
	 * @param translationContext TODO
	 */
	protected void serializationPostHook(Object object, TranslationContext translationContext)
	{
		if (object instanceof ISimplSerializationPost)
		{
			((ISimplSerializationPost) object).serializationPostHook(translationContext);
		}
	}

	/**
	 * 
	 * @param object
	 * @param translationContext TODO
	 */
	protected void serializationPreHook(Object object, TranslationContext translationContext)
	{
		if (object instanceof ISimplSerializationPre)
		{
			((ISimplSerializationPre) object).serializationPreHook(translationContext);
		}
	}

	/**
	 * 
	 * @param object
	 * @param translationContext
	 * @return
	 */
	protected boolean alreadySerialized(Object object, TranslationContext translationContext)
	{
		return SimplTypesScope.graphSwitch == GRAPH_SWITCH.ON
				&& translationContext.alreadyMarshalled(object);
	}

	/**
	 * returns the specific type of serializer based on the input format
	 * 
	 * @param format
	 * @return FormatSerializer
	 * @throws SIMPLTranslationException
	 */
	public static FormatSerializer getSerializer(Format format) throws SIMPLTranslationException
	{
		switch (format)
		{
			case XML:
				return new XMLSerializer();
			case JSON:
				return new JSONSerializer();
			case TLV:
				return new TLVSerializer();
			case BIBTEX:
				return new BibtexSerializer();
			default:
				throw new SIMPLTranslationException(format + " format not supported");
		}
	}

	/**
	 * returns the specific type of serializer based on the input format
	 * 
	 * @param format
	 * @return FormatSerializer
	 * @throws SIMPLTranslationException
	 */
	public static StringSerializer getStringSerializer(StringFormat format)
			throws SIMPLTranslationException
	{
		switch (format)
		{
			case XML:
				return new XMLSerializer();
			case JSON:
				return new JSONSerializer();
			case BIBTEX:
				return new BibtexSerializer();
			default:
				throw new SIMPLTranslationException(format + " format not supported");
		}
	}

	/**
	 * returns the specific type of serializer based on the input format
	 * 
	 * @param format
	 * @return FormatSerializer
	 * @throws SIMPLTranslationException
	 */
	public static FormatSerializer getBinarySerializer(BinaryFormat format)
			throws SIMPLTranslationException
	{
		switch (format)
		{
			case TLV:
				return new TLVSerializer();
			default:
				throw new SIMPLTranslationException(format + " format not supported");
		}
	}
}