package ecologylab.serialization.types.scalar;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import ecologylab.serialization.ScalarUnmarshallingContext;
import ecologylab.serialization.TranslationContext;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapImageType extends ImageType<Bitmap>{
	
	public BitmapImageType(){
		super(Bitmap.class);
	}

	@Override
	public Bitmap getInstance(String value, String[] formatStrings,
			ScalarUnmarshallingContext scalarUnmarshallingContext) 
	{

		byte[] data = stringToByteArray(value);
		return BitmapFactory.decodeStream(new ByteArrayInputStream(data));
	}

	@Override
	public String marshall(Bitmap value, TranslationContext suc)
	{

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		value.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return byteArrayToString(baos.toByteArray());
	}
}
