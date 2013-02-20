package simpl.interpretation;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import simpl.descriptions.ClassDescriptors;
import simpl.exceptions.SIMPLTranslationException;

public class UnderstandingCompositesTest {

	
	
	@Test
	public void testUnderstandingOfBasicScalars() throws SIMPLTranslationException
	{
		myScalars orig = new myScalars();
		orig.aDouble = 1.3;
		orig.aInteger = 13;
		orig.aField = "string";
		
		
		List<SimplInterpretation> interps = new LinkedList<SimplInterpretation>();
		
		interps.add(new ScalarInterpretation("aInteger", orig.aInteger.toString()));
		interps.add(new ScalarInterpretation("aField", orig.aField));
		interps.add(new ScalarInterpretation("aDouble", orig.aDouble.toString()));
		
		SimplUnderstander su = new SimplUnderstander();
		Object result = su.understandInterpretation(interps, ClassDescriptors.getClassDescriptor(myScalars.class));
		
		myScalars r = (myScalars)result;
		assertEquals(new Double(1.3), r.aDouble);
		assertEquals(new Integer(13), r.aInteger);
		assertEquals("string", r.aField);
		
	}

	

}
