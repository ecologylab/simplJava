package simpl.serialization;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import legacy.tests.DualBufferOutputStream;
import legacy.tests.TestCase;
import legacy.tests.TestingUtils;
import legacy.tests.circle.Point;


import ecologylab.serialization.DeserializationHookStrategy;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.Hint;
import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_hints;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.formatenums.Format;

import static org.junit.Assert.*;

import org.junit.Test;
import ecologylab.generic.*;
import java.lang.reflect.*;

public class SimplScalarDeSerializationTest{
	
	@Test
	public void scalarDeSerializationTest() throws SIMPLTranslationException, IOException
	{
		
		Point p = new Point(1,-1);
		SimplTypesScope s = SimplTypesScope.get("pointscope", Point.class);
		SimplTypesScope.enableGraphSerialization();
		Field[] pointFields = Point.class.getFields();
		
		
		//JSON===
		
		DualBufferOutputStream jsonOPStream = new DualBufferOutputStream();
		SimplTypesScope.serialize(p, jsonOPStream, Format.JSON);
		
		String result1 = jsonOPStream.toString();		
		assertEquals(result1, "{\"point\":{\"x\":\"1\",\"y\":\"-1\"}}");
		
		InputStream jsonIPStream = new ByteArrayInputStream(jsonOPStream.toByte());
		
		Object o1 = s.deserialize(jsonIPStream, (DeserializationHookStrategy) null, Format.JSON, null);
		assertTrue(o1 instanceof Point);
		Point p1 = (Point) o1;
		
		for(Field i: pointFields){
			assertEquals("Field" + i.getName() + " did not deserialize correctly", ReflectionTools.getFieldValue(p1, i), ReflectionTools.getFieldValue(p, i));
		}
		//===
		
		
		
		//XML===
		DualBufferOutputStream xmlOPStream = new DualBufferOutputStream();
		SimplTypesScope.serialize(p,xmlOPStream, Format.XML);
		
		String result2 = xmlOPStream.toString();
		assertEquals(result2,"<point x=\"1\" y=\"-1\"/>" );
		
		InputStream xmlIPStream = new ByteArrayInputStream(xmlOPStream.toByte());
		
		Object o2 = s.deserialize(xmlIPStream, (DeserializationHookStrategy) null, Format.XML, null);
		assertTrue(o2 instanceof Point);
		Point p2 = (Point) o2;
		
		for(Field i: pointFields){
			assertEquals("Field" + i.getName() + " did not deserialize correctly", ReflectionTools.getFieldValue(p2, i), ReflectionTools.getFieldValue(p, i));
		}
		
		//===
		
		
		
		
		
		
		
		
	}
}