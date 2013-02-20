package simpl.equals;

import static org.junit.Assert.*;

import org.junit.Test;

import ecologylab.fundamental.simplescalar.SimpleBoolean;

import simpl.Simpl;
import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.ClassDescriptors;
import simpl.descriptions.FieldDescriptor;
import simpl.exceptions.SIMPLTranslationException;


public class SimplEqualsScalarTests {
	
	// make a bunch of methods to call each of the saclar types. with the requisite correct or incorrect value;
	@Test
	public void testBooleanEquality() throws SIMPLTranslationException
	{
		testBasicScalarEquality(SimpleBoolean.class, new Boolean(true), new Boolean(false));
	}
	
	
	
	/**
	 * A test class that handles scalar equality for classes with a single scalar field.
	 * This is more intended to capture any issues that may exist with a 
	 * Also makes sure to catch the core contracts for equality outlined in java: 
	 * Reflexivity: x==x
	 * Symmetric: x, y, x==y, y==x
	 * Transitive: x,y,z: x==y -> true, y==z -> true; x==z
	 * Consistent, x, y -> some value;;; repeated many times, stays the same
	 * (We validate consistency by running each of the other checks multiple times in a row. 
	 * This should suffice.) 
	 * x.equals null => false, null = x => false 
	 * 
	 * @param simplClass The class to test things with. 
	 * @param correctValue a correct value for this scalar
	 * @param incorrectValue an incorrect value for this scalar
	 * @throws SIMPLTranslationException 
	 */
	private void testBasicScalarEquality(Class<?> simplClass, Object correctValue, Object incorrectValue) throws SIMPLTranslationException
	{	
		ClassDescriptor cd = ClassDescriptors.getClassDescriptor(simplClass);
		
		assertTrue("Expecting this class to have only one field; this test case is targeted for specific coverage," +
				" use a different test class / method please.",
				cd.allFieldDescriptors().size() == 1);
		
		assertEquals("Expecting one scalar field! Issue w/ indexer", cd.fields().Scalars.size(), 1);
		
		FieldDescriptor fd = (FieldDescriptor) cd.allFieldDescriptors().get(0);
		
		Object x = cd.getInstance();
		Object y = cd.getInstance();
		Object z = cd.getInstance();
		
		// test valid values. 
		setValue(x,fd, correctValue);
		setValue(y,fd, correctValue);
		setValue(z,fd, correctValue);

		for(int i = 0 ; i < 5; i ++)
		{
			validateReflexive(x);
		}
		
		
		for(int i = 0 ; i < 5; i ++)
		{
			validateSymmetric(x,y,true);
		}
		
		validateTransistivity(x,y,z);
		
	//	boolean fieldIsPrim = fd.getType().isPrimitive();
		
		//if(!fieldIsPrim)
		//{
		//	setValue(z,fd, null);
		//	assertEquals(null, fd.getValue(z));
		//}
		
		for(int i = 0 ; i < 5; i ++)
		{
			//validateNullity(x ,z, fieldIsPrim);
		}
		
		setValue(y,fd, incorrectValue);
		
		for(int i = 0 ; i < 5; i ++)
		{
			validateSymmetric(x,y, false);
		}
	}
	
	// seperating this out b/c this API may change. 
	private void setValue(Object context, FieldDescriptor fd, Object value)
	{
		try{
		fd.getScalarType().setFieldValue(fd.getScalarType().marshal(value), fd.getField(), context);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	private void validateReflexive(Object x)
	{
		assertTrue("Reflexivity implies x==x!", Simpl.equals(x, x));		
	}
	
	private void validateSymmetric(Object x, Object y, boolean expected)
	{
		boolean xEqy = Simpl.equals(x, y);
		assertEquals("Failed equality check at Symmetric Validation!",expected, xEqy);
		boolean yEqx = Simpl.equals(y, x);
		assertEquals("Failed Symmetry test!", xEqy, yEqx);
	}

	private void validateTransistivity(Object x, Object y, Object z)
	{
		assertTrue(Simpl.equals(x,y));
		assertTrue(Simpl.equals(y,z));
		assertTrue(Simpl.equals(x,z));
	}
	
	private void validateNullity(Object nonNull, Object hasNull, boolean isPrimitive)
	{	
		assertFalse("nonNull == null should be false!", Simpl.equals(nonNull, null));
		assertFalse("null == nonNull should be false!", Simpl.equals(null, nonNull));
		
		if(!isPrimitive)
		{
			assertFalse("null value == nonNull should be false!", Simpl.equals(hasNull, nonNull));
			assertFalse("nonNull == null value should be false!", Simpl.equals(nonNull, hasNull));
		}
	}
}
