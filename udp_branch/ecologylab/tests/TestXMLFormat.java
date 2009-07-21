/**
 * 
 */
package ecologylab.tests;

import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationScope;
import ecologylab.xml.XMLTranslationException;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public class TestXMLFormat extends ElementState
{
	@xml_attribute @xml_format("#") double			decimal0	= 1.654654654654645321321;

	@xml_attribute @xml_format("#.#") double		decimal1	= -1.654654654654645321321;

	@xml_attribute @xml_format("#.0#") double		decimal2	= -11111.654654654654645321321;

	@xml_attribute @xml_format("#.###") double	decimal3	= 0.654654654654645321321;

	public TestXMLFormat()
	{

	}

	/**
	 * @param args
	 * @throws XMLTranslationException
	 */
	public static void main(String[] args) throws XMLTranslationException
	{
		TestXMLFormat t = new TestXMLFormat();

		t.translateToXML(System.out);
		System.out.println();

		ElementState.translateFromXMLCharSequence(t.translateToXML().toString(),
				TranslationScope.get("test", TestXMLFormat.class)).translateToXML(
				System.out);
		System.out.println();

		System.out.println(t.translateToXML());
	}

}
