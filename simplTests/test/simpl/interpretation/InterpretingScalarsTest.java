package simpl.interpretation;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import simpl.core.ISimplTypesScope;
import simpl.core.SimplTypesScopeFactory;
import simpl.descriptions.ClassDescriptors;

public class InterpretingScalarsTest {
	
	@Test
	public void testInterpretScalars() throws Exception{
		
		myScalars orig = new myScalars();
		orig.aDouble = 1.3;
		orig.aInteger = 13;
		orig.aField = "string";
		
		ISimplTypesScope context = SimplTypesScopeFactory.name("testUnderstandingOfScalars").translations(myScalars.class).create();
		
		SimplInterpreter si = new SimplInterpreter();
		
		SimplInterpretation interpretation = si.interpretInstance(orig);
		
		assertNotNull("Interpretation should not be null!", interpretation);
		assertTrue("Expect interpretation of the root object to be a composite", interpretation instanceof CompositeInterpretation);
		
		CompositeInterpretation ci = (CompositeInterpretation)interpretation;
		assertEquals("my_scalars", ci.getTagName());
		assertEquals(3, ci.interpretations.size());
		
		SimplUnderstander su = new SimplUnderstander(context);
		
		Object result = su.understandInterpretation((CompositeInterpretation)interpretation);
				
		myScalars r = (myScalars)result;
		assertEquals(orig.aDouble, r.aDouble);
		assertEquals(orig.aInteger, r.aInteger);
		assertEquals(orig.aField, r.aField);	
	
	}

}
