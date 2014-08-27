package simpl.serialization;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import legacy.tests.DualBufferOutputStream;
import legacy.tests.TestCase;
import legacy.tests.TestingUtils;
import legacy.tests.circle.Circle;
import legacy.tests.circle.Point;


import ecologylab.generic.ReflectionTools;
import ecologylab.serialization.DeserializationHookStrategy;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.Hint;
import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_hints;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.formatenums.Format;
import ecologylab.serialization.library.rest.Fields;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimplCompositeDeSerializationTest {
	
	@Test
	public void compositeDeSerializationTest() throws SIMPLTranslationException
	{
		Point p = new Point(1, -1);
		Circle c = new Circle(3, p);
		SimplTypesScope s = SimplTypesScope.get("circlescope", Point.class, Circle.class);
		SimplTypesScope.enableGraphSerialization();
		
		Field[] circleFields = Circle.class.getFields();
		
		//JSON===
		DualBufferOutputStream outputStream = new DualBufferOutputStream();
		SimplTypesScope.serialize(c, outputStream, Format.JSON);
		
		String result = outputStream.toString();
		assertEquals("Serialized JSON string did not match expected", "{\"circle\":{\"radius\":\"3\",\"center\":{\"x\":\"1\",\"y\":\"-1\"}}}", result);
		
		InputStream inputStream = new ByteArrayInputStream(outputStream.toByte());
		
		Object jsonObject = s.deserialize(inputStream,  (DeserializationHookStrategy) null, Format.JSON, null);
		assertTrue(jsonObject instanceof Circle);
		Circle jsonCircle = (Circle) jsonObject;
		
		for(Field i: circleFields){
			assertEquals("Field" + i.getName() + " did not deserialize correctly", ReflectionTools.getFieldValue(jsonCircle, i), ReflectionTools.getFieldValue(c, i));	
		}
		
		//===
		
		//XML===
		DualBufferOutputStream xmlOStream = new DualBufferOutputStream();
		SimplTypesScope.serialize(c, xmlOStream, Format.XML);
		
		String xmlResult = xmlOStream.toString();
		assertEquals("Serialized XML string did not match expected", "<circle><radius>3</radius><center x=\"1\" y=\"-1\"/></circle>", xmlResult);
		
		InputStream xmlIStream = new ByteArrayInputStream(xmlOStream.toByte());
		
		Object xmlObject = s.deserialize(xmlIStream,  (DeserializationHookStrategy) null, Format.XML, null);
		assertTrue(xmlObject instanceof Circle);
		Circle xmlCircle = (Circle) xmlObject;
		
		for(Field i: circleFields){
			assertEquals("Field" + i.getName() + " did not deserialize correctly", ReflectionTools.getFieldValue(xmlCircle, i), ReflectionTools.getFieldValue(c, i));	
		}
		
		//===
	}
}