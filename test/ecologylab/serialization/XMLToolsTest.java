package ecologylab.serialization;

import static org.junit.Assert.*;

import org.junit.Test;

import ecologylab.serialization.annotations.simpl_inherit_parent_tag;
import ecologylab.serialization.types.ScalarType;

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

}
