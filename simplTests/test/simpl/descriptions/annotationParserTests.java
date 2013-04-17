package simpl.descriptions;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Collection;

import org.junit.Test;

import simpl.annotations.dbal.Hint;
import simpl.annotations.dbal.bibtex_tag;
import simpl.annotations.dbal.simpl_hints;
import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_scalar;
import simpl.descriptions.AnnotationParser;
import simpl.descriptions.MetaInformation;
import simpl.descriptions.ParameterDescriptor;

public class annotationParserTests {

	class noAnnotations{
		public Integer myScalar;
		public void myMethod(){}
	}
	
	class myClass{
		@simpl_scalar
		public Integer myScalar;
	}
	
	class myValueAnnotationClass{
		@bibtex_tag("my value is here")
		public Integer myValueAnnotation;
	}
	
	class myDefaultValueAnnotationClass{
		@simpl_hints
		public Integer myScalar;
	}
	
	@Test
	public void testThatAnnotationParserCorrectlyIdentifiesNoAnnotations() throws NoSuchFieldException, SecurityException, NoSuchMethodException {
		AnnotationParser ap = new AnnotationParser();
		assertTrue(ap.getAllMetaInformation(noAnnotations.class).isEmpty());
		assertTrue(ap.getAllMetaInformation(noAnnotations.class.getField("myScalar")).isEmpty());
		assertTrue(ap.getAllMetaInformation(noAnnotations.class.getMethod("myMethod", null)).isEmpty());	
	}
	
	@Test
	public void testThatAnnotationParserIdentifiesNoParameterAnnotations() throws Exception
	{
		AnnotationParser ap = new AnnotationParser();
		
		Collection<MetaInformation> metaInfos = ap.getAllMetaInformation(myClass.class.getField("myScalar"));
		assertEquals("Should have only one annotation: simpl_scalar", 1, metaInfos.size());
		MetaInformation first = metaInfos.iterator().next();
		
		assertEquals("Should be simpl_scalar", "simpl_scalar", first.getAnnotationName());
		assertEquals("Should have 0 parameters",0, first.getParameters().size());
	}
	
	@Test
	public void testThatAnnotationParserCorrectlyIdentifiesValueParameter() throws Exception
	{
		AnnotationParser ap = new AnnotationParser();
		
		Field f = myValueAnnotationClass.class.getFields()[0];
		
		Collection<MetaInformation> metaInfos = ap.getAllMetaInformation(f);
		assertEquals("Should have only one annotation: bibtex_tag", 1, metaInfos.size());
		MetaInformation first = metaInfos.iterator().next();
		
		assertEquals("Should be bibtex_tag", "bibtex_tag", first.getAnnotationName());
		assertEquals("Should have 1 parameter",1, first.getParameters().size());
		
		ParameterDescriptor param = first.getParameters().iterator().next();
		assertEquals("Should be named \"value\"", "value", param.getName());
		assertEquals("Should have given value...", "my value is here", param.getValue());
		
		
		assertEquals("Value should be correct when getting value via IMetaInformation", "my value is here", first.getValueFor("value"));
	}

	@Test
	public void testAnnotationParserGracefullyHandlesDefaultValues() throws Exception
	{
		Field defaultValueAnnotated = myDefaultValueAnnotationClass.class.getFields()[0];
	
		AnnotationParser ap = new AnnotationParser();
		
		Collection<MetaInformation> metaInfos = ap.getAllMetaInformation(defaultValueAnnotated);
		assertEquals("Should have only one annotation: bibtex_tag", 1, metaInfos.size());
		MetaInformation first = metaInfos.iterator().next();
		
		assertEquals("Should be simpl_hints", "simpl_hints", first.getAnnotationName());
		assertEquals("Should have 1 parameter",1, first.getParameters().size());
		
		ParameterDescriptor param = first.getParameters().iterator().next();
		assertEquals("Should be named \"value\"", "value", param.getName());
		
		Object value =  first.getValueFor("value");
		Hint[] myHints = (Hint[])value;
		assertEquals("Shoul dhave a single hint", 1, myHints.length);
		
		Hint firstHint = myHints[0];
		assertEquals("Value should be correct when getting value via IMetaInformation", Hint.XML_ATTRIBUTE, firstHint);
		// this is probably a bad example b/c it's an array, ho hum. 
	}
}
