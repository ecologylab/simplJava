package simpl.descriptions.beiber;

import static org.junit.Assert.*;

import org.junit.Test;

import ecologylab.serialization.primaryScenarioEnum;

import simpl.annotations.dbal.simpl_filter;
import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_scalar;
import simpl.annotations.dbal.simpl_tag;
import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.ClassDescriptors;
import simpl.descriptions.EnumerationDescriptor;
import simpl.descriptions.FieldDescriptor;
import simpl.descriptions.IMetaInformationProvider;
import simpl.descriptions.MetaInformation;
import simpl.exceptions.SIMPLDescriptionException;

/**
 * The annotation parser is tested in the annotationParserTests...
 * this makes sure that the field, class, and enum descriptions all correctly use the annotaiton parser
 * and correclty capture relevant annotation information. :D 
 * @author tom
 *
 */
public class FieldsAndClassesObtainAllMetaInformation {


	@simpl_inherit
	@myRandomAnnotation
	class myAnnotatedClass
	{
		@simpl_scalar
		@simpl_tag("another_tag_name")
		@myRandomAnnotation
		public Integer someField;
	}
	
	
	@Test
	public void testClassObtainsAllMetaInformation() {
		ClassDescriptor cd = ClassDescriptors.getClassDescriptor(myAnnotatedClass.class);
		
		assertFalse("Should not contain this metadata~", cd.containsMetaInformation("something_that_does_not_exist"));
		
		assertTrue("Should simpl_inherit!", cd.containsMetaInformation("simpl_inherit"));
		assertTrue("Should have myRandomAnnotation~",cd.containsMetaInformation("myRandomAnnotation"));
	
		MetaInformation simplInherit = cd.getMetaInformation("simpl_inherit");
		assertEquals("Recieved the incorrect Annotation from the given name~", "simpl_inherit", simplInherit.getAnnotationName());
		assertTrue("Should have no parameters!", simplInherit.getParameters().isEmpty());									
		
		MetaInformation myRandomAnnotation = cd.getMetaInformation("myRandomAnnotation");
		assertEquals("Recieved the incorrect Annotation from the given name~", "myRandomAnnotation", myRandomAnnotation.getAnnotationName());	
		assertTrue("Should have no parameters!", myRandomAnnotation.getParameters().isEmpty());
		
		
		FieldDescriptor fd = cd.allFieldDescriptors().get(0);
		assertTrue("Should have simpl_scalar~",fd.containsMetaInformation("simpl_scalar"));						//
		assertTrue("Should have myRandomAnnotation~",fd.containsMetaInformation("myRandomAnnotation"));			//

		assertTrue("Annotation for simpl_tag was not found~", fd.containsMetaInformation("simpl_tag"));								//
			
		MetaInformation simpl_tag = fd.getMetaInformation("simpl_tag");
		assertTrue("simpl_tag does not contain the default parameter~", simpl_tag.hasParameter("value"));																		//simpl_tag does not contain the default parameter
		assertEquals("getValueFor does not return the correct parameter value~","another_tag_name", simpl_tag.getValueFor("value"));													//
		assertEquals("getValue does not return the correct parameter value~", "another_tag_name", simpl_tag.getValue());														//
	}
	

	@Test
	public void testEnumerationDescriptionObtainsAllMetaInformation() throws SIMPLDescriptionException
	{
		EnumerationDescriptor ed = EnumerationDescriptor.get(annotatedEnumeration.class);
		assertTrue("Should have myRandomAnnotation~", ed.containsMetaInformation("myRandomAnnotation"));														//
		assertTrue("Should have simpl_tag~", ed.containsMetaInformation("simpl_tag"));																//
		assertEquals("getValue does not return the correct parameter value~","my_spiffy_tag",ed.getMetaInformation("simpl_tag").getValue());										//
		
		assertTrue("First Enumeration Entry should have myRandomAnnotation~", ed.getEnumerationEntries().get(0).containsMetaInformation("myRandomAnnotation"));						//
	}

}
