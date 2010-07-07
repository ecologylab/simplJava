/**
 * 
 */
package ecologylab.standalone;

import java.util.ArrayList;

import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationScope;
import ecologylab.xml.XMLTranslationException;
import ecologylab.xml.ElementState.simpl_nowrap;

/**
 *
 * @author andruid
 */
public class TestXml extends ElementState
{
	@simpl_nowrap 
	@simpl_collection("vendor")	ArrayList<String> set	= new ArrayList<String>();
	
	@simpl_scalar				String fooBar;
	
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
			println(es.serialize());
		} catch (XMLTranslationException e)
		{
			e.printStackTrace();
		}
    	
    	
    }
}
