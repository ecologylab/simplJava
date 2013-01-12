package ecologylab.serialization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EnumerationDescriptionTest {

	@Test(expected=SIMPLDescriptionException.class)
	public void descriptionsForNonEnumTypesProvokeException() throws SIMPLDescriptionException
	{
		EnumerationDescriptor invalid = EnumerationDescriptor.get(ClassDescriptor.class);
	}
	
	@Test
	public void descriptionForBasicEnumerationsWorks() throws SIMPLDescriptionException {
		EnumerationDescriptor ed = EnumerationDescriptor.get(primaryScenarioEnum.class);
	   
		assertTrue("There is no information about this enum", ed.metaInfo.isEmpty());
		assertTrue("There are no other tags for this enum", ed.otherTags.isEmpty());
		
		assertEquals("The packageName must be correct" , primaryScenarioEnum.class.getPackage().getName(), ed.getPackageName());
		assertEquals("The javaName must be correct", primaryScenarioEnum.class.getName(), ed.getJavaTypeName());
		assertEquals("The Simpl name must be correct", "primaryScenarioEnum", ed.getName());
		
		assertEquals("There should be three entries for this enum", 3, ed.getEnumerationEntries().size());
		
		assertTrue("Should contain all of the values of the enum! Missing first", ed.containsEntry("firstValue"));
		assertTrue("Should contain all of the values of the enum! Missing second", ed.containsEntry("secondValue"));
		assertTrue("Should contain all of the values of the enum! Missing third", ed.containsEntry("thirdValue"));
		assertFalse("Should not contain a value that isn't in the enum! There are no Jibbles here!", ed.containsEntry("jibbles"));
		assertFalse("Should be case sensitive! FIRSTENTRY ignores casing.", ed.containsEntry("FIRSTENTRY"));
		
		assertEquals("Should fetch the correct enumeration value; expected firstValue", primaryScenarioEnum.firstValue, ed.getEntryEnumValue("firstValue"));
		assertEquals("Should fetch the correct enumeration value; expected secondValue", primaryScenarioEnum.secondValue, ed.getEntryEnumValue("secondValue"));
		assertEquals("Should fetch the correct enumeration value; expected thirdValue", primaryScenarioEnum.thirdValue, ed.getEntryEnumValue("thirdValue"));
	}
	
	/**
	 * This handles some of the validations that should apply for most of the test cases; 
	 * these have been copy pasted from the prior test case so they can be reused...
	 * I could have refactored this in the first test case to a method call, but I felt it was nicer w/ the core assumptions there.
	 * @param ed
	 */
	private void performBasicValidations(Class<?> enumClass, EnumerationDescriptor ed)
	{
		assertTrue("There is no information about this enum", ed.metaInfo.isEmpty());
		assertTrue("There are no other tags for this enum", ed.otherTags.isEmpty());
		
		assertEquals("The packageName must be correct" , enumClass.getPackage().getName(), ed.getPackageName());
		assertEquals("The javaName must be correct", enumClass.getName(), ed.getJavaTypeName());
		assertEquals("The Simpl name must be correct", enumClass.getSimpleName(), ed.getName());
		
		assertEquals("There should be three entries for this enum", 3, ed.getEnumerationEntries().size());
		
		assertTrue("Should contain all of the values of the enum! Missing first", ed.containsEntry("firstValue"));
		assertTrue("Should contain all of the values of the enum! Missing second", ed.containsEntry("secondValue"));
		assertTrue("Should contain all of the values of the enum! Missing third", ed.containsEntry("thirdValue"));
		assertFalse("Should not contain a value that isn't in the enum! There are no Jibbles here!", ed.containsEntry("jibbles"));
		assertFalse("Should be case sensitive! FIRSTENTRY ignores casing.", ed.containsEntry("FIRSTENTRY"));
	}
	
	@Test(expected=SIMPLDescriptionException.class)
	public void descriptionForCustomValuedEnumerationsWithoutASimplFieldProvokesException() throws SIMPLDescriptionException
	{
		EnumerationDescriptor invalid = EnumerationDescriptor.get(invalidEnumExample.class);
	}
	
	@Test(expected=SIMPLDescriptionException.class)
	public void descriptionForCustomValuedEnumerationsWithInvalidSimplFieldTypesProvokesException() throws SIMPLDescriptionException
	{
		EnumerationDescriptor invalid = EnumerationDescriptor.get(secondaryScenarioRejectsNonIntegers.class);
	}
	
	@Test
	public void customValuedEnumerartionsAreCorrectlyIdentified()
	{		
		// Valid "custom valued" enumerations should, of course, be identified as such
		assertTrue(EnumerationDescriptor.isCustomValuedEnum(secondaryScenarioEnum.class));
		assertTrue(EnumerationDescriptor.isCustomValuedEnum(secondaryScenarioAlsoSupportsPrimitiveIntEnum.class));
		
		// Invalid "custom valued" enumerations (due to typing issues / lack of simpl_scalar) should be identified as "custom valued" 
		// so that other description code can through the appropriate exceptions
		assertTrue(EnumerationDescriptor.isCustomValuedEnum(secondaryScenarioRejectsNonIntegers.class));
		
		// Standard enumerations (like the basic scenario) should NOT be considered custom valued...
		// Because, duh, they're not.
		assertFalse("This enumeration has no custom values.", EnumerationDescriptor.isCustomValuedEnum(primaryScenarioEnum.class));
		
		
		// Things that are not enums are also, very obviously, not Custom Valued enumerations
		assertFalse(EnumerationDescriptor.isCustomValuedEnum(String.class));
	}
	
	@Test
	public void descriptionForCustomValuedEnumerationsWorks() throws SIMPLDescriptionException {
		
		EnumerationDescriptor ed = EnumerationDescriptor.get(secondaryScenarioEnum.class);
		performBasicValidations(secondaryScenarioEnum.class, ed);
		
		// let's get the core first, then do some value marshalling. ;P
		assertEquals(new Integer(3), ed.getEntryEnumIntegerValue("firstValue"));
		assertEquals(new Integer(5), ed.getEntryEnumIntegerValue("secondValue"));
		assertEquals(new Integer(7), ed.getEntryEnumIntegerValue("thirdValue"));
		
		EnumerationDescriptor secondStyle = EnumerationDescriptor.get(secondaryScenarioAlsoSupportsPrimitiveIntEnum.class);
		performBasicValidations(secondaryScenarioAlsoSupportsPrimitiveIntEnum.class, secondStyle);
		performValueValidation(secondStyle);
	}
	
	private void performValueValidation(EnumerationDescriptor ed)
	{
		assertEquals(new Integer(3), ed.getEntryEnumIntegerValue("firstValue"));
		assertEquals(new Integer(5), ed.getEntryEnumIntegerValue("secondValue"));
		assertEquals(new Integer(7), ed.getEntryEnumIntegerValue("thirdValue"));
		
		assertEquals("firstValue", ed.getEntryEnumFromValue(3).toString());
		assertEquals("secondValue", ed.getEntryEnumFromValue(5).toString());
		assertEquals("thirdValue", ed.getEntryEnumFromValue(7).toString());
		
	}
	
	@Test
	public void enumerationDescriptionMarshalsValuesCorrectlyForNormalEnums() throws SIMPLDescriptionException, SIMPLTranslationException
	{
		// This is like, the simplest case. If this goes wrong, tears and shame should be part of the fix. 
		EnumerationDescriptor ed = EnumerationDescriptor.get(primaryScenarioEnum.class);
		assertEquals("firstValue", ed.marshal(primaryScenarioEnum.firstValue));
		assertEquals("secondValue", ed.marshal(primaryScenarioEnum.secondValue));
	}
	
	@Test
	public void enumerationDescriptionMarshalsValuesForCustomValuedEnums() throws SIMPLTranslationException, SIMPLDescriptionException
	{
		// This is like, the simplest case. If this goes wrong, tears and shame should be part of the fix. 
		EnumerationDescriptor ed = EnumerationDescriptor.get(secondaryScenarioEnum.class);
		assertEquals("firstValue", ed.marshal(secondaryScenarioEnum.firstValue));
		assertEquals("secondValue", ed.marshal(secondaryScenarioEnum.secondValue));
		// Yes. This test was basically copy pasted.
	}

	
	@Test(expected=SIMPLTranslationException.class)
	public void enumerationDescriptionMarshallingThrowsExceptionForInvalidValuesAndTypes() throws SIMPLDescriptionException, SIMPLTranslationException 
	{
		EnumerationDescriptor ed = EnumerationDescriptor.get(primaryScenarioEnum.class);	
		ed.marshal("LOL A STRING"); // Strings are not enums, silly!
		ed.marshal(secondaryScenarioEnum.secondValue); // That's not the right enum to marshal, silly!
		ed.marshal(null); // Woah! Null?! I don't THINK so. 
	}
	
	@Test
	public void enumerationDescriptionUnmarshallsValuesForCustomValuedEnums() throws SIMPLDescriptionException, SIMPLTranslationException
	{
		EnumerationDescriptor ed = EnumerationDescriptor.get(secondaryScenarioEnum.class);	
		assertEquals(secondaryScenarioEnum.firstValue, ed.unmarshal("firstValue"));
		assertEquals(secondaryScenarioEnum.firstValue, ed.unmarshal("3"));
		
		assertEquals(secondaryScenarioEnum.thirdValue, ed.unmarshal("thirdValue"));
		assertEquals(secondaryScenarioEnum.thirdValue, ed.unmarshal("7"));
	}
	
	@Test(expected=SIMPLTranslationException.class)
	public void enumerationDescriptionThrowsExceptionOnUnmarshallingNonExistantNames() throws SIMPLDescriptionException, SIMPLTranslationException
	{
		EnumerationDescriptor ed = EnumerationDescriptor.get(secondaryScenarioEnum.class);	
		ed.unmarshal(null);// Null? NOPE.
		ed.unmarshal(""); // Empty? NOPE.
		ed.unmarshal("thisIsSomeValueThatIsNotInTheEnumerationAtAll"); // Not gonna happen.
	}
	
	@Test(expected=SIMPLTranslationException.class)
	public void enumerationDescriptionThrowsExceptionOnUnmarshallingNonExistantValues() throws SIMPLDescriptionException, SIMPLTranslationException
	{
		EnumerationDescriptor ed = EnumerationDescriptor.get(secondaryScenarioEnum.class);	
		ed.unmarshal("-1"); // Not in there.
		ed.unmarshal("90001"); // also not in there.
	}
}

