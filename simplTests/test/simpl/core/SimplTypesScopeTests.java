package simpl.core;

import static org.junit.Assert.*;

import org.junit.Test;

import simpl.annotations.dbal.simpl_scalar;
import simpl.descriptions.ClassDescriptor;

public class SimplTypesScopeTests {

	
	class myClass
	{
		@simpl_scalar
		public int aScalar;
	}
	
	@Test
	public void testBasicInvariants() {
		ISimplTypesScope ists = SimplTypesScopeFactory.name("test").translations(null).create();
		
		assertEquals("test", ists.getName());		
		// Ths is really fluff, but it makes sure that we have the most basic invariant possible. ;)
		ists.setName("New name");
		assertEquals("New name", ists.getName());
		
		ClassDescriptor<?> myDescriptor = ClassDescriptor.getClassDescriptor(myClass.class);
		
		assertEquals("my_class", myDescriptor.getTagName());
		assertEquals(1,myDescriptor.allFieldDescriptors().size()); 
		ists.addTranslation(myClass.class);
		
		assertTrue("Should contain the tag for the newly added descriptor.", ists.containsDescriptorForTag(myDescriptor.getTagName()));
		
	}

}
