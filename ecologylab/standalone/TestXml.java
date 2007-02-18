/**
 * 
 */
package ecologylab.standalone;

import java.util.ArrayList;

import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XmlTranslationException;

/**
 *
 * @author andruid
 */
public class TestXml extends ElementState
{
	@xml_collection("vendor")	ArrayList<String> set	= new ArrayList<String>();
	
	@xml_collection("foo bar")	String fooBar;
	
	
	static final TranslationSpace TS	= TranslationSpace.get("ecologylab.standalone");
	/**
	 * 
	 */
	public TestXml()
	{
		super();

	}

    static final String STUFF = "<test_xml><vendor>Garmin</vendor><vendor>Amazon</vendor></test_xml>";
    public static void main(String[] a)
    {
    	try
		{
			ElementState es		= translateFromXMLString(STUFF, TS);
			String xmlString	= es.translateToXML(false);
			println(xmlString);
		} catch (XmlTranslationException e)
		{
			e.printStackTrace();
		}
    	
    	
    }
}
