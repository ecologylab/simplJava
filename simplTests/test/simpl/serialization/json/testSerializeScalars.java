package simpl.serialization.json;

import static org.junit.Assert.*;

import org.junit.Test;

import simpl.exceptions.SIMPLTranslationException;
import simpl.interpretation.SimplInterpretation;
import simpl.interpretation.SimplInterpreter;
import simpl.serialization.Point;

public class testSerializeScalars {

	@Test
	public void testBasicScalarCase() throws SIMPLTranslationException {
		Point x = new Point();
		x.x = 1;
		x.y = 3;
		
		JsonSerializer json = new JsonSerializer();
		

		SimplInterpreter si = new SimplInterpreter();
		SimplInterpretation interp = si.interpretInstance(x);
		String result = json.serialize(interp);
		
		assertNotNull("Result should not be null!", result);
		assertEquals("{\"point\":{\"y\":\"3\",\"x\":\"1\"}}", result);
	}

}
