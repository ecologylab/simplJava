package ecologylab.serialization.serializers;

import ecologylab.serialization.SIMPLTranslationException;

/**
 * 
 * @author nabeelshahzad
 *
 */
public class SerializerFactory
{

	private static FormatSerializer	xmlSerializer			= null;

	private static FormatSerializer	jsonSerializer		= null;

	private static FormatSerializer	tlvSerializer			= null;

	private static FormatSerializer	bibitexSerializer	= null;
	
	
  /**
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
			if (xmlSerializer == null)
			{
				xmlSerializer = new XMLSerializer();
				return xmlSerializer;
			}
		case JSON:
			if (jsonSerializer == null)
			{
				jsonSerializer = new JSONSerializer();
				return jsonSerializer;
			}
		case TLV:
			if (tlvSerializer == null)
			{
				tlvSerializer = new TLVSerializer();
				return tlvSerializer;
			}
		case BIBTEX:
			if (bibitexSerializer == null)
			{
				bibitexSerializer = new BibtexSerializer();
				return bibitexSerializer;
			}
			else
				return bibitexSerializer;
		default:
			throw new SIMPLTranslationException(format + "format not supported");
		}
	}

}
