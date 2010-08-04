/**
 * 
 */
package ecologylab.standalone.researchnotebook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;


public class TestXml_Ecologylab   
{
	public TestXml_Ecologylab(){	}
	
	public static class TestXml2State extends ElementState{
		@simpl_nowrap
		@simpl_collection("test_items") ArrayList<TestItems> test_items = new ArrayList<TestItems>();

		@simpl_collection("producer")	ArrayList<String>	producers = new ArrayList<String>();
		
		@simpl_nowrap 
		@simpl_collection("vendor_case")	ArrayList<String> vendors	= new ArrayList<String>();

		@simpl_scalar 			String property;

		@simpl_scalar 			String source;
		
		@simpl_scalar			String fooBar;
		
		@simpl_scalar 			String test;
		
		static final TranslationScope TS = TranslationScope.get("testing123", TestXml2State.class, TestItems.class, TestSubItem.class, TestSubItem2.class);
		
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
			TestXml2State es	= (TestXml2State)TS.deserializeCharSequence(STUFF);
			
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
	
	// cf. RssState.java Channel.java 
	public static class TestRssState extends ElementState{
		@simpl_scalar float version; 
		
		@simpl_nowrap
		@simpl_collection("test_channel") ArrayList<TestChannel> channels = new ArrayList<TestChannel>(); 
		
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
		
		static TranslationScope TS = TranslationScope.get("TestRss", TestRssState.class, TestChannel.class, TestItem.class);
		
		public static void testRss() throws SIMPLTranslationException, MalformedURLException{
			TestRssState r = (TestRssState)TS.deserializeCharSequence(RSS);
			r.serialize(System.out);
			
			TestChannel tc = new TestChannel(); 
			tc.setTitle("test channel");
			tc.setDescription("test description");
			tc.setLanguage("ko");
			tc.setLink(new ParsedURL(new URL("http://www.korea.net/")));
			
			TestItem i = new TestItem(); 
			i.setTitle("test item");
			i.setDescription("test item description");
			i.setPubDate("2010/08/03");
			
			tc.getItems().add(i);
			r.getChannels().add(tc);
			
			System.out.println("\nafter adding data ---");
			r.serialize(System.out);
		}
		
		public ArrayList<TestChannel> getChannels() {
			return channels;
		}

		public void setChannels(ArrayList<TestChannel> channels) {
			this.channels = channels;
		}
	}
	
	// cf. http://api.bing.net/xml.aspx?Sources=image&AppId=828DA72AA6D172560F256E7B3784FA2295CB7D99&Version=2.2&Market=en-US&Query=texas&Web.Count=20
	// cf2. bingImageResult.xml bingWebResult.xml
	public static class BingSearch extends ElementState{

		// cf. BingImageSearchResult.java (in EcologylabGeneratedSemantics)
		//TODO make the root element <SearchResponse> available
		static final TranslationScope TS = TranslationScope.get("BingSearchTS", 
				SearchResponseState.class, Query.class/*, Image.class, Results.class, 
				ImageResult.class, Thumbnail.class */);
		
		public static void testBingSearch() throws IOException, SIMPLTranslationException{
			StringBuffer sb = readFile("ecologylab//standalone//researchnotebook//bingImageResult.xml");
			System.out.println(sb.toString());

			SearchResponseState r = (SearchResponseState)TS.deserializeCharSequence(sb.toString());
			
			r.serialize(System.out);
		}
	}
	
	public static StringBuffer readFile(String fileDir) throws IOException{
		File f = new File(fileDir);
		if(f.exists()){
			StringBuffer sb = new StringBuffer(); 
			String s = new String();
			
			FileReader r = new FileReader(f); 
			BufferedReader br = new BufferedReader(r); 
			while((s = br.readLine()) != null){
				sb.append(s+"\n");
			}
			return sb;  
		}else
			System.out.println("file does not exist");
		
		return null; 
	}
	
	public static void main(String args[]) throws SIMPLTranslationException, IOException{		
//		TestXml2State.testTestXml2(); 
//		TestRssState.testRss();
		BingSearch.testBingSearch(); 
	}
}
