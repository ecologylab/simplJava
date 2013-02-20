package simpl.descriptions.beiber;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import simpl.annotations.dbal.simpl_classes;
import simpl.annotations.dbal.simpl_composite;
import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_scalar;
import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.ClassDescriptors;
import simpl.descriptions.FieldDescriptor;

import static org.junit.Assert.*;

public class classDescriptorConstruction {
	
	
	@Before
	public void clearClassDescriptorCacheFirst()
	{
		ClassDescriptors.__ClearClassDescriptorCache();	
	}
	
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
		
		ClassDescriptor icd = ClassDescriptors.getClassDescriptor(aClass.class);
		assertEquals(icd.getName(), "aClass");
		assertEquals(icd.allFieldDescriptors().size(), 2);
		
		assertTrue("Should have created a class descriptor for aClass", ClassDescriptors.containsCD(aClass.class));
		assertTrue("Should have created a class descriptor for yourClass", ClassDescriptors.containsCD(yourClass.class));		
		assertFalse("Should not have created a class descirptor for bothCycles", ClassDescriptors.containsCD(bothCycles.class));
	}
	
	@Test
	public void testCDConstructionWithTwoLevels()
	{
		ClassDescriptors.__ClearClassDescriptorCache();
		
		ClassDescriptor icd = ClassDescriptors.getClassDescriptor(bothCycles.class);
		assertEquals(icd.getName(), "bothCycles");
		assertEquals(icd.allFieldDescriptors().size(), 4);
		
		assertTrue("Should have created a class descriptor for aClass", ClassDescriptors.containsCD(aClass.class));
		assertTrue("Should have created a class descriptor for yourClass", ClassDescriptors.containsCD(yourClass.class));		
		assertTrue("Should have created a class descirptor for bothCycles", ClassDescriptors.containsCD(bothCycles.class));

	}
	
	class class1{}
	// this class doesn't inherit from class1
	class class2 extends class1{}
	@simpl_inherit
	class class3 extends class2{}
	@simpl_inherit
	class class4 extends class3{}
	@simpl_inherit
	class class5 extends class4{}
	

	@Test
	public void testInheritanceHeirarchiesAreCreatedCorrectly()
	{
		ClassDescriptors.__ClearClassDescriptorCache();
		
		ClassDescriptor class2 = ClassDescriptors.getClassDescriptor(class2.class);
		assertNull("Should have no superclass, no simpl inherit.", class2.getSuperClassDescriptor());
		assertFalse(ClassDescriptors.containsCD(class3.class));
		assertFalse(ClassDescriptors.containsCD(class1.class));
		
		ClassDescriptor class3 = ClassDescriptors.getClassDescriptor(class3.class);
		assertNotNull("Should have superclass!", class3.getSuperClassDescriptor());
		assertEquals("Should be superclassed from class2", class2.getName(), class3.getSuperClassDescriptor().getName());
		assertFalse(ClassDescriptors.containsCD(class4.class));
		
		// Now create the chain to be sure that the middle links will get filled in.
		
		ClassDescriptor class5 = ClassDescriptors.getClassDescriptor(class5.class);
		assertTrue("Class 4 should have been constructed to construct CD for class5!", ClassDescriptors.containsCD(class4.class));
	}

	class classes{}
	class polymorphA extends classes{}
	class polymorphB extends classes{}
	class polymorphC extends classes{}
	
	class polymorph{
		@simpl_composite
		@simpl_classes({polymorphA.class, polymorphB.class})
		public classes myPolymorphicField;
	}
	
	class cyclePolymorph extends classes
	{
		@simpl_classes({polymorphA.class, cyclePolymorph.class })
		@simpl_composite
		public classes myPolymorphicField;
	}
	
	@Test
	public void TestPolymorphicFieldsTreatedCorrectly() throws Exception 
	{
		ClassDescriptors.__ClearClassDescriptorCache();
				
		ClassDescriptor icd = ClassDescriptors.getClassDescriptor(polymorph.class);
		
		FieldDescriptor polyField = icd.allFieldDescriptors().get(0);
		assertNotNull("Should have a field!", polyField);
		
		assertTrue(polyField.getPolymoprhicFieldDescriptors().contains(ClassDescriptors.getClassDescriptor(polymorphA.class)));
		assertTrue(polyField.getPolymoprhicFieldDescriptors().contains(ClassDescriptors.getClassDescriptor(polymorphB.class)));
		assertFalse("Base field should not be included.", polyField.getPolymoprhicFieldDescriptors().contains(ClassDescriptors.getClassDescriptor(classes.class)));
		
		assertFalse("Shouldn't have touched polymorph C at all", ClassDescriptors.containsCD(polymorphC.class));
		
	}
	
	@Test
	public void TestPolymorphicFieldsHandlesCycles() throws Exception
	{
		ClassDescriptors.__ClearClassDescriptorCache();
		
		ClassDescriptor icd = ClassDescriptors.getClassDescriptor(cyclePolymorph.class);
		
		FieldDescriptor polyField = icd.allFieldDescriptors().get(0);
		assertNotNull("Should have a field!", polyField);
		
		assertTrue(polyField.getPolymoprhicFieldDescriptors().contains(ClassDescriptors.getClassDescriptor(polymorphA.class)));
		assertTrue(polyField.getPolymoprhicFieldDescriptors().contains(ClassDescriptors.getClassDescriptor(cyclePolymorph.class)));
		assertFalse("Base field should not be included.", polyField.getPolymoprhicFieldDescriptors().contains(ClassDescriptors.getClassDescriptor(classes.class)));
		
		assertFalse("Shouldn't have touched polymorph C at all", ClassDescriptors.containsCD(polymorphC.class));
	}
	
	
	
	
	
}
