package simpl.descriptions;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.Test;

import simpl.descriptions.testclasses.basicSuperClass;
import simpl.descriptions.testclasses.inheritSuperClass;

public class testSuperclassFieldAssumptions {

	@Test
	/** 
	 * This validates that base class Field objects can be used to set child class fields. :) 
	 * The mechanism by which superclass fields get added to an object tangentially relies on this
	 * assumption 
	 * @throws Exception
	 */
	public void testReflectionAssumption () throws Exception  {
		Class<?> base = basicSuperClass.class;
		
		Class<?> inherit = inheritSuperClass.class;

		basicSuperClass bsc = new basicSuperClass();
		bsc.superField = 1;

		
		inheritSuperClass isc = new inheritSuperClass();
		isc.superField = 2;
		isc.baseField = 3;
		
		Field fBase = base.getField("superField");
		
		Field iBase = inherit.getField("superField");
		
		
		assertTrue(isc.superField == 2);
		
		iBase.set(isc, new Integer(4));
		
		assertTrue(isc.superField == 4);
		
		fBase.set(isc, 5);
		
		assertTrue(isc.superField == 5);
	}
	
	@Test
	public void testSuperClassFieldDescriptorsSetFieldsCorrectly()
	{
		ClassDescriptor base = ClassDescriptors.getClassDescriptor(basicSuperClass.class);
		
		ClassDescriptor inherit = ClassDescriptors.getClassDescriptor(inheritSuperClass.class);

		basicSuperClass bsc = new basicSuperClass();
		bsc.superField = 1;

		
		inheritSuperClass isc = new inheritSuperClass();
		isc.superField = 2;
		isc.baseField = 3;
		
		FieldDescriptor fBase = base.fields().by("name").get("superField");
		
		FieldDescriptor iBase = inherit.fields().by("name").get("superField");
		
		assertTrue(isc.superField == 2);
		
		iBase.setValue(isc, new Integer(4));
		
		assertTrue(isc.superField == 4);
		
		fBase.setValue(isc, 5);
		
		assertTrue(isc.superField == 5);
	}

}
