package simpl.interpretation;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import ecologylab.serialization.primaryScenarioEnum;
import ecologylab.serialization.secondaryScenarioEnum;

import simpl.core.ISimplTypesScope;
import simpl.core.SimplTypesScopeFactory;
import simpl.descriptions.ClassDescriptor;
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
		
		interps.add(new ScalarInterpretation("aInteger", orig.aInteger.toString(), "IntegerType"));
		interps.add(new ScalarInterpretation("aField", orig.aField, "StringType"));
		interps.add(new ScalarInterpretation("aDouble", orig.aDouble.toString(), "DoubleType"));
		
		ISimplTypesScope context = SimplTypesScopeFactory.name("testUnderstandingOfScalars").translations(myScalars.class).create();
		
		SimplUnderstander su = new SimplUnderstander(context);
		String tagName = ClassDescriptors.getClassDescriptor(myScalars.class).getTagName();
		assertNotNull(tagName);
		
		Object result = su.understandInterpretation(interps, tagName);
		
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
		simplInterps.add(new ScalarInterpretation("myString", "string", "StringType"));
			
		List<SimplInterpretation> innerCompositeInterps = new LinkedList<SimplInterpretation>();
		innerCompositeInterps.add(new ScalarInterpretation("aInteger", orig.aInteger.toString(), "IntegerType"));
		innerCompositeInterps.add(new ScalarInterpretation("aField", orig.aField, "StringType"));
		innerCompositeInterps.add(new ScalarInterpretation("aDouble", orig.aDouble.toString(), "DoubleType"));
			
		CompositeInterpretation ci = new CompositeInterpretation();

		ci.setFieldName("myComposite");
		ci.setTagName("my_scalars");
		for(SimplInterpretation si : innerCompositeInterps)
		{
			ci.addInterpretation(si);
		}
		
		simplInterps.add(ci);
				
		ISimplTypesScope context = SimplTypesScopeFactory.name("compositeUndersatnding").translations(plainComposite.class, myScalars.class).create();
		ClassDescriptor cd = ClassDescriptors.getClassDescriptor(plainComposite.class);
		assertNotNull(cd);
		assertNotNull(cd.getTagName());
		assertEquals("plain_composite", cd.getTagName());
		
		
		assertNotNull(context.getClassDescriptorByTag("plain_composite"));
		

				SimplUnderstander su = new SimplUnderstander(context);
				
				assertNotNull("Null tag name on composite class!", ClassDescriptors.getClassDescriptor(plainComposite.class).getTagName());
				assertEquals("plain_composite",ClassDescriptors.getClassDescriptor(plainComposite.class).getTagName());
				
				Object result = su.understandInterpretation(simplInterps, ClassDescriptors.getClassDescriptor(plainComposite.class).getTagName());
				
				
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
		simplInterps.add(new ScalarInterpretation("myString", "string", "StringType"));
			
		CompositeInterpretation refInterp = new CompositeInterpretation();
		refInterp.setTagName("my_scalars");
		refInterp.setRefString("1");
		refInterp.setFieldName("right");
		
		simplInterps.add(refInterp);
		
		List<SimplInterpretation> innerCompositeInterps = new LinkedList<SimplInterpretation>();
		innerCompositeInterps.add(new ScalarInterpretation("aInteger", orig.aInteger.toString(), "IntegerType"));
		innerCompositeInterps.add(new ScalarInterpretation("aField", orig.aField, "StringType"));
		innerCompositeInterps.add(new ScalarInterpretation("aDouble", orig.aDouble.toString(), "DoubleType"));
			
		CompositeInterpretation idInterp = new CompositeInterpretation();
		
		idInterp.setTagName("my_scalars");
		idInterp.setFieldName("left");
		idInterp.setIDString("1");
		for(SimplInterpretation si : innerCompositeInterps)
		{
			idInterp.addInterpretation(si);
		}
		
		simplInterps.add(idInterp);
		
		ISimplTypesScope context = SimplTypesScopeFactory.name("compositeWithBasicRefsTest").translations(myScalars.class, nonCycleRef.class).create();
		SimplUnderstander su = new SimplUnderstander(context);
		
		Object result = su.understandInterpretation(simplInterps, ClassDescriptors.getClassDescriptor(nonCycleRef.class).getTagName());
		
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
		fail("Not implemented yet");
	}
	
	@Test
	public void testUnderstandingWorksWithCycles()
	{
		fail("not implemented yet");
	}
}
