package ecologylab.serialization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import org.junit.Test;

import simpl.annotations.dbal.simpl_inherit_parent_tag;
import simpl.core.ScalarUnmarshallingContext;
import simpl.tools.XMLTools;
import simpl.types.ScalarType;


public class XMLToolsTest {

	@Test
	public void XmlTagInheritedFromParent()
	{
		@simpl_inherit_parent_tag
		final class WonkyDummyClassName extends ScalarType<String>{
			@Override
			public String getInstance(String value, String[] formatStrings,
					ScalarUnmarshallingContext scalarUnmarshallingContext) {
				// TODO Auto-generated method stub
				return null;
			}
		}
		
		String Class_BaseTagName = XMLTools.getXmlTagName(ScalarType.class,"");
		String Class_WonkyTagName = XMLTools.getXmlTagName(WonkyDummyClassName.class,"");
		assertEquals(Class_BaseTagName, Class_WonkyTagName);
		assertNotNull(Class_WonkyTagName);
		assertNotNull(Class_BaseTagName);
	}
	
	@Test
	/**
	 * Tests that XMLTools identifies collections correctly, and that it also determines enum collections correctly.
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public void XMLToolsIsCollectionIdentifiesEnumCollection() throws NoSuchFieldException, SecurityException
	{
		Class<?> lass = enumIssueTestClass.class;
		
		Field f = lass.getDeclaredField("ourUsages");
		assertNotNull(f);
		assertTrue("Failed to represent field F as a collection. F was : " + f.getName() + " of type: " + f.getType().getName(), XMLTools.representAsCollection(f));
		assertTrue("Expected F to be an enumeation collection", XMLTools.isEnumCollection(f));
		
		Field notGeneric = lass.getDeclaredField("notGeneric");		
		assertNotNull(notGeneric);
		assertTrue("expected array list to be collection", XMLTools.representAsCollection(notGeneric));
		assertFalse("non-generic Should not be an enum", XMLTools.isEnum(notGeneric));
		
	
		Field composite = lass.getDeclaredField("composite");
		assertNotNull(composite);
		assertTrue("Composite collection is a collection!", XMLTools.representAsCollection(composite));
		assertFalse("Composite collection IS NOT an enum collection!", XMLTools.isEnumCollection(composite));
	}

}
