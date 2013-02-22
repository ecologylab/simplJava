package simpl.interpretation;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import ecologylab.serialization.primaryScenarioEnum;
import ecologylab.serialization.secondaryScenarioEnum;

import simpl.descriptions.ClassDescriptors;
import simpl.exceptions.SIMPLTranslationException;

public class UnderstandingCompositesTest {
	
	@Test
	public void testUnderstandingOfBasicScalars() throws SIMPLTranslationException
	{
		// this is just here for refernce to show the type of object we're validating. 
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
	
	@Test
	public void testUnderstandingOfEnumerations()
	{
		myEnumerations orig = new myEnumerations();
		orig.myString = "string";
		orig.primaryEnum = primaryScenarioEnum.firstValue;
		orig.secondaryEnum = secondaryScenarioEnum.secondValue;
		orig.secondaryEnumInts = secondaryScenarioEnum.thirdValue;
		
		List<SimplInterpretation> interps = new LinkedList<SimplInterpretation>();

		fail("Implement this test");		
	}

	@Test
	public void testUnderstandingOfComposites() throws SIMPLTranslationException
	{
		// this is just here for refernce to show the type of object we're validating. 
			
		plainComposite pc = new plainComposite();
		
		myScalars orig = new myScalars();
		orig.aDouble = 1.3;
		orig.aInteger = 13;
		orig.aField = "string";
			
		pc.myComposite = orig;
		
		pc.myString = "string";
			
		List<SimplInterpretation> simplInterps = new LinkedList<SimplInterpretation>();
		simplInterps.add(new ScalarInterpretation("myString", "string"));
			
		List<SimplInterpretation> innerCompositeInterps = new LinkedList<SimplInterpretation>();
		innerCompositeInterps.add(new ScalarInterpretation("aInteger", orig.aInteger.toString()));
		innerCompositeInterps.add(new ScalarInterpretation("aField", orig.aField));
		innerCompositeInterps.add(new ScalarInterpretation("aDouble", orig.aDouble.toString()));
			
		CompositeInterpretation ci = new CompositeInterpretation();
		
		ci.setFieldName("myComposite");
		for(SimplInterpretation si : innerCompositeInterps)
		{
			ci.addInterpretation(si);
		}
		
		simplInterps.add(ci);
				
				SimplUnderstander su = new SimplUnderstander();
				
				Object result = su.understandInterpretation(simplInterps, ClassDescriptors.getClassDescriptor(plainComposite.class));
				
				
				assertEquals("string", ((plainComposite)result).myString);
				
				myScalars r = ((plainComposite)result).myComposite;
				assertEquals(new Double(1.3), r.aDouble);
				assertEquals(new Integer(13), r.aInteger);
				assertEquals("string", r.aField);
	}
	
	
	
	@Test
	public void testUnderstandingOfBasicRefs() throws SIMPLTranslationException
	{
		nonCycleRef noCycles = new nonCycleRef();
		
		myScalars orig = new myScalars();
		orig.aDouble = 1.3;
		orig.aInteger = 13;
		orig.aField = "string";
		
		noCycles.left= orig;
		noCycles.right = orig;
		
		
		List<SimplInterpretation> simplInterps = new LinkedList<SimplInterpretation>();
		simplInterps.add(new ScalarInterpretation("myString", "string"));
			
		CompositeInterpretation refInterp = new CompositeInterpretation();
		refInterp.setRefString("1");
		refInterp.setFieldName("right");
		
		simplInterps.add(refInterp);
		
		List<SimplInterpretation> innerCompositeInterps = new LinkedList<SimplInterpretation>();
		innerCompositeInterps.add(new ScalarInterpretation("aInteger", orig.aInteger.toString()));
		innerCompositeInterps.add(new ScalarInterpretation("aField", orig.aField));
		innerCompositeInterps.add(new ScalarInterpretation("aDouble", orig.aDouble.toString()));
			
		CompositeInterpretation idInterp = new CompositeInterpretation();
		
		idInterp.setFieldName("left");
		idInterp.setIDString("1");
		for(SimplInterpretation si : innerCompositeInterps)
		{
			idInterp.addInterpretation(si);
		}
		
		simplInterps.add(idInterp);
		
		SimplUnderstander su = new SimplUnderstander();
		
		Object result = su.understandInterpretation(simplInterps, ClassDescriptors.getClassDescriptor(nonCycleRef.class));
		
		assertEquals("string", ((nonCycleRef)result).myString);
		
		myScalars r = ((nonCycleRef)result).left;
		assertEquals(new Double(1.3), r.aDouble);
		assertEquals(new Integer(13), r.aInteger);
		assertEquals("string", r.aField);

		r = ((nonCycleRef)result).right;
		assertEquals(new Double(1.3), r.aDouble);
		assertEquals(new Integer(13), r.aInteger);
		assertEquals("string", r.aField);
	}
	
	@Test
	public void testUnderstandingWorksWithNestedComposites()
	{
		
	}
	
	@Test
	public void testUnderstandingWorksWithCycles()
	{
		
	}
}
