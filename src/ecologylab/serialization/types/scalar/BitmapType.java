package ecologylab.serialization.types.scalar;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import ecologylab.serialization.ScalarUnmarshallingContext;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.types.ScalarType;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapType extends ScalarType<Bitmap>{
	
	public BitmapType(){
		super(Bitmap.class, null, null, null);
	}
	
	public String fix(byte[] arr)
	{
		
		StringBuilder res = new StringBuilder(arr.length*2);
		
		for (int i = 0; i < arr.length; ++i)
		{
			int in = (arr[i] >= 0)?arr[i]:256+arr[i];
			String x = Integer.toString(in,16);
			if (in < 16)
				res.append("0");
			res.append(x);
		}
		
		return res.toString();
	}

	@Override
	public Bitmap getInstance(String value, String[] formatStrings,
			ScalarUnmarshallingContext scalarUnmarshallingContext) {

		value = value.trim();
		byte[] data = new byte[value.length()/2];
		
		// TODO: optimize
		for (int i = 0; i < data.length; ++i)
			data[i] = (byte)Integer.parseInt(value.substring(2*i, 2*i+2), 16);
		return BitmapFactory.decodeStream(new ByteArrayInputStream(data));
	}

	@Override
	public String marshall(Bitmap value, TranslationContext suc)
	{

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		value.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return fix(baos.toByteArray());
	}
}
