/**
 * 
 */
package ecologylab.standalone;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_nowrap;
import ecologylab.serialization.annotations.simpl_scalar;

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
			ElementState es		= TS.deserializeCharSequence(STUFF);
			println(es.serialize());
		} catch (SIMPLTranslationException e)
		{
			e.printStackTrace();
		}
    	
    	
    }
}
