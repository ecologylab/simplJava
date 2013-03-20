package simpl.serialization.json;

import static org.junit.Assert.*;

import org.junit.Test;

import simpl.exceptions.SIMPLTranslationException;
import simpl.serialization.Circle;
import simpl.serialization.Point;

public class testSerializeComposites {

	@Test
	public void testBasicCompositeCase() throws SIMPLTranslationException {
		Circle c = new Circle();
		
		c.radius = 1.337;
	
		Point center = new Point();
		center.x = 0;
		center.y = -1;
		
		c.center = center;
		
		String result = new JsonSerializer().serialize(c);
		
		assertEquals("{\"circle\":{\"center\":{\"y\":\"-1\",\"x\":\"0\"},\"radius\":\"1.337\"}}", result);
	}

}
