package simpl.equals;

import static org.junit.Assert.*;

import org.junit.Test;

import simpl.Simpl;

/**
 * A test class to cover the general invariants for simpl.equals, mostly 
 * nullity, class, type restrictions. 
 * Specific feature-level tests should go in their own test class. :) 
 */
public class SimplEqualsGeneralInvariants {

	@Test
	public void testNullityWorksCorrectly() {
		Object o = new Object(); 
		assertTrue(Simpl.equals(null, null));
		assertFalse(Simpl.equals(null, o));
		assertFalse(Simpl.equals(o,null));
	}
	
	final class myClassWithoutSimplAnything
	{
		public String myString;
		public myClassWithoutSimplAnything()
		{
		}
		
		@Override
		public boolean equals(Object other)
		{
			return ((myClassWithoutSimplAnything)other).myString.equals(this.myString);
		}
	}
	
	@Test
	public void testSimplEqualsFallsThroughToUnderlyingEquals()
	{
		// Sometimes we get things without a class descriptor. 
		myClassWithoutSimplAnything lhs = new myClassWithoutSimplAnything();
		lhs.myString = "test";
		myClassWithoutSimplAnything rhs = new myClassWithoutSimplAnything();
		rhs.myString = "test";
		
		assertTrue("Standard equals should be true!", lhs.equals(rhs));
		assertTrue("Simpl equals should be true!", Simpl.equals(lhs,rhs));
	}
	
	@Test
	public void testSimplEqualsReturnsFalseForObjectsOfDifferentClasses()
	{
		String s = "differentFrom";
		Integer i = 13;
		
		assertFalse(Simpl.equals(s,i));
		assertFalse(Simpl.equals(i,s));
	}

}
