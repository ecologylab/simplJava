package ecologylab.simpl;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.primaryScenarioEnum;
import ecologylab.serialization.secondaryScenarioEnum;
import ecologylab.serialization.formatenums.StringFormat;

public class ListDeSerializationTests {

	
	private static SimplTypesScope ourSTS = SimplTypesScope.get("enumTestsDeSerialize", primaryScenarioEnum.class, secondaryScenarioEnum.class,
			customValuedEnumerationScalar.class, basicEnumerationScalar.class, basicEnumerationList.class, basicScalarList.class, basicComposite.class, basicCompositeList.class); 
	@Test
	public void EnumerationListsSerialize()
	{
		fail("You haven't picked a format yet, Tom. Pick one.");
	}
	
	@Test
	public void EnumerationListsDeserialize() throws SIMPLTranslationException
	{
		List<primaryScenarioEnum> ourList = new ArrayList<primaryScenarioEnum>();
		ourList.add(primaryScenarioEnum.firstValue);
		ourList.add(primaryScenarioEnum.secondValue);
		ourList.add(primaryScenarioEnum.thirdValue);
		
		basicEnumerationList bel = new basicEnumerationList();
		bel.ourList = ourList;
		
		StringBuilder result = ourSTS.serialize(bel, StringFormat.XML);
		basicEnumerationList after = (basicEnumerationList) ourSTS.deserialize(result.toString(), StringFormat.XML);
		
		assertNotNull(after.ourList);
		assertEquals(3, after.ourList.size());
		for(int i = 0; i < 3; i++)
		{
			assertEquals(bel.ourList.get(i), after.ourList.get(i));
		}
	}
	
	
	@Test
	public void ScalarListsDeserializeXML() throws SIMPLTranslationException
	{
		testScalarListSerialization(StringFormat.XML);
	}
	
	@Test
	public void ScalarListsDeserializeJSON() throws SIMPLTranslationException
	{
		testScalarListSerialization(StringFormat.JSON);
	}
	
	private void testScalarListSerialization(StringFormat format) throws SIMPLTranslationException
	{
		List<Integer> myList = new ArrayList<Integer>();
		myList.add(0);
		myList.add(1);
		myList.add(2);
		
		basicScalarList bel = new basicScalarList();
		bel.ourList = myList; 
		
		StringBuilder result = ourSTS.serialize(bel, format);
		
		System.out.println(result.toString());
		
		basicScalarList after = (basicScalarList) ourSTS.deserialize(result.toString(), format);
		
		assertNotNull(after.ourList);
		assertEquals("Missing elements from the list!", 3, after.ourList.size());
		for(int i = 0; i < 3; i++)
		{
			assertEquals(bel.ourList.get(i), after.ourList.get(i));
		}
	}
	
	@Test 
	public void basicCompositeDeserializesXML() throws SIMPLTranslationException
	{
		for(int i = -3; i < 4; i++)
		{
			testABasicComposite(i, StringFormat.XML);
		}
	}
	
	@Test 
	public void basicCompositeDeserializesJSON() throws SIMPLTranslationException
	{
		for(int i = -3; i < 4; i++)
		{
			testABasicComposite(i, StringFormat.JSON);
		}
	}
	
	private void testABasicComposite(int i, StringFormat format) throws SIMPLTranslationException
	{
		basicComposite bc = new basicComposite(i);
		
		assertEquals("value in bc.a was incorrect.", new Integer(i),bc.a);
		assertEquals("value in bc.b was incorrect.", i+1,bc.b);
	
		StringBuilder result = ourSTS.serialize(bc, format);
		basicComposite after = (basicComposite) ourSTS.deserialize(result.toString(), format);
		
		assertEquals("Values incorrect for a after roundtrip.", bc.a, after.a);
		assertEquals("Values incorrect for b after roundtrip.", bc.b, after.b);
	}
	
	@Test
	public void CompositeListsDeserializeJSON() throws SIMPLTranslationException
	{
		testCompositeList(StringFormat.JSON);
	}
	
	@Test
	public void CompositeListsDeserializeXML() throws SIMPLTranslationException
	{
		testCompositeList(StringFormat.XML);
	}
	
	
	private void testCompositeList(StringFormat format) throws SIMPLTranslationException
	{
		List<basicComposite> myList = new ArrayList<basicComposite>();
		myList.add(new basicComposite(0));
		myList.add(new basicComposite(1));
		myList.add(new basicComposite(2));
		
		basicCompositeList bel = new basicCompositeList();
		bel.ourList = myList; 
		
		StringBuilder result = ourSTS.serialize(bel, format);
		basicCompositeList after = (basicCompositeList) ourSTS.deserialize(result.toString(), format);
		
		assertNotNull(after.ourList);
		assertEquals("Missing elements from the list!",3, after.ourList.size());
		for(int i = 0; i < 3; i++)
		{
			assertEquals("Values incorrect for rountrip for a: ", bel.ourList.get(i).a, after.ourList.get(i).a);			
			assertEquals("Values incorrect for rountrip for b: ", bel.ourList.get(i).b, after.ourList.get(i).b);
		}
	}
}
