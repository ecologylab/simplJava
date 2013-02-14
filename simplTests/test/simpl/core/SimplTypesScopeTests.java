package simpl.core;

import static org.junit.Assert.*;

import org.junit.Test;

import simpl.annotations.dbal.simpl_scalar;
import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.ClassDescriptors;

public class SimplTypesScopeTests {

	
	class myClass
	{
		@simpl_scalar
		public int aScalar;
	}
	
	@Test
	/**
	 * When I make a simpl type scope, it should:
	 * Have the name that I give it
	 * Be able to be assigned a new name, because that's how the factory can play nicely w/ STS
	 * Insert a class
	 * Get the classdescriptor by tag
	 * Remove a class
	 * Make sure it's not there. :) 
	 */
	public void testBasicInvariants() {
		ISimplTypesScope ists = SimplTypesScopeFactory.name("test").translations(null).create();
		
		assertEquals("test", ists.getName());		
		// Ths is really fluff, but it makes sure that we have the most basic invariant possible. ;)
		ists.setName("New name");
		assertEquals("New name", ists.getName());
		
		ClassDescriptor<?> myDescriptor = ClassDescriptors.getClassDescriptor(myClass.class);
		
		assertEquals("my_class", myDescriptor.getTagName());
		assertEquals(1,myDescriptor.allFieldDescriptors().size()); 
		ists.addTranslation(myClass.class);
		
		assertTrue("Should contain the tag for the newly added descriptor.", ists.containsDescriptorForTag(myDescriptor.getTagName()));
		
		ists.removeTranslation(myClass.class);
		
		assertEquals("All items should be gone!" ,0, ists.getAllClassDescriptors().size());
		
		assertFalse("Should NOT contain the tag for the newly added descriptor.", ists.containsDescriptorForTag(myDescriptor.getTagName()));
	}
	
	class aClass{}
	
	class bClass{}
	
	class cClass{}
	
	class dClass{}
	
	/**
	 * If I have a simple type scope, A, containing some classes...
	 * And I have a STS, B, which inherits FROM A...
	 * B should contain: All of B's classes, and all of A's. :D 
	 */
	@Test
	public void testInheritSTS()
	{
		ISimplTypesScope ists = SimplTypesScopeFactory.name("A").translations(aClass.class, cClass.class).create();
		assertTrue(ists.containsDescriptorForTag("a_class"));
		assertTrue(ists.containsDescriptorForTag("c_class"));
		
		assertEquals("Should have two items in the STS:", 2, ists.getAllClassDescriptors().size());
		
		ISimplTypesScope b = SimplTypesScopeFactory.name("B").inherits(ists).translations(bClass.class).create();
		assertTrue("We expect B to at least contain bClass...", b.containsDescriptorForTag("b_class"));
		
		assertTrue("Should contain all classes in A, missing a_class", b.containsDescriptorForTag("a_class"));
		assertTrue("Should contain all classes in A, missing c_class", b.containsDescriptorForTag("c_class"));

		assertFalse("A should not contain b_class", ists.containsDescriptorForTag("b_class"));
	}

}
