package ecologylab.simpl;

import static org.junit.Assert.*;

import org.junit.Test;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.EnumerationDescriptor;
import ecologylab.serialization.SIMPLDescriptionException;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.primaryScenarioEnum;
import ecologylab.serialization.secondaryScenarioEnum;
import ecologylab.serialization.formatenums.StringFormat;

public class EnumDescriptionDeSerializationTest {

	
	@Test
	public void letsTrySomethingTricksy()
	{
		ClassDescriptor cd = ClassDescriptor.getClassDescriptor(primaryScenarioEnum.class);
		cd.toString();
	}

	@Test
	public void EnumDescriptorCanBeDescribed()
	{
		ClassDescriptor cd = ClassDescriptor.getClassDescriptor(EnumerationDescriptor.class);
	}
	
	@Test
	public void descriptionWillCorrectlySerializeToXML() throws SIMPLDescriptionException, SIMPLTranslationException
	{
		EnumerationDescriptionTestPerFormat(primaryScenarioEnum.class, StringFormat.XML, false);	
	}
	
	@Test
	public void descriptionWillCorrectlySerializeToJSON() throws SIMPLDescriptionException, SIMPLTranslationException
	{
		EnumerationDescriptionTestPerFormat(primaryScenarioEnum.class, StringFormat.JSON, false);	
	}
	
	@Test
	public void descriptionWithValuesWillCorrectlySerializeToXML() throws SIMPLDescriptionException, SIMPLTranslationException
	{
		EnumerationDescriptionTestPerFormat(secondaryScenarioEnum.class, StringFormat.XML, true);	
	}
	
	@Test
	public void descriptionWithValuesWillCorrectlySerializeToJSON() throws SIMPLDescriptionException, SIMPLTranslationException
	{
		EnumerationDescriptionTestPerFormat(secondaryScenarioEnum.class, StringFormat.JSON, true);	
	}
	
	private void EnumerationDescriptionTestPerFormat(Class<?> forEnumClass, StringFormat theFormat, boolean validateValues) throws SIMPLDescriptionException, SIMPLTranslationException
	{
		EnumerationDescriptor primaryScenario = EnumerationDescriptor.get(forEnumClass);
		
		SimplTypesScope sts = SimplTypesScope.get("enumTest", EnumerationDescriptor.class);
		StringBuilder staticSerialized = SimplTypesScope.serialize(primaryScenario, theFormat);
		StringBuilder specificSTSSerialized = sts.serialize(primaryScenario, theFormat);
		
		assertTrue(staticSerialized.toString().equals(specificSTSSerialized.toString()));
		System.out.println(staticSerialized.toString());
		System.out.println(specificSTSSerialized.toString());
		
		EnumerationDescriptor recaptured = (EnumerationDescriptor) sts.deserialize(staticSerialized.toString(), theFormat);
		performBasicValidations(forEnumClass, recaptured);	
		
		if(validateValues)
		{
			performValueValidation(recaptured);
		}
	}
	
	/**
	 * This handles some of the validations that should apply for most of the test cases; 
	 * these have been copy pasted from the prior test case so they can be reused...
	 * I could have refactored this in the first test case to a method call, but I felt it was nicer w/ the core assumptions there.
	 * @param ed
	 */
	private void performBasicValidations(Class<?> enumClass, EnumerationDescriptor ed)
	{
		
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

	private void performValueValidation(EnumerationDescriptor ed)
	{
		assertEquals(new Integer(3), ed.getEntryEnumIntegerValue("firstValue"));
		assertEquals(new Integer(5), ed.getEntryEnumIntegerValue("secondValue"));
		assertEquals(new Integer(7), ed.getEntryEnumIntegerValue("thirdValue"));
		
		assertEquals("firstValue", ed.getEntryEnumFromValue(3).toString());
		assertEquals("secondValue", ed.getEntryEnumFromValue(5).toString());
		assertEquals("thirdValue", ed.getEntryEnumFromValue(7).toString());
	}
}
