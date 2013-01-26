package simpl.types.scalar;

import java.nio.ByteBuffer;

import simpl.annotations.dbal.simpl_inherit;
import simpl.core.ScalarUnmarshallingContext;
import simpl.core.TranslationContext;
import simpl.types.CrossLanguageTypeConstants;
import simpl.types.ScalarType;

import ecologylab.generic.Base64Coder;

@simpl_inherit
public class BinaryDataType extends ScalarType<ByteBuffer>
implements CrossLanguageTypeConstants 
{
	public BinaryDataType()
	{
		super(ByteBuffer.class, DOTNET_BINARY_DATA, null, null);
	}
	
	/**
	 * read the Base64 encoded string . convert to byte array. 
	 */
	@Override
	public ByteBuffer getInstance(String value, String[] formatStrings,
			ScalarUnmarshallingContext scalarUnmarshallingContext) 
	{
			return ByteBuffer.wrap(Base64Coder.decode(value));
	}

	/**
	 * read the binary content of the input byteBuffer, and encode it using Base64
	 */
	@Override
	public String marshall(ByteBuffer input, TranslationContext serializationContext)
	{	
		return new String(Base64Coder.encode(input.array()));
	}
}
