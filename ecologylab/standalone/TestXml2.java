/**
 * 
 */
package ecologylab.standalone;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;


public class TestXml2 extends ElementState 
{
	@simpl_nowrap 
	@simpl_collection("vendor_case")	ArrayList<String> vendors	= new ArrayList<String>();
	
	@simpl_collection("producer")	ArrayList<String>	producers = new ArrayList<String>();
	@simpl_nowrap
	@simpl_collection("test_items") ArrayList<TestItems> test_items = new ArrayList<TestItems>(); 
	
	@simpl_scalar				String fooBar;
	
	@simpl_scalar 			String property;
	
	@simpl_scalar 			String source; 
	
	@simpl_scalar String test;
	
	static final TranslationScope TS	= TranslationScope.get("testing123", TestXml2.class, TestItems.class);
	
	static final String STUFF = 
			"<test_xml2 property=\"xml\" source=\"ecologylab\" foo_bar=\"baz\">" +
				"<test_items>" +
					"<country>Korea</country>" +
					"<country>US</country>" +
					"<country>China</country>" + 
				"</test_items>" +
				"<producers>" +
					"<producer>Ecologylab</producer>" +
					"<producer>Tamu</producer>" +
				"</producers>" + 
				"<vendor_case>Garmin</vendor_case>" +
				"<vendor_case>Amazon</vendor_case>" +
				"</test_xml2>";
    
		public static void main(String args[]) throws SIMPLTranslationException{		
			TestXml2 es		= (TestXml2)TS.deserializeCharSequence(STUFF);
			
			es.getVendors().add("added GMC");
			es.getVendors().add("added ABC");
			ArrayList<String> s = es.getVendors();
//			for (String string : s)
//				System.out.println(string);
			
			es.getProducers().add("added Computer Science");
			es.getProducers().add("added Texas");
			ArrayList<String> s1 = es.getProducers(); 
//			for(String st: s1)
//				System.out.println(st);
			
			System.out.println(es.serialize());
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
