package ecologylab.fundamental;

import static org.junit.Assert.*;

import org.junit.Test;

import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.FieldDescriptor;
import simpl.exceptions.SIMPLTranslationException;
import simpl.types.ScalarType;
import simpl.types.TypeRegistry;

import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.formatenums.StringFormat;

public class RoundtripTestsProvisional {

	@Test
	public void testJSONRoundtripWithTrickyString() throws SIMPLTranslationException {

		
		TrickyString ts = new TrickyString();
		ts.trickyString = "\" \\ / // <<< >>> {}{}{} {>>>   ==  =::  / / / /_ -- --__ : : \b a \f b \n c \n d  \r e \t f"; // we choke on unicode, for now \u1337";
		System.out.println(ts.trickyString);
		
		StringBuilder serialized = SimplTypesScope.serialize(ts, StringFormat.JSON);
		System.out.println(serialized.toString());
		
		SimplTypesScope sts = SimplTypesScope.get("stringTest", TrickyString.class);
		
		Object result = sts.deserialize(serialized.toString(), StringFormat.JSON);
		assertTrue(result.getClass().equals(TrickyString.class));
		TrickyString otherString = (TrickyString)result;
		
		System.out.println(otherString.trickyString);
		
		assertEquals(ts.trickyString, otherString.trickyString);
	}

	@Test
	public void testXMLRoundtripWithTrickyString() throws SIMPLTranslationException {

		
		TrickyString ts = new TrickyString();
		ts.trickyString = "< >> <<<<< >>>> ><<><><>< \"/ '' = = = = ;:  === \": ' \\\"\\\"\\\\\" '\" \\ / // / / / /_ -- --__ : : \b a \f b \n c \n  d \r e \t f"; //We choke on unicode, for now. \u1337";
		System.out.println(ts.trickyString);
		
		StringBuilder serialized = SimplTypesScope.serialize(ts, StringFormat.XML);
		System.out.println(serialized.toString());
		
		SimplTypesScope sts = SimplTypesScope.get("stringTest", TrickyString.class);
		
		Object result = sts.deserialize(serialized.toString(), StringFormat.XML);
		assertTrue(result.getClass().equals(TrickyString.class));
		TrickyString otherString = (TrickyString)result;
		
		System.out.println(otherString.trickyString);
		
		assertEquals(ts.trickyString, otherString.trickyString);
	}

	
	@Test
	public void TestSimplStringScalarTypeSetsDefaultToNull()
	{
		TrickyString ts = new TrickyString();
		ts.trickyString = "totallyNotDefault";
		
		ClassDescriptor<?> cd = ClassDescriptor.getClassDescriptor(TrickyString.class);
		
		FieldDescriptor fd = cd.allFieldDescriptors().get(0);
		TranslationContext tc = null;
		fd.setFieldToScalarDefault(ts, tc);
		
		assertEquals("Should have changed!", null, ts.trickyString);
		
	}
	
	@Test
	public void nullStringValue() throws SIMPLTranslationException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException 
	{
	    TrickyString ts = new TrickyString();
	    ts.trickyString = null;
	    
	    ScalarType<?> stringType = TypeRegistry.getScalarType(String.class);
	    
	    assertTrue("Null is the new default string value - via value check", stringType.isDefaultValue(ts.trickyString));
	    assertTrue("Null is the new default string value - via field check", stringType.isDefaultValue(ts.getClass().getField("trickyString"), ts));
	    
	    String xml = SimplTypesScope.serialize(ts, StringFormat.XML).toString();
	    System.out.println(xml);
	    
		  SimplTypesScope typeScope = SimplTypesScope.get("TrickyString", TrickyString.class);
	    TrickyString ts1 = (TrickyString) typeScope.deserialize(xml, StringFormat.XML);
	    assertNotNull("Expecting an object back!", ts1);

	    assertEquals("Expecting null value back!",null, ts1.trickyString);
	}
	
	
	
}
