package ecologylab.serialization.simplstringformats;

import static ecologylab.serialization.simplstringformats.SimplStringFormatTesting.roundtrip;

import org.junit.Test;

import simpl.core.ISimplStringFormat;
import simpl.formats.string.JSONFormat;



public class JSONFormatTest {

	@Test
	public void testAFewCases() {
		ISimplStringFormat format = new JSONFormat();
		roundtrip(format, "the most basic string ever");
		roundtrip(format, "sad;lkfjsadf \r\n <some html asdfasdf> </html> <!CDATA[\"awwwwwww yeah \"]--> ");	
	}

}
