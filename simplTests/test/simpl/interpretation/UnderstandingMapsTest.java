package simpl.interpretation;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Map;

import org.junit.Test;

import simpl.exceptions.SIMPLTranslationException;

public class UnderstandingMapsTest {

	private SimplInterpretation stringVal(String s)
	{
		return new ScalarInterpretation("", s, "StringType");
	}
	
	private void validateUppercaseMap(Map m, String s)
	{
		assertEquals(s.toUpperCase(), m.get(s));
	}
	
	@Test
	public void getValueWorksCorrectly() throws SIMPLTranslationException
	{
		MapInterpretation mi = new MapInterpretation();
	
		mi.addEntryInterpretation(stringVal("a"), stringVal("A"));
		mi.addEntryInterpretation(stringVal("b"), stringVal("B"));
		mi.addEntryInterpretation(stringVal("c"), stringVal("C"));
		
		Map result = (Map) mi.getValue(null, new HashSet<String>(), null);
		assertNotNull(result);
		validateUppercaseMap(result, "a");
		validateUppercaseMap(result, "b");
		validateUppercaseMap(result, "c");
	}
	
	@Test
	public void testMapOfScalarToScalar()
	{
				
		

	
	}
	
	@Test
	public void testMapOfScalarToComposite()
	{
		fail("implement this test");
	}
	
	@Test
	public void testMapOfScalarToCyclicComposite()
	{
		fail("implement this test");
	}
	
	@Test
	public void testMapOfScalarToList()
	{
		fail("implement this test");
	}
	
	@Test
	public void testMapOfScalarToMapOfScalarToScalar()
	{
		fail("implement this test");
	}
	
	@Test
	public void testMapOfCompositeToScalar()
	{
		fail("implement this test");
	}
	
	@Test
	public void testMapOfCyclicCompositeToScalar()
	{
		fail("implement this test");
	}
}
