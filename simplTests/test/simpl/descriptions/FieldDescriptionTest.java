package simpl.descriptions;

import org.junit.Test;

import simpl.annotations.dbal.simpl_scalar;
import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.FieldDescriptor;
import simpl.descriptions.FieldType;
import simpl.exceptions.SIMPLTranslationException;
import static org.junit.Assert.*;

import ecologylab.serialization.primaryScenarioEnum;

public class FieldDescriptionTest {

	@Test
	public void testDescribeEnumerationField() throws SIMPLTranslationException{
		final class anEnumField
		{
			@simpl_scalar
			private primaryScenarioEnum myEnum;
		}
		
		ClassDescriptor cd = ClassDescriptor.getClassDescriptor(anEnumField.class);
		assertEquals(1,cd.allFieldDescriptors().size());
		
		FieldDescriptor fd = (FieldDescriptor)cd.allFieldDescriptors().get(0);
		assertEquals(FieldType.SCALAR, fd.getType());
		assertEquals(primaryScenarioEnum.class, fd.getFieldType());
		assertEquals(true, fd.isEnum());
		assertNotNull(fd.getEnumerationDescriptor());
		assertEquals("firstValue", fd.getEnumerationDescriptor().marshal(primaryScenarioEnum.firstValue));
		assertEquals(primaryScenarioEnum.firstValue, fd.getEnumerationDescriptor().unmarshal("firstValue"));
		
	}
}
