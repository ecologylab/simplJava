package simpl.serialization.json;

import static org.junit.Assert.*;

import org.junit.Test;

import simpl.exceptions.SIMPLTranslationException;
import simpl.serialization.Point;

public class testSerializeScalars {

	@Test
	public void testBasicScalarCase() throws SIMPLTranslationException {
		Point x = new Point();
		x.x = 1;
		x.y = 3;
		
		JsonSerializer json = new JsonSerializer();
		String result = json.serialize(x);
		
		assertNotNull("Result should not be null!", result);
		assertEquals("{\"point\":{\"y\":\"3\",\"x\":\"1\"}}", result);
	}

}
