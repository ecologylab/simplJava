package ecologylab.fundamental;

import static org.junit.Assert.*;

import org.junit.Test;

import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
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

	
}
