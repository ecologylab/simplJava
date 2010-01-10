/**
 * 
 */
package ecologylab.standalone;

import java.util.ArrayList;

import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationScope;
import ecologylab.xml.XMLTranslationException;
import ecologylab.xml.ElementState.xml_nowrap;

/**
 *
 * @author andruid
 */
public class TestXml extends ElementState
{
	@xml_nowrap 
	@xml_collection("vendor")	ArrayList<String> set	= new ArrayList<String>();
	
	@xml_attribute				String fooBar;
	
	static final TranslationScope TS	= TranslationScope.get("testing123", TestXml.class);
	/**
	 * 
	 */
	public TestXml()
	{
		super();

	}

    static final String STUFF = "<test_xml foo_bar=\"baz\"><vendor>Garmin</vendor><vendor>Amazon</vendor></test_xml>";
    public static void main(String[] a)
    {
    	try
		{
			ElementState es		= translateFromXMLCharSequence(STUFF, TS);
			println(es.translateToXML());
		} catch (XMLTranslationException e)
		{
			e.printStackTrace();
		}
    	
    	
    }
}
