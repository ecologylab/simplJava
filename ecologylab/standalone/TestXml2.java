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
public class TestXml2 extends ElementState
{
	@simpl_nowrap 
	@simpl_collection("vendor_case")	ArrayList<String> vendors	= new ArrayList<String>();
	
	@simpl_nowrap
	@simpl_collection("producer")	ArrayList<String>	producers = new ArrayList<String>(); 
	
	@simpl_scalar				String fooBar;
	
	@simpl_scalar 			String property;
	
	@simpl_scalar 			String source; 
	
	@simpl_scalar String test; 
	
	static final TranslationScope TS	= TranslationScope.get("testing123", TestXml2.class);

	public TestXml2()
	{
		super();
	}

		static final String STUFF = 
			"<test_xml2 property=\"xml\" source=\"ecologylab\" foo_bar=\"baz\">" +
				"<items>" +
				"<country name=\"korea\"></country>" +
				"<country name=\"usa\"></country>" +
				"</items>" +
				"<producer>ecologylab</producer>" +
				"<producer>tamu</producer>" +
				"<vendor_case>Garmin</vendor_case>" +
				"<vendor_case>Amazon</vendor_case>" +
				"</test_xml2>";
    
		public static void main(String[] a)
    {
    	try
		{
			TestXml2 es		= (TestXml2)TS.deserializeCharSequence(STUFF);
			
			es.getVendors().add("GMC");
			es.getVendors().add("ABC");
			ArrayList<String> s = es.getVendors();
			
			for (String string : s)
			{
				System.out.println(string);
			}
			
			es.getProducers().add("Computer science");
			es.getProducers().add("texas");
			ArrayList<String> s1 = es.getProducers(); 
			for(String st: s1)
				System.out.println(st);
			
			println(es.serialize());
			
		} catch (SIMPLTranslationException e)
		{
			e.printStackTrace();
		}
    	
    	
    }
		
		public ArrayList<String> getProducers()
		{
			return producers;
		}

		public void setProducers(ArrayList<String> producers)
		{
			this.producers = producers;
		}
		

		public String getFooBar()
		{
			return fooBar;
		}
		public ArrayList<String> getVendors()
		{
			return vendors;
		}

		public void setVendors(ArrayList<String> vendors)
		{
			this.vendors = vendors;
		}

		public void setFooBar(String fooBar)
		{
			this.fooBar = fooBar;
		}
}
