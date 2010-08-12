package ecologylab.standalone.researchnotebook.compositionTS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;
import ecologylab.standalone.researchnotebook.Image;
import ecologylab.standalone.researchnotebook.SiteSet;

public class MAIN {
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
	
	public static class Composition extends ElementState{ 
		static final TranslationScope TS = TranslationScope.get("CompositionTS", 
				CompositionState.class, Preferences.class, SeedSet.class, SiteSet.class, 
				TraversableSet.class, UntraversableSet.class, RejectDomainSet.class,
				ContainerSet.class, Set.class, Container.class, Document.class, 
				WikipediaPage.class, Paragraphs.class, Paragraph.class, Anchors.class,
				Categories.class, Category.class, Thumbinners.class, Thumbinner.class,
				SurrogateSet.class, Surrogate.class, ImageElement.class, Image.class,
				Visual.class, Extent.class, TextElement.class, Text.class, TextChunk.class,
				CfTextToken.class, NamedStyle.class, InterestModel.class, Values.class,
				InterestModelEntry.class);
		
		public static void testComposition() throws IOException, SIMPLTranslationException{
			StringBuffer sb = readFile("ecologylab//standalone//researchnotebook//collageData//composition1.xml");
			System.out.println(sb.toString());
			
			CompositionState r = (CompositionState)TS.deserializeCharSequence(sb.toString());
			r.serialize(System.out);
			System.out.println();
			System.out.println(r.save_agent_state);
			System.out.println(r.cool_space_size);
			System.out.println(r.preferences.screen.size);
			System.out.println(r.seed_set.search.get(0).query); 
			ArrayList<Search> s = r.seed_set.getSearch();
			for(Search sr: s){
				System.out.println(sr.query);
			}
			ArrayList<Site> st = r.site_set.getSite();
			for(Site si: st){
				System.out.println(si.domain);
			}
			r.setState("false"); 
			r.setSize(30);
			
			SimpleDateFormat thisDateFormat = new SimpleDateFormat("MM-dd-yy_'at'_HH_mm_ss");
			String out = thisDateFormat.format(Calendar.getInstance().getTime());
			r.setDate(out); 
			r.serialize(System.out); 
		}	
	}
	public static void main(String[] args) throws IOException, SIMPLTranslationException {
		Composition c = new Composition(); 
		c.testComposition(); 
	}
}
