package simplTestCasesDeSerializationTest;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;

import legacy.tests.DualBufferOutputStream;

import legacy.tests.maps.ClassDes;
import legacy.tests.maps.FieldDes;
import legacy.tests.maps.TranslationS;

import legacy.tests.TestCase;
import legacy.tests.TestingUtils;
import ecologylab.generic.ReflectionTools;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.formatenums.Format;

import org.junit.Test;

public class SimplMapsDeSerializationTest {
	
	@Test
	public void mapsDeSerializationTest() throws SIMPLTranslationException{
		
		//Could not complete the test in time, SimplEquals, once implemented, may make this easier.
		
		TranslationS trans = new TranslationS();

		ClassDes cd1 = new ClassDes("cd1");

		cd1.fieldDescriptorsByTagName.put("fd1_cd1", new FieldDes("fd1_cd1"));
		cd1.fieldDescriptorsByTagName.put("fd2_cd1", new FieldDes("fd2_cd1"));
		cd1.fieldDescriptorsByTagName.put("fd3_cd1", new FieldDes("fd3_cd1"));

		ClassDes cd2 = new ClassDes("cd2");
		cd2.fieldDescriptorsByTagName.put("fd1_cd2", new FieldDes("fd1_cd2"));
		cd2.fieldDescriptorsByTagName.put("fd2_cd2", new FieldDes("fd2_cd2"));
		cd2.fieldDescriptorsByTagName.put("fd3_cd2", new FieldDes("fd3_cd2"));

		trans.entriesByTag.put("cd1", cd1);
		trans.entriesByTag.put("cd2", cd2);
		
		SimplTypesScope tScope = SimplTypesScope.get("testingMapWithinMapsTScope", TranslationS.class, ClassDes.class,
				FieldDes.class);
		
		SimplTypesScope.enableGraphSerialization();
		
		Field[] translationSFields = TranslationS.class.getFields();
		
		
		//JSON===
		DualBufferOutputStream jsonOStream = new DualBufferOutputStream();
		SimplTypesScope.serialize(trans, jsonOStream, Format.JSON);
		
		String jsonResult = jsonOStream.toString();
		assertEquals(jsonResult, "{\"translation_s\":{\"class_descriptor\":[{\"tag_name\":\"cd1\",\"field_descriptor\":[{\"field_name\":\"fd3_cd1\"},{\"field_name\":\"fd2_cd1\"},{\"field_name\":\"fd1_cd1\"}]},{\"tag_name\":\"cd2\",\"field_descriptor\":[{\"field_name\":\"fd2_cd2\"},{\"field_name\":\"fd3_cd2\"},{\"field_name\":\"fd1_cd2\"}]}]}}");
		
		InputStream jsonIStream = new ByteArrayInputStream(jsonOStream.toByte());
		
		Object jsonObject = tScope.deserialize(jsonIStream, Format.JSON);
		assertTrue(jsonObject instanceof TranslationS);
		TranslationS jsonTranslationS = (TranslationS) jsonObject;
		
		//assertEquals(jsonTranslationS);
		
		/*for(Field i:translationSFields){
			assertEquals("Field" + i.getName() + " did not deserialize correctly", ReflectionTools.getFieldValue(jsonTranslationS, i), ReflectionTools.getFieldValue(trans, i));
		}*/
		
		//===

		//XML===
		DualBufferOutputStream xmlOStream = new DualBufferOutputStream();
		SimplTypesScope.serialize(trans, xmlOStream, Format.XML);
		
		String xmlResult = xmlOStream.toString();
		assertEquals(xmlResult, "<translation_s><class_descriptor tag_name=\"cd1\"><field_descriptor field_name=\"fd3_cd1\"/><field_descriptor field_name=\"fd2_cd1\"/><field_descriptor field_name=\"fd1_cd1\"/></class_descriptor><class_descriptor tag_name=\"cd2\"><field_descriptor field_name=\"fd2_cd2\"/><field_descriptor field_name=\"fd3_cd2\"/><field_descriptor field_name=\"fd1_cd2\"/></class_descriptor></translation_s>");
		
		InputStream xmlIStream = new ByteArrayInputStream(xmlOStream.toByte());
		
		Object xmlObject = tScope.deserialize(xmlIStream, Format.XML);
		assertTrue(xmlObject instanceof TranslationS);
		TranslationS xmlTranslationS = (TranslationS) xmlObject;
		
		//for(int i = 0; i < xmlTranslationS.entriesByTag.size(); i++){
		//	assertEquals(xmlTranslationS.entriesByTag.values().toArray()[i], trans.entriesByTag.values().toArray()[i]); 
		//}
		
		//assertEquals(xmlTranslationS);
	}
}