package simpl.interpretation;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class UnderstandingListsTest {

	@Test
	public void testUnderstandsABasicScalarList() {
		// just here for reference. 
		listOfScalars los = new listOfScalars();
		
		los.myList.add(0);
		los.myList.add(1);
		los.myList.add(2);
		los.myList.add(3);
		
		los.myString = "string";

		List<SimplInterpretation> interps = new LinkedList<SimplInterpretation>();
		
		
		ListInterpretation li = new ListInterpretation();
		li.setFieldName("myList");
		li.addItemInterpretation(new ScalarInterpretation("name","value", "StringType"));
		// probably can marshal inner type data through the list type! :D 
		// How will that work for composites?

		
		fail("implement this test");
		
	}

}
