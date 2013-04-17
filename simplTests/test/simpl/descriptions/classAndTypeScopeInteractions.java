package simpl.descriptions;

import static org.junit.Assert.*;

import org.junit.Test;

import simpl.annotations.dbal.simpl_composite;
import simpl.annotations.dbal.simpl_scope;
import simpl.core.ISimplTypesScope;
import simpl.core.SimplTypesScopeFactory;
import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.ClassDescriptors;
import simpl.exceptions.SIMPLDescriptionException;

/**
 * There are some possibly gnarly interactions between making a Type Scope
 * and making a class descriptor. This class should try to test those.
 * @author tom
 *
 */
public class classAndTypeScopeInteractions {

	class A{}
	class AChild extends A{}
	class AnotherChild extends A{}
	class B
	{
		@simpl_scope("A_Test")
		@simpl_composite
		public A myPolymorph;
	}
	
	// A test class with a non-existent type scope
	// Should provoke exceptions. 
	class B_360NoScopeMaster
	{
		@simpl_scope("DOES NOT EXIST AT ALL")
		@simpl_composite
		public A myPolymorph;
	}
	
	@Test
	public void testCreatePolymorphicDescriptorAsPartOfSTSConstruction()
	{
		ISimplTypesScope sts = SimplTypesScopeFactory.name("A_Test").translations(A.class, AChild.class, AnotherChild.class, B.class).create();

		ClassDescriptor cd = sts.getClassDescriptorByTag("b");
		
	
	}
	
	@Test(expected=RuntimeException.class)
	public void testNonExistentScopesShouldThrowAnException()
	{
		ClassDescriptor CD = ClassDescriptors.getClassDescriptor(B_360NoScopeMaster.class);
	}

}
