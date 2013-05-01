package simpl.interpretation;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import simpl.core.ISimplTypesScope;
import simpl.core.SimplTypesScopeFactory;
import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.ClassDescriptors;
import simpl.descriptions.FieldDescriptor;
import simpl.descriptions.testclasses.basicSuperClass;
import simpl.exceptions.SIMPLTranslationException;
import simpl.interpretation.testclasses.basicCompositeAsScalar;
import simpl.interpretation.testclasses.classWithCompositeAsScalarAsScalar;
import simpl.types.ScalarType;
import simpl.types.TypeRegistry;

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
	
	@Test
	public void testCompositeAsScalarDescribed() throws SIMPLTranslationException
	{
		ClassDescriptor cd = ClassDescriptors.getClassDescriptor(basicCompositeAsScalar.class);
		assertEquals((Integer)2, (Integer)cd.fields().size()); 
		assertEquals((Integer)1, (Integer)cd.fields().CompositesAsScalars.size());
		
		assertTrue(TypeRegistry.containsScalarTypeFor(basicCompositeAsScalar.class));
		
		ScalarType st = TypeRegistry.getScalarType(basicCompositeAsScalar.class);
		basicCompositeAsScalar bsc = new basicCompositeAsScalar();
		
		bsc.x = 9;
		bsc.y = 100;
		
		assertEquals("9", st.marshal(bsc));
		
		basicCompositeAsScalar newbsc = (basicCompositeAsScalar) st.unmarshal("9");
		
		assertEquals((Integer)9, newbsc.x);
		
	}
	
	@Test
	public void interpretCompositeAsScalarAsScalar() throws Exception
	{
		classWithCompositeAsScalarAsScalar asScalar = new classWithCompositeAsScalarAsScalar();
		asScalar.ourScalar.x = 13;
		
		
		ClassDescriptor cd = ClassDescriptors.getClassDescriptor(asScalar);
		
		assertEquals((Integer)1, cd.fields().size());
		
		FieldDescriptor fd = cd.fields().getAllItems().get(0);
		ScalarType st = fd.getScalarType();
		assertNotNull(st);
		
		
		
		ISimplTypesScope context = SimplTypesScopeFactory.name("testUnderstandingOfScalarsAsComposites").translations(classWithCompositeAsScalarAsScalar.class, basicCompositeAsScalar.class).create();
		
		SimplInterpreter si = new SimplInterpreter();
		
		SimplInterpretation interpretation = si.interpretInstance(asScalar);
		
		assertNotNull("Interpretation should not be null!", interpretation);
		assertTrue("Expect interpretation of the root object to be a composite", interpretation instanceof CompositeInterpretation);
		
		CompositeInterpretation ci = (CompositeInterpretation)interpretation;
		assertEquals("class_with_composite_as_scalar_as_scalar", ci.getTagName());
		assertEquals(1, ci.interpretations.size());

		ScalarInterpretation ssi = (ScalarInterpretation) ci.interpretations.get(0);
		assertEquals("ourScalar", ssi.fieldName);
		assertEquals("13", ssi.fieldValue);
		assertEquals("basicCompositeAsScalar", ssi.scalarTypeName);
		
		SimplUnderstander su = new SimplUnderstander(context);
		
		Object result = su.understandInterpretation((CompositeInterpretation)interpretation);
		
		classWithCompositeAsScalarAsScalar res = (classWithCompositeAsScalarAsScalar)result;
		assertNotNull("OurComposite As scalar should not be null!", res.ourScalar);
		assertEquals(res.ourScalar.x, new Integer(13));
		
		
	}

}
