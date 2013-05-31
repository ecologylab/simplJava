package simplTestCasesDeSerializationTest;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;

import legacy.tests.DualBufferOutputStream;
import legacy.tests.scalar.ScalarCollection;

import org.junit.Test;

import ecologylab.generic.ReflectionTools;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.formatenums.Format;

public class SimplScalarCollectionDeSerializationTest {

	@Test
	public void scalarCollectionDeSerializationTest() throws SIMPLTranslationException{
		
		
		SimplTypesScope.enableGraphSerialization();
		
		ScalarCollection sc = new ScalarCollection();
		sc.addInt(1);
		sc.addInt(2);
		sc.addInt(3);
		sc.addInt(4);
		sc.addInt(5);

		SimplTypesScope translationScope = SimplTypesScope.get("scalarCollectionTScope", ScalarCollection.class);
		
		Field[] scFields = ScalarCollection.class.getFields();
		
		//JSON===
		DualBufferOutputStream jsonOStream = new DualBufferOutputStream();
		SimplTypesScope.serialize(sc, jsonOStream, Format.JSON);
		
		String jsonResult = jsonOStream.toString();
		assertEquals(jsonResult, "{\"scalar_collection\":{\"circles\":[\"1\",\"2\",\"3\",\"4\",\"5\"]}}");
		
		InputStream jsonIStream = new ByteArrayInputStream(jsonOStream.toByte());

		Object jsonObject = translationScope.deserialize(jsonIStream, Format.JSON);
		assertTrue(jsonObject instanceof ScalarCollection);
		ScalarCollection jsonCollection = (ScalarCollection) jsonObject;
		
		for(Field i:scFields){
			assertEquals("Field"+i.getName()+" did not deserialize correctly", ReflectionTools.getFieldValue(jsonCollection, i), ReflectionTools.getFieldValue(sc, i));
		}
		//===
		
		
		//XML===
		DualBufferOutputStream xmlOStream = new DualBufferOutputStream();
		SimplTypesScope.serialize(sc, xmlOStream, Format.XML);
		
		String xmlResult = xmlOStream.toString();
		assertEquals(xmlResult, "<scalar_collection><circles>1</circles><circles>2</circles><circles>3</circles><circles>4</circles><circles>5</circles></scalar_collection>");
		
		InputStream xmlIStream = new ByteArrayInputStream(xmlOStream.toByte());

		Object xmlObject = translationScope.deserialize(xmlIStream, Format.XML);
		assertTrue(xmlObject instanceof ScalarCollection);
		ScalarCollection xmlCollection = (ScalarCollection) xmlObject;
		
		for(Field i:scFields){
			assertEquals("Field" + i.getName() + " did not deserialize correctly", ReflectionTools.getFieldValue(xmlCollection, i), ReflectionTools.getFieldValue(sc, i));
		}
		
		//===
		
	}
}
