package ecologylab.simpl;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.FieldType;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.primaryScenarioEnum;
import ecologylab.serialization.secondaryScenarioEnum;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.formatenums.StringFormat;

public class EnumerationSerializationDeserialization {
	
	// TODO: MAPS... OTHER FORMATS. :3 

	private void validateBasicSerializationCases(Object toSerialize, String toExpect, StringFormat format) throws SIMPLTranslationException
	{
		StringBuilder result = SimplTypesScope.serialize(toSerialize, format);
		
		System.out.println(result.toString());
		// Really sneaky way to check if firstValue is in the XML / JSON.
		assertTrue(result.toString().contains(toExpect));
	}
	
	@Test
	public void testSerializeBasicScalar() throws SIMPLTranslationException
	{
		basicEnumerationScalar baseCase = new basicEnumerationScalar();
		baseCase.ourEnum = primaryScenarioEnum.firstValue;
		
		validateBasicSerializationCases(baseCase, "firstValue", StringFormat.XML);
		validateBasicSerializationCases(baseCase, "firstValue", StringFormat.JSON);
	
		// Should like so in XML: 
		// <basic_enumeration_scalar our_enum="firstValue"/>
		// JSON is: {"basic_enumeration_scalar":{"our_enum":"firstValue"}}

	}
	
	// These are pretty much the same for serialize
	// deserialize is going ot be diferent though. :3
	@Test
	public void testSerializeValuedScalar() throws SIMPLTranslationException
	{
		customValuedEnumerationScalar valuedCase = new customValuedEnumerationScalar();
		valuedCase.ourEnum = secondaryScenarioEnum.secondValue;

		validateBasicSerializationCases(valuedCase, "secondValue", StringFormat.XML);
		validateBasicSerializationCases(valuedCase, "secondValue", StringFormat.JSON);
		
		//<custom_valued_enumeration_scalar our_enum="secondValue"/>
		//{"custom_valued_enumeration_scalar":{"our_enum":"secondValue"}}
	}
	
	private static SimplTypesScope ourSTS = SimplTypesScope.get("enumTestsDeSerialize", primaryScenarioEnum.class, secondaryScenarioEnum.class,
			customValuedEnumerationScalar.class, basicEnumerationScalar.class, basicEnumerationList.class);

	
	private void validateDeserialization(String representation, StringFormat format, Object expected) throws SIMPLTranslationException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
	{
		Object result = ourSTS.deserialize(representation, format);
		
		Field ourField = result.getClass().getField("ourEnum");
		ourField.setAccessible(true);
		
		Object value = ourField.get(result);
		assertEquals(expected, value);
	}
	
	@Test
	public void testDeserializeBaseCase() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, SIMPLTranslationException
	{
		basicEnumerationScalar baseCase = new basicEnumerationScalar();
		baseCase.ourEnum = primaryScenarioEnum.firstValue;
	
		validateDeserialization("<basic_enumeration_scalar our_enum=\"firstValue\"/>", StringFormat.XML, primaryScenarioEnum.firstValue);
		validateDeserialization("{\"basic_enumeration_scalar\":{\"our_enum\":\"secondValue\"}}", StringFormat.JSON, primaryScenarioEnum.secondValue);
	}
	
	@Test
	public void testDeserializeCustomValuedCase() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, SIMPLTranslationException
	{
		validateDeserialization("<custom_valued_enumeration_scalar our_enum=\"secondValue\"/>", StringFormat.XML, secondaryScenarioEnum.secondValue);
		validateDeserialization("<custom_valued_enumeration_scalar our_enum=\"5\"/>", StringFormat.XML, secondaryScenarioEnum.secondValue);
				
		validateDeserialization("{\"custom_valued_enumeration_scalar\":{\"our_enum\":\"secondValue\"}}", StringFormat.JSON, secondaryScenarioEnum.secondValue);
		validateDeserialization("{\"custom_valued_enumeration_scalar\":{\"our_enum\":\"5\"}}", StringFormat.JSON, secondaryScenarioEnum.secondValue);
	}
	

	
	@Test
	public void EnumerationListsCanBeDescribed()
	{
		ClassDescriptor cd = ClassDescriptor.getClassDescriptor(basicEnumerationList.class);
		assertEquals(1,cd.allFieldDescriptors().size());
		
		FieldDescriptor fd = (FieldDescriptor) cd.allFieldDescriptors().get(0);
	
		// Yeah, that's not a guarentee. I don't feel comfy making enums "elements" 
		// but it makes sense insofar as they rely upon the type scope. :\
		
		
		assertEquals(FieldType.COLLECTION_ELEMENT, fd.getType());
		assertEquals("ourEnumList", fd.getName());
	}
	
	
	
	
	// TODO: Tests for lists; let's get the base case fixed first. 
	
	
	

}
