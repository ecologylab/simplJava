package ecologylab.serialization.serializers.binaryformats;

import java.io.DataOutputStream;
import java.io.File;
import java.io.OutputStream;

import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.serializers.FormatSerializer;

public abstract class BinarySerializer extends FormatSerializer
{

	@Override
	public void serialize(Object object, OutputStream outputStream,
			TranslationContext translationContext) throws SIMPLTranslationException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void serialize(Object object, File outputFile, TranslationContext translationContext)
			throws SIMPLTranslationException
	{
		// TODO Auto-generated method stub

	}
	
	/**
	 * 
	 * @param object
	 * @param dataOutputStream
	 * @param translationContext
	 * @throws SIMPLTranslationException
	 */
	public abstract void serialize(Object object, DataOutputStream dataOutputStream,
			TranslationContext translationContext) throws SIMPLTranslationException;

}
