package simpl.types.scalar;

import simpl.types.ScalarType;
import ecologylab.serialization.ScalarUnmarshallingContext;
import ecologylab.serialization.TranslationContext;

public abstract class ImageType<T> extends ScalarType<T> 

{
	public ImageType(Class thatClass) 
	{
		super(thatClass);
	}

	/**
	 * get platform dependent image type T instance from string.
	 */
	@Override
	public abstract T getInstance(String value, String[] formatStrings,
			ScalarUnmarshallingContext scalarUnmarshallingContext);
	
	/**
	 * marshall platform dependent image type T to string. 
	 */
	@Override
	public abstract String marshall(T input, TranslationContext serializationContext);

	/**
	 * convert raw image byte array to string
	 * 
	 * @param imageRawByte
	 * @return
	 */
	public String byteArrayToString(byte[] rawImageByteArray)
	{
		StringBuilder res = new StringBuilder(rawImageByteArray.length*2);
		
		for (int i = 0; i < rawImageByteArray.length; ++i)
		{
			int in = (rawImageByteArray[i] >= 0)?rawImageByteArray[i]:256+rawImageByteArray[i];
			String x = Integer.toString(in,16);
			if (in < 16)
				res.append("0");
			res.append(x);
		}
		
		return res.toString();
	}
	
	/**
	 * convert serialized image string to byte array
	 * 
	 * @param serializedImageString
	 * @return
	 */
	public byte[] stringToByteArray(String serializedImageString)
	{
		serializedImageString = serializedImageString.trim();
		byte[] data = new byte[serializedImageString.length() / 2];

		// TODO: optimize
		for (int i = 0; i < data.length; ++i)
			data[i] = (byte) Integer.parseInt(serializedImageString.substring(2 * i, 2 * i + 2), 16);

		return data;
	}
	
}
