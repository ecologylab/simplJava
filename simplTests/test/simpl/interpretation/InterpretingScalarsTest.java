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
		
		List<SimplInterpretation> interpretations = si.interpretInstance(orig);
		
		SimplUnderstander su = new SimplUnderstander(context);
		String tagName = ClassDescriptors.getClassDescriptor(myScalars.class).getTagName();
		assertNotNull(tagName);
		
		Object result = su.understandInterpretation(interpretations, tagName);
		
		myScalars r = (myScalars)result;
		assertEquals(orig.aDouble, r.aDouble);
		assertEquals(orig.aInteger, r.aInteger);
		assertEquals(orig.aField, r.aField);	
	
	}

}
