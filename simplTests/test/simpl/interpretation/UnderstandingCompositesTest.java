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
		
		
		CompositeInterpretation rootObject = new CompositeInterpretation("my_scalars");
		
		List<SimplInterpretation> interps = new LinkedList<SimplInterpretation>();
		
		interps.add(new ScalarInterpretation("aInteger", orig.aInteger.toString(), "IntegerType"));
		interps.add(new ScalarInterpretation("aField", orig.aField, "StringType"));
		interps.add(new ScalarInterpretation("aDouble", orig.aDouble.toString(), "DoubleType"));
		
		rootObject.addInterpretations(interps);
		
		ISimplTypesScope context = SimplTypesScopeFactory.name("testUnderstandingOfScalars").translations(myScalars.class).create();
		
		SimplUnderstander su = new SimplUnderstander(context);
		String tagName = ClassDescriptors.getClassDescriptor(myScalars.class).getTagName();
		assertNotNull(tagName);
		
		Object result = su.understandInterpretation(rootObject);
		
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
		
		CompositeInterpretation rootObject = new CompositeInterpretation("my_enumerations");
		
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
	
		rootObject.addInterpretations(interps);
		
		ISimplTypesScope context = SimplTypesScopeFactory.name("enumsEnumsENUMS").translations(myEnumerations.class, primaryScenarioEnum.class, secondaryScenarioEnum.class).create();
		
		SimplUnderstander su = new SimplUnderstander(context);
		
		myEnumerations result = (myEnumerations)su.understandInterpretation(rootObject);
		
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
			
		CompositeInterpretation rootObject = new CompositeInterpretation("plain_composite");
		
		List<SimplInterpretation> simplInterps = new LinkedList<SimplInterpretation>();
		simplInterps.add(new ScalarInterpretation("myString", "string", "StringType"));

				
		CompositeInterpretation ci = new CompositeInterpretation("my_scalars");
		ci.setFieldName("myComposite");
		
		List<SimplInterpretation> innerCompositeInterps = new LinkedList<SimplInterpretation>();
		innerCompositeInterps.add(new ScalarInterpretation("aInteger", orig.aInteger.toString(), "IntegerType"));
		innerCompositeInterps.add(new ScalarInterpretation("aField", orig.aField, "StringType"));
		innerCompositeInterps.add(new ScalarInterpretation("aDouble", orig.aDouble.toString(), "DoubleType"));
		
		ci.addInterpretations(innerCompositeInterps);
	
		simplInterps.add(ci);
		
		rootObject.addInterpretations(simplInterps);
		
		ISimplTypesScope context = SimplTypesScopeFactory.name("compositeUndersatnding").translations(plainComposite.class, myScalars.class).create();
		ClassDescriptor cd = ClassDescriptors.getClassDescriptor(plainComposite.class);
		assertNotNull(cd);
		assertNotNull(cd.getTagName());
		assertEquals("plain_composite", cd.getTagName());
		
		
		assertNotNull(context.getClassDescriptorByTag("plain_composite"));
		

				SimplUnderstander su = new SimplUnderstander(context);
				
				assertNotNull("Null tag name on composite class!", ClassDescriptors.getClassDescriptor(plainComposite.class).getTagName());
				assertEquals("plain_composite",ClassDescriptors.getClassDescriptor(plainComposite.class).getTagName());
				
				Object result = su.understandInterpretation(rootObject);
				
				
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
		
		CompositeInterpretation rootObject = new CompositeInterpretation("non_cycle_ref");
		
		// Construction of the actual interpretation
		List<SimplInterpretation> simplInterps = new LinkedList<SimplInterpretation>();
		simplInterps.add(new ScalarInterpretation("myString", "string", "StringType"));
			
		CompositeInterpretation refInterp = new CompositeInterpretation("my_scalars");
		refInterp.setRefString("1");
		refInterp.setFieldName("right");
		
		simplInterps.add(refInterp);
		
		List<SimplInterpretation> innerCompositeInterps = new LinkedList<SimplInterpretation>();
		innerCompositeInterps.add(new ScalarInterpretation("aInteger", orig.aInteger.toString(), "IntegerType"));
		innerCompositeInterps.add(new ScalarInterpretation("aField", orig.aField, "StringType"));
		innerCompositeInterps.add(new ScalarInterpretation("aDouble", orig.aDouble.toString(), "DoubleType"));
			
		CompositeInterpretation idInterp = new CompositeInterpretation("my_scalars");
		
		idInterp.setTagName("my_scalars");
		idInterp.setFieldName("left");
		idInterp.setIDString("1");
		for(SimplInterpretation si : innerCompositeInterps)
		{
			idInterp.addInterpretation(si);
		}
		
		simplInterps.add(idInterp);
		
		rootObject.addInterpretations(simplInterps);
		/// Done making our interpretations~!
		
		
		ISimplTypesScope context = SimplTypesScopeFactory.name("compositeWithBasicRefsTest").translations(myScalars.class, nonCycleRef.class).create();
	
		
		SimplUnderstander su = new SimplUnderstander(context);
		
		Object result = su.understandInterpretation(rootObject);
		
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
		
		
		CompositeInterpretation rootObject = new CompositeInterpretation("outer_composite");
		
		List<SimplInterpretation> outerInterps = new LinkedList<SimplInterpretation>();
		outerInterps.add(new ScalarInterpretation("myString", "string", "StringType"));
		
		CompositeInterpretation ci_pc = new CompositeInterpretation("plain_composite");
		ci_pc.setFieldName("ReferencedComp");
			
			
		CompositeInterpretation ci_ms = new CompositeInterpretation("my_scalars");
		ci_ms.setFieldName("myComposite");
		
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
			
		rootObject.addInterpretations(outerInterps);
		
		ISimplTypesScope context = SimplTypesScopeFactory.name("nestedCompositeUnderstanding").translations(plainComposite.class, myScalars.class, OuterComposite.class).create();
		SimplUnderstander su = new SimplUnderstander(context);
		
		Object result = su.understandInterpretation(rootObject);
		
		OuterComposite r = ((OuterComposite)result);
		
		assertEquals("OuterComposite.myString is incorrect", r.myString, oc.myString);
		assertNotNull("ReferencedComp should not be null", r.ReferencedComp);
		assertEquals("plainComposite.myString is incorrect", r.ReferencedComp.myString, pc.myString);
		assertNotNull("MyScalar should not be null", r.ReferencedComp.myComposite);
		assertEquals("MyScalar contains incorrect string", r.ReferencedComp.myComposite.aField, orig.aField);
		assertEquals("MyScalar contains incorrect int", r.ReferencedComp.myComposite.aDouble, orig.aDouble);
		assertEquals("MyScalar contains incorrect double", r.ReferencedComp.myComposite.aInteger, orig.aInteger);
	}

	@Test
	public void testUnderstandingOfSelfReference() throws SIMPLTranslationException
	{
		//Build Object A
		hardCompositeNode A = new hardCompositeNode();
		A.myString = "Astring";
		A.right = A;
		
		CompositeInterpretation rootObject = new CompositeInterpretation("hard_composite_node");
		rootObject.setIDString("1");		
		rootObject.addInterpretation(new ScalarInterpretation("myString", "Astring", "StringType"));
		
			CompositeInterpretation refInterp = new CompositeInterpretation("hard_composite_node");
			refInterp.setFieldName("right");
			refInterp.setRefString("1");
			
		rootObject.addInterpretation(refInterp);	
		
		//Composite references itself. Cycle of 1
			
		//Setup:
			ISimplTypesScope context = SimplTypesScopeFactory.name("selfReferenceUnderstanding").translations(hardCompositeNode.class).create();
			SimplUnderstander su = new SimplUnderstander(context);
		
		Object result = su.understandInterpretation(rootObject);
		hardCompositeNode r = ((hardCompositeNode)result);
		
		assertEquals("myString is incorrect", r.myString, A.myString);

		assertNotNull("right should not be null!", r.right);
		assertTrue("r.right should be the same object as r", r.right==r);
		assertNull("left is not null, and should be", r.left);
	}
	
	@Test
	public void testUnderstandingOfMutualReference() throws SIMPLTranslationException //Two nodes mutually referencing each other. A node should be its own grandchild
	{
		//Build Object A,B
		hardCompositeNode A = new hardCompositeNode();
		A.myString = "Astring";
		
		hardCompositeNode B = new hardCompositeNode();
		B.myString = "Bstring";
		
		A.right = B;
		B.right = A;
		
		CompositeInterpretation rootObject = new CompositeInterpretation("hard_composite_node"); // we'll treat A as the root node. 
		rootObject.addInterpretation(new ScalarInterpretation("myString", "Astring", "StringType"));
		rootObject.setIDString("1");
		
		CompositeInterpretation BInterp = new CompositeInterpretation("hard_composite_node");
		BInterp.setFieldName("right");
		BInterp.addInterpretation(new ScalarInterpretation("myString", "Bstring", "StringType"));
		
		CompositeInterpretation referenceToAinB = new CompositeInterpretation("hard_composite_node");
		referenceToAinB.setRefString("1");
		referenceToAinB.setFieldName("right");
		BInterp.addInterpretation(referenceToAinB);
				
		//Setup:
		ISimplTypesScope context = SimplTypesScopeFactory.name("mutualReferenceUnderstanding").translations(hardCompositeNode.class).create();
		SimplUnderstander su = new SimplUnderstander(context);
				
		Object result = su.understandInterpretation(rootObject);
		hardCompositeNode r = ((hardCompositeNode)result); //r should equal
		
		assertEquals("myString of A does not match that of r", A.myString, r.myString);
		assertEquals("myString of B does not match that of r's child", B.myString, r.right.myString);
		assertEquals("myString of A does not match that of r's grandchild", A.myString, r.right.right.myString);
		assertNull("A's left is not null, and should be", A.left);
		assertNull("B's left is not null, and should be", B.left);
		
		//Two composites reference each other. Cycle of 2
	}
	
	@Test
	public void testUnderstandingOfDirectedLoop() throws SIMPLTranslationException
	{
		//Three composites, each referencing the one to the "left". Directed graph around a group of 3. A node should be its own great grandchild
		hardCompositeNode A = new hardCompositeNode();
		A.myString = "Astring";
		hardCompositeNode B = new hardCompositeNode();
		B.myString = "Bstring";
		hardCompositeNode C = new hardCompositeNode();
		C.myString = "Cstring";
		
		A.right = B;
		B.right = C;
		C.right = A;
		
		CompositeInterpretation rootObject = new CompositeInterpretation("hard_composite_node");
		rootObject.addInterpretation(new ScalarInterpretation("myString", "Astring", "StringType"));
		rootObject.setIDString("1");
		
		CompositeInterpretation bObject = new CompositeInterpretation("hard_composite_node");
		bObject.setFieldName("right");
		bObject.addInterpretation(new ScalarInterpretation("myString", "Bstring", "StringType"));
		
		rootObject.addInterpretation(bObject);
		
		CompositeInterpretation cObject = new CompositeInterpretation("hard_composite_node");
		cObject.setFieldName("right");
		cObject.addInterpretation(new ScalarInterpretation("myString", "Cstring", "StringType"));
		
		CompositeInterpretation referenceToAinC = new CompositeInterpretation("hard_composite_node");
		referenceToAinC.setFieldName("right");
		referenceToAinC.setRefString("1");
		
		cObject.addInterpretation(referenceToAinC);
		
		bObject.addInterpretation(cObject);
				
		//Setup:
		ISimplTypesScope context = SimplTypesScopeFactory.name("directedLoopReferenceUnderstanding").translations(hardCompositeNode.class).create();
		SimplUnderstander su = new SimplUnderstander(context);
				
		Object result = su.understandInterpretation(rootObject);
		hardCompositeNode r = ((hardCompositeNode)result); //r should equal
		
		assertEquals("myString of A does not match that of r", A.myString, r.myString);
		assertEquals("myString of B does not match that of r's child", B.myString, r.right.myString);
		assertEquals("myString of C does not match that of r's grandchild", C.myString, r.right.right.myString);
		assertEquals("myString of A does not match that of r's greatgrandchild", A.myString, r.right.right.right.myString);
		assertNull("left of A should be null", A.left);
		assertNull("left of B should be null", B.left);
		assertNull("left of C should be null", C.left);
		//Two composites reference each other. Cycle of 2
	}
	
	@Test
	public void testUnderstandingOfSpecialCase() throws SIMPLTranslationException //Still needs work
	{
		//Three composites, each referencing the one to the "left". Directed graph around a group of 3. A node should be its own great grandchild
		hardCompositeNode A = new hardCompositeNode();
		A.myString = "Astring";
		hardCompositeNode B = new hardCompositeNode();
		B.myString = "Bstring";
		hardCompositeNode C = new hardCompositeNode();
		C.myString = "Cstring";
		
		A.right = B;
		B.right = C;
		C.right = A;
		B.left = A;
				
		//Build Interps
		
		
		CompositeInterpretation rootObject = new CompositeInterpretation("hard_composite_node");
		rootObject.addInterpretation(new ScalarInterpretation("myString", "Astring", "StringType"));
		
		CompositeInterpretation bObject = new CompositeInterpretation("hard_composite_node");
		bObject.setFieldName("right");
		bObject.addInterpretation(new ScalarInterpretation("myString", "Bstring", "StringType"));
		
		CompositeInterpretation referenceToAinB = new CompositeInterpretation("hard_composite_node");
		referenceToAinB.setRefString("1");
		referenceToAinB.setFieldName("left");
		bObject.addInterpretation(referenceToAinB);
		
		
		CompositeInterpretation cObject = new CompositeInterpretation("hard_composite_node");
		cObject.setFieldName("right");
		cObject.addInterpretation(new ScalarInterpretation("myString", "Cstring", "StringType"));
		
		CompositeInterpretation referenceToAinC = new CompositeInterpretation("hard_composite_node");
		referenceToAinC.setFieldName("right");
		referenceToAinC.setRefString("1");
		
		cObject.addInterpretation(referenceToAinC);
		bObject.addInterpretation(cObject);
		rootObject.addInterpretation(bObject);
		
		//Setup:
		ISimplTypesScope context = SimplTypesScopeFactory.name("directedLoopReferenceUnderstanding").translations(hardCompositeNode.class).create();
		SimplUnderstander su = new SimplUnderstander(context);
				
		Object result = su.understandInterpretation(rootObject);
		
		hardCompositeNode r = ((hardCompositeNode)result); //r should equal
				
		assertEquals("myString of A does not match that of r", A.myString, r.myString);
		assertEquals("myString of B does not match that of r's child", B.myString, r.right.myString);
		assertEquals("myString of C does not match that of r's grandchild", C.myString, r.right.right.myString);
		assertEquals("myString of A does not match that of r's greatgrandchild", A.myString, r.right.right.right.myString);
		assertNull("Left of A should be Null", A.left);
		assertNull("Left of C should be Null", C.left);
		//Three composites, A refers to b, b refers to c, c refers to b. B is both the child and great grandchild of A. B is its own grandchild
	}
}
