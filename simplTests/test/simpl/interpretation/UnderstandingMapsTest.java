package simpl.interpretation;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Map;

import org.junit.Test;

import simpl.core.ISimplTypesScope;
import simpl.core.SimplTypesScopeFactory;
import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.ClassDescriptors;
import simpl.descriptions.FieldDescriptor;
import simpl.exceptions.SIMPLTranslationException;
import simpl.types.MapType;

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
	
	private ScalarInterpretation string(String s)
	{
		return new ScalarInterpretation("", s, "StringType");
	}
	
	private ScalarInterpretation integ(Integer i)
	{
		return new ScalarInterpretation("", i.toString(), "IntegerType");
	}
	
	@Test
	public void testMapOfScalarToScalar() throws SIMPLTranslationException
	{
				
		mapOfScalarToScalar ourMapCase = new mapOfScalarToScalar();
		ourMapCase.myString = "maps!";
		ourMapCase.ourMap.put("a", 1);
		ourMapCase.ourMap.put("b", 2);
		ourMapCase.ourMap.put("c", 3);
		
		CompositeInterpretation rootObject = new CompositeInterpretation("map_of_scalar_to_scalar");
		rootObject.addInterpretation(new ScalarInterpretation("myString", "maps!", "StringType"));
		
		// get the map type from the ourMap field... 
		ClassDescriptor cd = ClassDescriptors.getClassDescriptor(mapOfScalarToScalar.class);
		FieldDescriptor fd = cd.fields().getAllItems().get(0);
		MapType theMapType;
		
		MapInterpretation map = new MapInterpretation();
		map.addEntryInterpretation(string("a"), integ(1));
		map.addEntryInterpretation(string("b"), integ(2));
		map.addEntryInterpretation(string("c"), integ(3));
		
		map.setFieldName("ourMap");
		
		rootObject.addInterpretation(map);
		
		
		ISimplTypesScope sts = SimplTypesScopeFactory.name("mapScalarToScalar").translations(mapOfScalarToScalar.class).create();
		
		
		SimplUnderstander su = new SimplUnderstander(sts);
		
		
		mapOfScalarToScalar result = (mapOfScalarToScalar)su.understandInterpretation(rootObject);
		
		assertEquals(result.myString, ourMapCase.myString);
		assertEquals(3, result.ourMap.size());
		assertEquals(new Integer(1), result.ourMap.get("a"));
		assertEquals(new Integer(2), result.ourMap.get("b"));
		assertEquals(new Integer(3), result.ourMap.get("c"));
		
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
