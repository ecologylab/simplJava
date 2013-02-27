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
		
		los.myString = "string";

		SimplInterpreter interpreter = new SimplInterpreter();
		
		SimplUnderstander understander = new SimplUnderstander(context);
		
		Object result = understander.understandInterpretation(interpreter.interpretInstance(los), "list_of_scalars");

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
