package simpl.interpretation;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import simpl.core.ISimplTypesScope;
import simpl.core.SimplTypesScopeFactory;
import simpl.exceptions.SIMPLTranslationException;

public class UnderstandingListsTest {

	@Test
	public void testUnderstandsABasicScalarList() throws SIMPLTranslationException {
		// just here for reference. 
		listOfScalars los = new listOfScalars();
		
		los.myList.add(0);
		los.myList.add(1);
		los.myList.add(2);
		los.myList.add(3);
		
		los.myString = "string";

		List<SimplInterpretation> interps = new LinkedList<SimplInterpretation>();
		interps.add(new ScalarInterpretation("myString", "string", "StringType"));
		
		ListInterpretation li = new ListInterpretation();
		li.setFieldName("myList");
		li.addItemInterpretation(new ScalarInterpretation("", "0", "IntegerType"));
		li.addItemInterpretation(new ScalarInterpretation("", "1", "IntegerType"));
		li.addItemInterpretation(new ScalarInterpretation("", "2", "IntegerType"));
		li.addItemInterpretation(new ScalarInterpretation("", "3", "IntegerType"));

		interps.add(li);
		
		ISimplTypesScope context = SimplTypesScopeFactory.name("scalarListTest").translations(listOfScalars.class).create();
		
		SimplUnderstander su = new SimplUnderstander(context);
		Object result = su.understandInterpretation(interps, "list_of_scalars");
		assertNotNull(result);
		
		listOfScalars losResult = (listOfScalars)result;
		
		assertEquals("string", losResult.myString);
		assertEquals(4, losResult.myList.size());
		
		List<Integer> toValidate = losResult.myList;
		
		for(int i = 0; i < 4; i++)
		{
			assertEquals(new Integer(i), toValidate.get(i));
		}		
	}
	
	private simplerInnerListComposite makeComposite(Integer i)
	{
		simplerInnerListComposite silc = new simplerInnerListComposite();
		silc.myInt = i;
		return silc;
	}
	
	private CompositeInterpretation makeCompositeInterp(Integer i)
	{
		CompositeInterpretation ourInterp = new CompositeInterpretation();
		ourInterp.tagName = "simpler_inner_list_composite";
		ourInterp.fieldName = "";
		ourInterp.idString = "";
		ourInterp.refString = "";
		ourInterp.addInterpretation(new ScalarInterpretation("myInt", i.toString(), "IntegerType"));
		
		return ourInterp;
	}
	
	@Test
	public void testUnderstandsABasicCompositeList() throws SIMPLTranslationException
	{
		List<simplerInnerListComposite> ourList = new ArrayList<simplerInnerListComposite>();
		for(Integer i = 0; i < 4; i++)
		{
			ourList.add(makeComposite(i));
		}
		
		listOfComposites loc = new listOfComposites();
		
		loc.listOfComposites = ourList;
		
		loc.myString = "string";
		
		
		List<SimplInterpretation> interps = new LinkedList<SimplInterpretation>();
		interps.add(new ScalarInterpretation("myString", "string", "StringType"));
		
		ListInterpretation li = new ListInterpretation();
		li.setFieldName("listOfComposites");
		for(Integer i = 0; i < 4 ; i ++)
		{
			li.addItemInterpretation(makeCompositeInterp(i));
		}
		
		interps.add(li);
		
		ISimplTypesScope context = SimplTypesScopeFactory.name("compositeListUnderstanding").translations(simplerInnerListComposite.class, listOfComposites.class).create();
		
		SimplUnderstander su = new SimplUnderstander(context);
		
		Object result = su.understandInterpretation(interps, "list_of_composites");
		
		assertNotNull(result);
		
		listOfComposites locResult = (listOfComposites)result;
		
		assertEquals("string", locResult.myString);
		assertEquals(4, locResult.listOfComposites.size());
		
		for(Integer i = 0; i<4; i++)
		{

			simplerInnerListComposite item = locResult.listOfComposites.get(i);
			
			assertEquals(i, item.myInt);
		}
	}
	
	@Test
	public void testUnderstandingOfListsWithInternalCycles()
	{
		fail("Implement this test");
	}
	
	@Test
	public void testUnderstandingOfListsOfListsOfCyclicComposites()
	{
		fail("implement this test");
	}
	
	@Test
	public void testUnderstandingOfListsOfLists() throws SIMPLTranslationException
	{

		listOfListsOfScalars example = new listOfListsOfScalars();
		// todo: populate an example here so that it can be interpreted. :) 
		
		ListInterpretation li = new ListInterpretation();
		li.setFieldName("listOfLists");
		
		// Let's make the list [[0,1,2,3],[1,2,3,4]]
		ListInterpretation li1 = new ListInterpretation();
		li1.addItemInterpretation(new ScalarInterpretation("", "0", "IntegerType"));
		li1.addItemInterpretation(new ScalarInterpretation("", "1", "IntegerType"));
		li1.addItemInterpretation(new ScalarInterpretation("", "2", "IntegerType"));
		li1.addItemInterpretation(new ScalarInterpretation("", "3", "IntegerType"));

		assertEquals(4, li1.size());
		Object li1Result = li1.getValue(null, null, null);
		
		// we expect the inner interpretation to provide the list [0,1,2,3]
		
		List ourList = null;
		try{
			ourList = (List)li1Result;
		}catch(RuntimeException e)
		{
			fail(e.getMessage() + " || Error at casting!");
		}
		
		assertNotNull(ourList);
		assertFalse("Inner list should not be empty!", ourList.isEmpty());
		assertEquals(4, ourList.size());
		
		for(Integer i = 0; i < 4; i++)
		{
			assertEquals(new Integer(i), (Integer)ourList.get(i));
		}
		
		ListInterpretation li2 = new ListInterpretation();
		li2.addItemInterpretation(new ScalarInterpretation("", "1", "IntegerType"));
		li2.addItemInterpretation(new ScalarInterpretation("", "2", "IntegerType"));
		li2.addItemInterpretation(new ScalarInterpretation("", "3", "IntegerType"));
		li2.addItemInterpretation(new ScalarInterpretation("", "4", "IntegerType"));

		Object li2Result = li2.getValue(null, null, null);
		
		li.addItemInterpretation(li1);
		li.addItemInterpretation(li2);
		
		List<SimplInterpretation> interps = new LinkedList<SimplInterpretation>();
		interps.add(new ScalarInterpretation("myString", "string", "StringType"));
		interps.add(li);
		
		ISimplTypesScope context = SimplTypesScopeFactory.name("ListOfListTests").translations(listOfListsOfScalars.class).create();	
		
		SimplUnderstander su = new SimplUnderstander(context);
		
		Object result = su.understandInterpretation(interps, "list_of_lists_of_scalars");
		
		assertNotNull(result);
		
		listOfListsOfScalars llResult = (listOfListsOfScalars)result;
		
		assertEquals("string", llResult.myString);
		
		for(Integer listID = 0; listID < 2; listID++)
		{
			assertEquals("Size incorrect on list : " + listID.toString(), 4, llResult.listOfLists.get(listID).size());
			
			for(Integer listIndex = 0; listIndex < 4; listIndex++)
			{
				// list 0 = 0,1,2,3
				// list 1 = 1,2,3,4
				// so asserting id+index gives us a value validation
				assertEquals(new Integer(listID+listIndex), llResult.listOfLists.get(listID).get(listIndex));
			}
		}
	}
	
	@Test
	public void testListOfMapsWorksCorrectly()
	{
		fail("Implement this test");
	}
	
	@Test
	public void testListOfEnumerationsWorksCorrectly()
	{
		fail("Implement this test");
	}
}
