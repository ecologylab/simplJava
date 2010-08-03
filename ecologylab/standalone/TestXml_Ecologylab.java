/**
 * 
 */
package ecologylab.standalone;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;


public class TestXml_Ecologylab   
{
	public TestXml_Ecologylab(){	}
	
	public static class TestXml2 extends ElementState{
		@simpl_nowrap
		@simpl_collection("test_items") ArrayList<TestItems> test_items = new ArrayList<TestItems>();

		@simpl_collection("producer")	ArrayList<String>	producers = new ArrayList<String>();
		
		@simpl_nowrap 
		@simpl_collection("vendor_case")	ArrayList<String> vendors	= new ArrayList<String>();

		@simpl_scalar 			String property;

		@simpl_scalar 			String source;
		
		@simpl_scalar			String fooBar;
		
		@simpl_scalar 			String test;
		
		static final TranslationScope TS = TranslationScope.get("testing123", TestXml2.class, TestItems.class, TestSubItem.class, TestSubItem2.class);
		
		static final String STUFF = 
				"<test_xml2 property=\"xml\" source=\"ecologylab\" foo_bar=\"baz\">" +
					"<test_items name=\"countries\">" +
						"<test_sub_item priority=\"1\" target=\"ab\">Korea</test_sub_item>" +
						"<test_sub_item priority=\"2\">US</test_sub_item>" +
						"<test_sub_item priority=\"3\">China</test_sub_item>" +
						"<test_sub_item2 priority=\"9\">Russia</test_sub_item2>" + 
					"</test_items>" +
					"<test_items priority=\"low\">" +
						"<test_sub_item priority=\"4\">Another country</test_sub_item>" + 
					"</test_items>" + 
					"<producers>" +
						"<producer>Ecologylab</producer>" +
						"<producer>Tamu</producer>" +
					"</producers>" + 
					"<vendor_case>Garmin</vendor_case>" +
					"<vendor_case>Amazon</vendor_case>" +
				"</test_xml2>";
		
		//TODO : extract element value of test_sub_item
		public static void testTestXml2() throws SIMPLTranslationException{
			TestXml2 es	= (TestXml2)TS.deserializeCharSequence(STUFF);
			
			es.getVendors().add("added GMC");
			es.getVendors().add("added ABC");
//			ArrayList<String> s = es.getVendors();
//				for (String string : s)
//					System.out.println(string);
			
			es.getProducers().add("added Computer Science");
			es.getProducers().add("added Texas");
//			ArrayList<String> s1 = es.getProducers(); 
//				for(String st: s1)
//					System.out.println(st);
			
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
	
	public static class TestRss extends ElementState{
		@simpl_scalar float version; 
		
		static String RSS = 
				"<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
				"<test_rss version=\"2.0\">" +
					"<test_channel>" +
						"<title>xkcd.com</title>" +
						"<link>http://xkcd.com/</link>" +
						"<description>webcomic</description>" +
						"<language>en</language>" +
						"<test_item>" +
							"<title>Atheists</title>" +
							"<link>http://xkcd.com/774/</link>" +
							"<description>atheists.png</description>" +
							"<pubDate>Mon, 02 Aug 2010 04:00:00 -0000</pubDate>" +
							"<guid>http://xkcd.com/774/</guid>" +
						"</test_item>" +
						"<test_item>" +
							"<title>University Website</title>" +
							"<link>http://xkcd.com/773/</link>" +
							"<description>university_website.png</description>" +
							"<pubDate>Fri, 30 Jul 2010 04:00:00 -0000</pubDate>" +
							"<guid>http://xkcd.com/773/</guid>" +
						"</test_item>" + 
					"</test_channel>" +
				"</test_rss>";
		
		public static void testRss(){
		}
		
	public static class BingSearch extends ElementState{
		static String bingSearch; 
		//http://api.bing.net/xml.aspx?Sources=image&AppId=828DA72AA6D172560F256E7B3784FA2295CB7D99&Version=2.2&Market=en-US&Query=texas&Web.Count=20
	}
	}
	
	public static void main(String args[]) throws SIMPLTranslationException{		
//		TestXml2.testTestXml2(); 
		TestRss.testRss(); 
	}
}
