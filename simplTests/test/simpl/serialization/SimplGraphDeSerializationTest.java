package simpl.serialization;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;

import legacy.tests.DualBufferOutputStream;
import legacy.tests.graph.ClassA;
import legacy.tests.graph.ClassB;



import org.junit.Test;

import ecologylab.generic.ReflectionTools;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.formatenums.Format;


public class SimplGraphDeSerializationTest {

	@Test
	public void graphDeSerializationTest() throws SIMPLTranslationException{
		
		//NOTE: Currently, the only verification for correct serialization, is correct deserialization.
		
		
		SimplTypesScope.enableGraphSerialization();

		ClassA test = new ClassA(1, 2);
		ClassB classB = new ClassB(3, 4, test);

		test.setClassB(classB);
		
		Field[] classAFields = ClassA.class.getFields();
		
		SimplTypesScope translationScope = SimplTypesScope.get("classATScope", ClassA.class, ClassB.class);
		
		
		//JSON===
		DualBufferOutputStream jsonOStream = new DualBufferOutputStream();
		SimplTypesScope.serialize(test, jsonOStream, Format.JSON);
		
		String jsonResult = jsonOStream.toString();
		
		InputStream jsonIStream = new ByteArrayInputStream(jsonOStream.toByte());
		
		
		Object jsonObject = translationScope.deserialize(jsonIStream,Format.JSON);
		
		assertTrue(jsonObject instanceof ClassA);
		
		ClassA jsonClassA = (ClassA) jsonObject;
		assertTrue(jsonClassA.getClassB() instanceof ClassB);
		
		assertEquals(jsonClassA.getX(), test.getX());
		assertEquals(jsonClassA.getY(), test.getY());
		assertEquals(jsonClassA.getClassB().getA(), test.getClassB().getA());
		assertEquals(jsonClassA.getClassB().getB(), test.getClassB().getB());
		
		
		for(Field i:classAFields){
			assertEquals("Field" + i.getName() + " did not deserialize correctly", ReflectionTools.getFieldValue(jsonClassA, i), ReflectionTools.getFieldValue(test, i));
		}
		
		
		//===
		
		
		//XML===
		DualBufferOutputStream xmlOStream = new DualBufferOutputStream();
		SimplTypesScope.serialize(test, xmlOStream, Format.XML);
		
		String xmlResult = xmlOStream.toString();
				
		InputStream xmlIStream = new ByteArrayInputStream(xmlOStream.toByte());
		
		
		Object xmlObject = translationScope.deserialize(xmlIStream,Format.XML);
		
		assertTrue(xmlObject instanceof ClassA);
		
		ClassA xmlClassA = (ClassA) xmlObject;
		assertTrue(xmlClassA.getClassB() instanceof ClassB);
		
		assertEquals(xmlClassA.getX(), test.getX());
		assertEquals(xmlClassA.getY(), test.getY());
		assertEquals(xmlClassA.getClassB().getA(), test.getClassB().getA());
		assertEquals(xmlClassA.getClassB().getB(), test.getClassB().getB());
		
		for(Field i:classAFields){
			assertEquals("Field" + i.getName() + " did not deserialize correctly", ReflectionTools.getFieldValue(xmlClassA, i), ReflectionTools.getFieldValue(test, i));
		}
		
		//===
		
		SimplTypesScope.disableGraphSerialization();
		
	}
}
