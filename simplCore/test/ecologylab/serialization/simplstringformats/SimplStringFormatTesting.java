package ecologylab.serialization.simplstringformats;

import org.junit.Assert;

import ecologylab.serialization.ISimplStringFormat;

public class SimplStringFormatTesting {

	public static void roundtrip(ISimplStringFormat format, String toTest)
	{
		String escaped = format.escape(toTest);
		String unescaped = format.unescape(escaped);
		Assert.assertEquals("Roundtrip failed on format: " + format.getClass().getName() , toTest, unescaped);		
	}
}
