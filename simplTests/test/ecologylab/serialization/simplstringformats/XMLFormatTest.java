package ecologylab.serialization.simplstringformats;

import static ecologylab.serialization.simplstringformats.SimplStringFormatTesting.roundtrip;

import org.junit.Test;

import simpl.core.ISimplStringFormat;
import simpl.formats.string.XMLFormat;


public class XMLFormatTest {

	@Test
	public void testAFewCases() {
		ISimplStringFormat format = new XMLFormat();
		roundtrip(format, "sad;lkfjsadf \r\n <some html asdfasdf> </html> <!CDATA[\"awwwwwww yeah \"]--> ");	
	}

}
