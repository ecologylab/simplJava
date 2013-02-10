package simpl.types.scalar;

import java.lang.reflect.Field;
import java.util.Collection;

import org.junit.Test;

import simpl.exceptions.SIMPLTranslationException;
import static org.junit.Assert.*;

import ecologylab.fundamental.simplescalar.SimpleBoolean;
import ecologylab.fundamental.simplescalar.Simpleprimboolean;

public class JavaScalarTypeTests {

	@Test
	/**
	 * We want to validate that the reflection mechanism to get supported value types for a scalar type is correct. 
	 */
	public void TestBooleanScalarTypeEnumeratesSupportedTypes()
	{
		BooleanType bt = new BooleanType();
		Collection<Class<?>> supportedTypes = bt.getSupportedTypes();
		assertEquals(2, supportedTypes.size());
		
		assertTrue(supportedTypes.contains(Boolean.class));
		assertTrue(supportedTypes.contains(boolean.class));
	}
	
	final class trickyTypeClass
	{
		public String notABoolean;
	}

	@Test
	public void TestScalarTypeRejectsUnsupportedTypes() throws SIMPLTranslationException, NoSuchFieldException, SecurityException
	{	
		 try
		 {
			 BooleanType bt = new BooleanType();
			 bt.getFieldString(trickyTypeClass.class.getField("notABoolean"), new trickyTypeClass());
		 }
		 catch(SIMPLTranslationException e)
		 {
			 if(!e.getMessage().contains("Type not supported"))
			 {
				 fail("Exception type was invalid! Message was: " + e.getMessage());
			 }
		 }
		 catch(Exception e)
		 {
			 fail("Invalid exception type! Was: "+  e.getMessage());
		 }
		 
		 
		 try
		 {
			 BooleanType bt = new BooleanType();
			 bt.setFieldValue("gonna fail" ,trickyTypeClass.class.getField("notABoolean"), new trickyTypeClass());
		 }
		 catch(SIMPLTranslationException e)
		 {
			 if(!e.getMessage().contains("Type not supported"))
			 {
				 fail("Exception type was invalid! Message was: " + e.getMessage());
			 }
		 }
		 catch(Exception e)
		 {
			 fail("Invalid exception type! Was: "+  e.getMessage());
		 }
		 
	}
	
	@Test
	public void TestBooleanScalarTypeGetsCorrectValue() throws NoSuchFieldException, SecurityException, SIMPLTranslationException
	{
		SimpleBoolean s = new SimpleBoolean();
		s.setSimpleBoolean(new Boolean(true));

		Field f = s.getClass().getDeclaredField("simpleboolean");
		
		BooleanType bt = new BooleanType();
		assertEquals("true", bt.getFieldString(f, s));
	}
	
	@Test
	public void TestBooleanScalarTypeSetsCorrectValue() throws Exception 
	{
		SimpleBoolean s = new SimpleBoolean();
		s.setSimpleBoolean(new Boolean(false));
		
		Field f = s.getClass().getDeclaredField("simpleboolean");
		
		BooleanType bt = new BooleanType();
		
		bt.setFieldValue("true", f, s);
		assertEquals(new Boolean(false), bt.unmarshal("false"));
		assertEquals(new Boolean(true), bt.unmarshal("true"));
		
		assertEquals(new Boolean(true), s.getSimpleBoolean());
	}
	
	@Test
	public void TestBooleanScalarGetsCorrectPrimitiveValue() throws Exception
	{
		
		Simpleprimboolean s = new Simpleprimboolean();
		s.setSimpleprimboolean(true);
		
		Field f = s.getClass().getDeclaredField("simpleprimboolean");
		
		BooleanType bt = new BooleanType();
		assertEquals("true", bt.getFieldString(f, s));
	}
	
	@Test
	public void TestBooleanScalarSetsCorrectPrimitiveValue() throws Exception
	{
		Simpleprimboolean s = new Simpleprimboolean();
		s.setSimpleprimboolean(false);
		
		Field f = s.getClass().getDeclaredField("simpleprimboolean");
		
		BooleanType bt = new BooleanType();
		
		bt.setFieldValue("true", f, s);

		assertEquals(true, s.getSimpleprimboolean());
	}
	
	@Test
	public void TestBooleanScalarChecksDefaultValueForNonPritive() throws Exception 
	{
		
		BooleanType bt = new BooleanType();
		
		SimpleBoolean s = new SimpleBoolean();

		Field f = s.getClass().getDeclaredField("simpleboolean");
		
		s.setSimpleBoolean(new Boolean(false));
		
		assertTrue(bt.isFieldDefaultValue(f, s));
		
		s.setSimpleBoolean(new Boolean(true));
		
		assertFalse(bt.isFieldDefaultValue(f, s));
	}

	@Test
	public void TestBooleanScalarChecksDefaultValueForPritive() throws Exception 
	{
		
		BooleanType bt = new BooleanType();
		
		Simpleprimboolean s = new Simpleprimboolean();

		Field f = s.getClass().getDeclaredField("simpleprimboolean");
		
		s.setSimpleprimboolean(false);
		
		assertTrue(bt.isFieldDefaultValue(f, s));
		
		s.setSimpleprimboolean(true);
		
		assertFalse(bt.isFieldDefaultValue(f, s));
	}


}
