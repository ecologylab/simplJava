package simpl.descriptions.beiber;

import org.junit.Test;

import simpl.annotations.dbal.simpl_composite;
import simpl.annotations.dbal.simpl_scalar;
import simpl.descriptions.beiber.fieldDescriptorConstruction.aClass;
import simpl.descriptions.beiber.fieldDescriptorConstruction.yourClass;

import static org.junit.Assert.*;

public class classDescriptorConstruction {
	
	public class aClass{
		@simpl_scalar
		public Integer myScalarField;
		
		@simpl_composite
		public yourClass myComposite;
	}
	
	public class yourClass{
		@simpl_scalar
		public String myStringScalar;
		
		@simpl_composite
		public aClass myCycle;
	}

	public class bothCycles
	{
		@simpl_scalar 
		public String someString;

		@simpl_scalar
		public Integer someInt;
		
		@simpl_composite
		public yourClass b;
		
		@simpl_composite
		public aClass a;
	}
	
	// so this is copy pasted. I should refactor this.
	// Some other time. :) 
	
	@Test
	public void testCDConstructionBaseCaseWithCyclesInFieldTypes()
	{
		ClassDescriptors.__ClearClassDescriptorCache();
		
		IClassDescriptor icd = ClassDescriptors.get(aClass.class);
		assertEquals(icd.getName(), "aClass");
		assertEquals(icd.getFields().size(), 2);
		
		assertTrue("Should have created a class descriptor for aClass", ClassDescriptors.containsCD(aClass.class));
		assertTrue("Should have created a class descriptor for yourClass", ClassDescriptors.containsCD(yourClass.class));		
		assertFalse("Should not have created a class descirptor for bothCycles", ClassDescriptors.containsCD(bothCycles.class));

		
	
	
	
	}
	
	@Test
	public void testCDConstructionWithTwoLevels()
	{
		ClassDescriptors.__ClearClassDescriptorCache();
		
		IClassDescriptor icd = ClassDescriptors.get(bothCycles.class);
		assertEquals(icd.getName(), "bothCycles");
		assertEquals(icd.getFields().size(), 4);
		
		assertTrue("Should have created a class descriptor for aClass", ClassDescriptors.containsCD(aClass.class));
		assertTrue("Should have created a class descriptor for yourClass", ClassDescriptors.containsCD(yourClass.class));		
		assertTrue("Should have created a class descirptor for bothCycles", ClassDescriptors.containsCD(bothCycles.class));

	}
}
