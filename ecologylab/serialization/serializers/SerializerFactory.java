package ecologylab.serialization.serializers;

import ecologylab.serialization.SIMPLTranslationException;

/**
 * 
 * @author nabeelshahzad
 * 
 */
public class SerializerFactory
{

	private static FormatSerializer	xmlSerializer			= new XMLSerializer();

	private static FormatSerializer	jsonSerializer		= new JSONSerializer();

	private static FormatSerializer	tlvSerializer			= new TLVSerializer();

	private static FormatSerializer	bibitexSerializer	= new BibtexSerializer();

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
			return xmlSerializer;
		case JSON:
			return jsonSerializer;
		case TLV:
			return tlvSerializer;
		case BIBTEX:
			return bibitexSerializer;
		default:
			throw new SIMPLTranslationException(format + " format not supported");
		}
	}

}
