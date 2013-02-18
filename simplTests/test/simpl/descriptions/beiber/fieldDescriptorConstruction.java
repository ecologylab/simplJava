package simpl.descriptions.beiber;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import simpl.annotations.dbal.simpl_classes;
import simpl.annotations.dbal.simpl_composite;
import simpl.annotations.dbal.simpl_scalar;
import simpl.descriptions.FieldType;

public class fieldDescriptorConstruction {

	
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
	
	@Test
	public void testSetUpdatesAndScalarFieldDescribes()  throws Exception{
		ClassDescriptors.__ClearClassDescriptorCache();
		
		
		// Get our field to test (myScalarField from aClass)
		
		Field field = aClass.class.getField("myScalarField");
		
		Set<UpdateClassDescriptorCallback> dependentClasses = new HashSet<UpdateClassDescriptorCallback>();
		// We're just going to add something to be sure that the set changes. :) 
		UpdateClassDescriptorCallback myUCD = null;
		dependentClasses.add(myUCD);
		assertFalse(dependentClasses.isEmpty());
	
		IFieldDescriptor myDescriptor = FieldDescriptors.getFieldDescriptor(field, dependentClasses);
		
		assertTrue(dependentClasses.isEmpty());
		assertEquals("Should be scalar", FieldType.SCALAR, myDescriptor.getFieldType());
		assertEquals("Should be myScalarField", "myScalarField", myDescriptor.getName());
	}
	
	@Test
	public void testDependentFDReturnsDependency() throws Exception
	{
		ClassDescriptors.__ClearClassDescriptorCache();
		
		Field field = aClass.class.getField("myComposite");
		
		Set<UpdateClassDescriptorCallback> dependentClasses = new HashSet<UpdateClassDescriptorCallback>();
		// We're just going to add something to be sure that the set changes. :) 
		UpdateClassDescriptorCallback myUCD = null;
		dependentClasses.add(myUCD);
		assertFalse(dependentClasses.isEmpty());
		
		
		IFieldDescriptor myDescriptor = FieldDescriptors.getFieldDescriptor(field, dependentClasses);
						
		assertEquals("Should be composite", FieldType.COMPOSITE_ELEMENT, myDescriptor.getFieldType());
		assertEquals("Should be myComposite", "myComposite", myDescriptor.getName());
	}
}
