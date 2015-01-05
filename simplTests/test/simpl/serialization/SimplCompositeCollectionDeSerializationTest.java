package simpl.serialization;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;

import legacy.tests.DualBufferOutputStream;
import legacy.tests.circle.Circle;
import legacy.tests.circle.CollectionOfCircles;
import legacy.tests.circle.Point;

import org.junit.Test;

import ecologylab.generic.ReflectionTools;
import ecologylab.serialization.DeserializationHookStrategy;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.formatenums.Format;

public class SimplCompositeCollectionDeSerializationTest {

	@Test
	public void compositeCollectionDeSerializationTest() throws SIMPLTranslationException
	{
		CollectionOfCircles coc = new CollectionOfCircles();
		
		coc.addCircle(1, 2, 3);
		coc.addCircle(1, 2, 4);
		coc.addCircle(1, 2, 5);
		coc.addCircle(1, 2, 6);
		coc.addCircle(1, 2, 7);
		
		SimplTypesScope circleTranslationScope = SimplTypesScope.get("collectionOfCirclesTScope",
		CollectionOfCircles.class, Circle.class, Point.class);
		
		Field[] cocFields = CollectionOfCircles.class.getFields();
		
		//JSON===
		DualBufferOutputStream jsonOStream = new DualBufferOutputStream();
		SimplTypesScope.serialize(coc, jsonOStream, Format.JSON);
		
		String jsonResult = jsonOStream.toString();
		
		assertEquals("Serialized JSON string did not match expected", jsonResult, "{\"collection_of_circles\":{\"collection_of_circles\":{\"circles\":[{\"radius\":\"1\",\"center\":{\"x\":\"2\",\"y\":\"3\"}},{\"radius\":\"1\",\"center\":{\"x\":\"2\",\"y\":\"4\"}},{\"radius\":\"1\",\"center\":{\"x\":\"2\",\"y\":\"5\"}},{\"radius\":\"1\",\"center\":{\"x\":\"2\",\"y\":\"6\"}},{\"radius\":\"1\",\"center\":{\"x\":\"2\",\"y\":\"7\"}}]},\"yo\":\"1\"}}");
		
		InputStream jsonIStream = new ByteArrayInputStream(jsonOStream.toByte());
		Object jsonObject = circleTranslationScope.deserialize(jsonIStream,  (DeserializationHookStrategy) null, Format.JSON, null);
		
		assertTrue(jsonObject instanceof CollectionOfCircles);
		CollectionOfCircles coc_json = (CollectionOfCircles) jsonObject;
		
		for(Field i:cocFields){
			assertEquals("Field" + i.getName() + " did not deserialize correctly", ReflectionTools.getFieldValue(coc_json, i), ReflectionTools.getFieldValue(coc, i));	
		}
		
		//===
		
		//XML===
		DualBufferOutputStream xmlOStream = new DualBufferOutputStream();
		SimplTypesScope.serialize(coc, xmlOStream, Format.XML);
		
		String xmlResult = xmlOStream.toString();
		
		assertEquals("Serialized XML string did not match expected", xmlResult, "<collection_of_circles><collection_of_circles><circles><radius>1</radius><center x=\"2\" y=\"3\"/></circles><circles><radius>1</radius><center x=\"2\" y=\"4\"/></circles><circles><radius>1</radius><center x=\"2\" y=\"5\"/></circles><circles><radius>1</radius><center x=\"2\" y=\"6\"/></circles><circles><radius>1</radius><center x=\"2\" y=\"7\"/></circles></collection_of_circles><yo>1</yo></collection_of_circles>");
		
		InputStream xmlIStream = new ByteArrayInputStream(xmlOStream.toByte());
		Object xmlObject = circleTranslationScope.deserialize(xmlIStream,  (DeserializationHookStrategy) null, Format.XML, null);
		
		assertTrue(xmlObject instanceof CollectionOfCircles);
		CollectionOfCircles coc_xml = (CollectionOfCircles) xmlObject;
		
		for(Field i:cocFields){
			assertEquals("Field" + i.getName() + " did not deserialize correctly", ReflectionTools.getFieldValue(coc_xml, i), ReflectionTools.getFieldValue(coc, i));
			
		}
		
		//===
	}
}
