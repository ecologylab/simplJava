package simpl.interpretation;

import static org.junit.Assert.*;

import org.junit.Test;

import simpl.core.ISimplTypesScope;
import simpl.core.SimplTypesScopeFactory;

public class InterpretingListsTest {

	@Test
	public void testInterpretScalarList() throws Exception {
		
		ISimplTypesScope context = SimplTypesScopeFactory.name("listinterp").translations(listOfScalars.class).create();
		
		listOfScalars los = new listOfScalars();
		
		los.myList.add(0);
		los.myList.add(1);
		los.myList.add(2);
		los.myList.add(3);

		CompositeInterpretation ci = new CompositeInterpretation("list_of_scalars");
		
		ListInterpretation li = new ListInterpretation(los.myList.getClass());
		li.addItemInterpretation(new ScalarInterpretation("", "0", "IntegerType"));
		li.addItemInterpretation(new ScalarInterpretation("", "1", "IntegerType"));
		li.addItemInterpretation(new ScalarInterpretation("", "2", "IntegerType"));
		li.addItemInterpretation(new ScalarInterpretation("", "3", "IntegerType"));
		li.setFieldName("myList");
		
		ci.addInterpretation(ci);
		
		SimplInterpreter interpreter = new SimplInterpreter();
		
		SimplUnderstander understander = new SimplUnderstander(context);
		
		CompositeInterpretation rootInterp = (CompositeInterpretation)interpreter.interpretInstance(los);
		assertEquals(2,rootInterp.interpretations.size());
		ListInterpretation listInterp = (ListInterpretation)rootInterp.interpretations.get(0);
		
		assertEquals(4, listInterp.getInterpretations().size());
		assertEquals("list_of_scalars",rootInterp.getTagName());
		
		
		
		Object result = understander.understandInterpretation(rootInterp);

		
		
		assertNotNull(result);
		
		listOfScalars theResult = (listOfScalars)result;
		
		assertEquals(theResult.myString, los.myString);
		assertEquals(theResult.myList.size(), los.myList.size());
		for(int i = 0; i < theResult.myList.size(); i++)
		{
			assertEquals(theResult.myList.get(i), los.myList.get(i));
		}
	}

}
