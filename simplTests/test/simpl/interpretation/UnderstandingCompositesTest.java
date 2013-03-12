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
		// this is just here for reference to show the type of object we're validating. 
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
	public void testUnderstandingOfEnumerations() throws SIMPLTranslationException
	{
		myEnumerations orig = new myEnumerations();
		orig.myString = "string";
		orig.primaryEnum = primaryScenarioEnum.firstValue;
		orig.secondaryEnum = secondaryScenarioEnum.secondValue;
		orig.secondaryEnumInts = secondaryScenarioEnum.thirdValue;
		
		List<SimplInterpretation> interps = new LinkedList<SimplInterpretation>();
		interps.add(new ScalarInterpretation("myString", "string", "StringType"));
		
		// I think we're going to treat enumerations as a scalar value
		// This is good b/c we can't really distinguish between an enum interpretation at the serialziation level
		// unless we happen to have the type information, which we really don't. 
		// This is better; we just delegate interp of enums to the scalar interpreation, it'll have to marshall via the STS, complicate some of the 
		// logic, but this will be for the best. 
		interps.add(new ScalarInterpretation("primaryEnum", "firstValue", "primary_scenario_enum"));
		interps.add(new ScalarInterpretation("secondaryEnum", "secondValue", "secondary_scenario_enum"));
		interps.add(new ScalarInterpretation("secondaryEnumInts", "7", "secondary_scenario_enum"));
	
		
		ISimplTypesScope context = SimplTypesScopeFactory.name("enumsEnumsENUMS").translations(myEnumerations.class, primaryScenarioEnum.class, secondaryScenarioEnum.class).create();
		
		SimplUnderstander su = new SimplUnderstander(context);
		
		myEnumerations result = (myEnumerations)su.understandInterpretation(interps, "my_enumerations");
		
		assertEquals(result.myString, orig.myString);
		assertEquals(result.primaryEnum, orig.primaryEnum);
		assertEquals(result.secondaryEnum, orig.secondaryEnum);
		assertEquals(result.secondaryEnumInts, orig.secondaryEnumInts);
	}
	

	@Test
	public void testUnderstandingOfComposites() throws SIMPLTranslationException
	{
		// this is just here for refernce to show the type of object we're validating. 
			
		plainComposite pc = new plainComposite();
		
		myScalars orig = new myScalars();
		orig.aDouble = 1.3; // aDouble , "1.3", DoubleType
		orig.aInteger = 13; // aInteger, "13",IntegerType.. 
		orig.aField = "string";
			
		pc.myComposite = orig;
		
		pc.myString = "string"; // scalar interpretation ->  myString, String, StringType
			
		List<SimplInterpretation> simplInterps = new LinkedList<SimplInterpretation>();
		simplInterps.add(new ScalarInterpretation("myString", "string", "StringType"));

				
		CompositeInterpretation ci = new CompositeInterpretation();
		ci.setFieldName("myComposite");
		ci.setTagName("my_scalars");
		
		List<SimplInterpretation> innerCompositeInterps = new LinkedList<SimplInterpretation>();
		innerCompositeInterps.add(new ScalarInterpretation("aInteger", orig.aInteger.toString(), "IntegerType"));
		innerCompositeInterps.add(new ScalarInterpretation("aField", orig.aField, "StringType"));
		innerCompositeInterps.add(new ScalarInterpretation("aDouble", orig.aDouble.toString(), "DoubleType"));
	
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
		// Example object for this test:
		nonCycleRef noCycles = new nonCycleRef();
		
		myScalars orig = new myScalars();
		orig.aDouble = 1.3;
		orig.aInteger = 13;
		orig.aField = "string";
		
		noCycles.left= orig;
		noCycles.right = orig;
		
		
		
		// Construction of the actual interpretation
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
		
		
		/// Done making our interpretations~!
		
		
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
	public void testUnderstandingWorksWithNestedComposites() throws SIMPLTranslationException
	{
		//Test 1: Object Person, referencing object child
		
		//Building Objects
		myScalars orig = new myScalars();
		orig.aField = "string";
		orig.aInteger = 314;
		orig.aDouble = 3.14;
		
		plainComposite pc = new plainComposite();
		pc.myComposite = orig;
		pc.myString = "string";
		
		OuterComposite oc = new OuterComposite();
		oc.ReferencedComp = pc;
		oc.myString = "string";
		
		//Building Interpretations:
		List<SimplInterpretation> outerInterps = new LinkedList<SimplInterpretation>();
		outerInterps.add(new ScalarInterpretation("myString", "string", "StringType"));
		
			CompositeInterpretation ci_pc = new CompositeInterpretation();
			ci_pc.setFieldName("ReferencedComp");
			ci_pc.setTagName("plain_composite");
				
				
				CompositeInterpretation ci_ms = new CompositeInterpretation();
				ci_ms.setFieldName("myComposite");
				ci_ms.setTagName("my_scalars");
				
					List<SimplInterpretation> innerCompositeInterps = new LinkedList<SimplInterpretation>();
					innerCompositeInterps.add(new ScalarInterpretation("aInteger", orig.aInteger.toString(), "IntegerType"));
					innerCompositeInterps.add(new ScalarInterpretation("aField", orig.aField, "StringType"));
					innerCompositeInterps.add(new ScalarInterpretation("aDouble", orig.aDouble.toString(), "DoubleType"));
						
				for(SimplInterpretation si : innerCompositeInterps)
				{
					ci_ms.addInterpretation(si);
				}
			ci_pc.addInterpretation(new ScalarInterpretation("myString", "string", "StringType"));
			ci_pc.addInterpretation(ci_ms);
		
		outerInterps.add(ci_pc);
			
		
		ISimplTypesScope context = SimplTypesScopeFactory.name("nestedCompositeUnderstanding").translations(plainComposite.class, myScalars.class, OuterComposite.class).create();
		SimplUnderstander su = new SimplUnderstander(context);
		
		Object result = su.understandInterpretation(outerInterps, "outer_composite");
		OuterComposite r = ((OuterComposite)result);
		
		assertEquals("OuterComposite.myString is incorrect", r.myString, oc.myString);
		assertNotNull("ReferencedComp should not be null", r.ReferencedComp);
		assertEquals("plainComposite.myString is incorrect", r.ReferencedComp.myString, pc.myString);
		assertNotNull("MyScalar should not be null", r.ReferencedComp.myComposite);
		assertEquals("MyScalar contains incorrect string", r.ReferencedComp.myComposite.aField, orig.aField);
		assertEquals("MyScalar contains incorrect int", r.ReferencedComp.myComposite.aDouble, orig.aDouble);
		assertEquals("MyScalar contains incorrect double", r.ReferencedComp.myComposite.aInteger, orig.aInteger);
	}
}
