package ecologylab.serialization.simplstringformats;

import org.junit.Test;

import simpl.core.ISimplStringFormat;
import simpl.formats.string.JSONFormat;

public class JSONFormatTest {

	@Test
	public void testAFewCases() {
		ISimplStringFormat format = new JSONFormat();
		SimplStringFormatTesting.roundtrip(format, "the most basic string ever");
		SimplStringFormatTesting.roundtrip(format, "sad;lkfjsadf \r\n <some html asdfasdf> </html> <!CDATA[\"awwwwwww yeah \"]--> ");	
	}

}
