package ecologylab.tutorials.game;

import static org.junit.Assert.*;

import org.junit.Test;

import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.formatenums.Format;
import ecologylab.serialization.formatenums.StringFormat;

import java.io.File;
import java.nio.file.*;

public class ThreatTest {

	@Test
	public void testRoundtripOnThreats() throws SIMPLTranslationException {
		Threat tom = new Threat();
		
		tom.id = "a";
		tom.ord = 13;
		tom.tVal = 3.14;
		
		tom.dir = new Coordinate(1,2);
		tom.vel = new Coordinate(3,4);
		tom.pos = new Coordinate(5,6);
		
		String result = SimplTypesScope.serialize(tom, StringFormat.XML).toString();
		
		System.out.println(result);
		
		SimplTypesScope example = SimplTypesScope.get("ThreatTest", Threat.class, Coordinate.class);
		Object XMLresult = example.deserialize(result, StringFormat.XML);
		
		Threat r = (Threat) XMLresult;
		
		assertEquals(r.dir, tom.dir);
		assertEquals(r.vel, tom.vel);
		assertEquals(r.pos, tom.pos);
		
		assertEquals(r.id, tom.id);
		assertEquals(r.ord, tom.ord);
		assertEquals(r.tVal, tom.tVal,0.1);
		
	}
	
	@Test
	public void testDeserializationOfTutorial() throws SIMPLTranslationException
	{
		SimplTypesScope example = SimplTypesScope.get("ThreatTest", Threat.class, Coordinate.class, OrbitingThreat.class, PatrollingThreat.class, RepellableThreat.class);

		File test = new File("SimplTutorial/test/ecologylab/tutorials/game/test.xml");
		
		example.deserialize(test, Format.XML);

		// todo put validation here. 

	}

}
