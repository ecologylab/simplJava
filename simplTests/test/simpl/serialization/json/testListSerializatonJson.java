package simpl.serialization.json;

import static org.junit.Assert.*;

import org.junit.Test;

import simpl.core.ISimplTypesScope;
import simpl.core.SimplTypesScopeFactory;
import simpl.exceptions.SIMPLTranslationException;
import simpl.interpretation.CompositeInterpretation;
import simpl.interpretation.ListInterpretation;
import simpl.interpretation.ScalarInterpretation;
import simpl.interpretation.SimplInterpreter;
import simpl.interpretation.SimplUnderstander;
import simpl.interpretation.listOfScalars;

public class testListSerializatonJson {

	@Test
	public void testScalarListSerialization() throws SIMPLTranslationException {
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
		
		ci.addInterpretation(li);

		JsonSerializer jsonSer = new JsonSerializer();
		String res = jsonSer.serialize(ci);
		
		assertEquals("{\"list_of_scalars\":{\"myList\":[\"0\",\"1\",\"2\",\"3\"]}}", res);
	}
	
	@Test
	public void testCompositeListSerialization() throws SIMPLTranslationException {
		
		
		
	}
	
	
	@Test
	public void testEnumerationListSerialization() throws SIMPLTranslationException
	{
		
		
		
	}

}
