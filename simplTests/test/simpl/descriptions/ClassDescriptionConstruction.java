package simpl.descriptions;

import org.junit.Test;

import simpl.annotations.dbal.simpl_composite;
import simpl.annotations.dbal.simpl_composite_as_scalar;
import simpl.annotations.dbal.simpl_scalar;
import simpl.types.scalar.IntegerType;
import simpl.types.scalar.StringType;

import static org.junit.Assert.*;


public class ClassDescriptionConstruction {

	class myClass
	{
		@simpl_scalar
		public int myInt; 
		
		@simpl_composite
		public innerClass myComposite;
	}
	
	class innerClass
	{
		@simpl_scalar
		public String innerScalar;
	}
	
	class myCompositeStringClass
	{
		@simpl_scalar
		public String myString;
		
		@simpl_composite
		@simpl_composite_as_scalar
		public innerClass myComposite;
	}
	
	@Test
	public void testCompositesGetDescribed()
	{
		ClassDescriptor<?> myClass = ClassDescriptors.getClassDescriptor(myClass.class);
		
		assertEquals(2, myClass.allFieldDescriptors().size());
		
		FieldDescriptor myInt = myClass.allFieldDescriptors().get(0);
		assertEquals("myInt is a scalar!", FieldType.SCALAR, myInt.getType());
		assertEquals("myInt", myInt.getName());
		assertEquals(int.class, myInt.getFieldType());
		assertEquals(IntegerType.class, myInt.getScalarType().getClass());
		
		
		FieldDescriptor myInnerComposite = myClass.allFieldDescriptors().get(1);
		
		assertEquals("myComposite is a composite!", FieldType.COMPOSITE_ELEMENT,myInnerComposite.getType());
		assertEquals("myComposite", myInnerComposite.getName());
		assertEquals(innerClass.class, myInnerComposite.getFieldType());
	}
	
	@Test
	public void testCompositeAsScalarGetsDescribed()
	{
		ClassDescriptor<?> myClass = ClassDescriptors.getClassDescriptor(myCompositeStringClass.class);
		
		assertEquals("Not all fields were described!", 2, myClass.allFieldDescriptors().size());
		
		FieldDescriptor myString = myClass.allFieldDescriptors().get(0);
		assertEquals("myString is a scalar!", FieldType.SCALAR, myString.getType());
		assertEquals("myString", myString.getName());
		assertEquals(String.class, myString.getFieldType());
		assertEquals(StringType.class, myString.getScalarType().getClass());
		
		FieldDescriptor myInnerComposite = myClass.allFieldDescriptors().get(1);
		
		assertEquals("myComposite is a composite!", FieldType.COMPOSITE_ELEMENT,myInnerComposite.getType());
		assertEquals("myComposite", myInnerComposite.getName());
		assertEquals(innerClass.class, myInnerComposite.getFieldType());
	}
}
