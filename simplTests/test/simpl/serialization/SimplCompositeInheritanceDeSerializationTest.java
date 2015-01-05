package simpl.serialization;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;

import legacy.tests.DualBufferOutputStream;
import legacy.tests.inheritance.BaseClass;
import legacy.tests.inheritance.ChildClass1;
import legacy.tests.inheritance.ChildClass2;
import legacy.tests.inheritance.ContainingClass;

import org.junit.Test;

import ecologylab.generic.ReflectionTools;
import ecologylab.serialization.DeserializationHookStrategy;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.formatenums.Format;

public class SimplCompositeInheritanceDeSerializationTest {
	
	@Test
	public void compositeInheritanceDeSerializationTest() throws SIMPLTranslationException{
		
		SimplTypesScope translationScope = SimplTypesScope.get("containingClassTScope", ContainingClass.class,
				BaseClass.class);

		ContainingClass ccb = new ContainingClass();
		ccb.setTheField(new BaseClass());
		
		Field[] ccbFields = ContainingClass.class.getFields();
		
		
		//JSON===
		DualBufferOutputStream jsonOStream = new DualBufferOutputStream();
		SimplTypesScope.serialize(ccb, jsonOStream, Format.JSON);
		
		String jsonResult = jsonOStream.toString();
		assertEquals(jsonResult, "{\"containing_class\":{\"fred\":{\"new_tag_var\":\"3\"}}}");
		
		InputStream jsonIStream = new ByteArrayInputStream(jsonOStream.toByte());
		
		Object jsonObject =translationScope.deserialize(jsonIStream,  (DeserializationHookStrategy) null, Format.JSON, null);
		assertTrue(jsonObject instanceof ContainingClass);
		ContainingClass jsonCCB = (ContainingClass) jsonObject;
		
		for(Field i:ccbFields){
			assertEquals("Field" + i.getName() + " did not deserialize correctly", ReflectionTools.getFieldValue(jsonCCB, i), ReflectionTools.getFieldValue(ccb, i));
		}
		
		
		//===
		
		//XML===
		DualBufferOutputStream xmlOStream = new DualBufferOutputStream();
		SimplTypesScope.serialize(ccb, xmlOStream, Format.XML);
		
		String xmlResult = xmlOStream.toString();
		assertEquals(xmlResult, "<containing_class><fred new_tag_var=\"3\"/></containing_class>");
		
		InputStream xmlIStream = new ByteArrayInputStream(xmlOStream.toByte());
		
		Object xmlObject =translationScope.deserialize(xmlIStream,  (DeserializationHookStrategy) null, Format.XML, null);
		assertTrue(xmlObject instanceof ContainingClass);
		ContainingClass xmlCCB = (ContainingClass) xmlObject;
		
		for(Field i:ccbFields){
			assertEquals("Field" + i.getName() + " did not deserialize correctly", ReflectionTools.getFieldValue(xmlCCB, i), ReflectionTools.getFieldValue(ccb, i));
		}
				
				
		//===
	}
}
