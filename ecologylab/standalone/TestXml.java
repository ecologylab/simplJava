/**
 * 
 */
package ecologylab.standalone;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;

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

    public ArrayList<String> getSet()
	{
		return set;
	}
	public void setSet(ArrayList<String> set)
	{
		this.set = set;
	}
	public String getFooBar()
	{
		return fooBar;
	}
	public void setFooBar(String fooBar)
	{
		this.fooBar = fooBar;
	}

		static final String STUFF = "<test_xml foo_bar=\"baz\"><vendor>Garmin</vendor><vendor>Amazon</vendor></test_xml>";
    
		public static void main(String[] a)
    {
    	try
		{
			TestXml es		= (TestXml)TS.deserializeCharSequence(STUFF);
			
			es.getSet().add("GMC");
			es.getSet().add("ABC");
			ArrayList<String> s = es.getSet();
			
			for (String string : s)
			{
				System.out.println(string);
			}
			
			println(es.serialize());
			
		} catch (SIMPLTranslationException e)
		{
			e.printStackTrace();
		}
    	
    	
    }
}
