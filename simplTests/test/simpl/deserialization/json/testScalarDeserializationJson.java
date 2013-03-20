package simpl.deserialization.json;

import static org.junit.Assert.*;

import org.junit.Test;

import simpl.core.ISimplTypesScope;
import simpl.core.SimplTypesScopeFactory;
import simpl.exceptions.SIMPLTranslationException;
import simpl.interpretation.CompositeInterpretation;
import simpl.interpretation.SimplUnderstander;
import simpl.serialization.Point;

public class testScalarDeserializationJson {

	@Test
	public void testDeserializeScalars() throws SIMPLTranslationException {
		
		ISimplTypesScope sts = SimplTypesScopeFactory.name("scalartest").translations(Point.class).create();
		
		CompositeInterpretation ci = new JsonDeserializer().deserialize("{\"point\":{\"y\":\"3\",\"x\":\"1\"}}", sts);
		SimplUnderstander su = new SimplUnderstander(sts);
		
		Object result = su.understandInterpretation(ci);
		
		assertNotNull("Result should not be null!", result);
		assertTrue(result instanceof Point);
		
		Point point = (Point)result;
		
		assertTrue(point.x == 1);
		assertEquals(point.y, new Integer(3));
		
	}
	
	
}
