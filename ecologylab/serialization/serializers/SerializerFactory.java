package ecologylab.serialization.serializers;

import ecologylab.serialization.SIMPLTranslationException;

/**
 * 
 * @author nabeelshahzad
 * 
 */
public class SerializerFactory
{

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
}
