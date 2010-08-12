package ecologylab.standalone.researchnotebook.compositionTS;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.Hint;

public class WikipediaPage extends ElementState{
	@simpl_scalar ParsedURL location; 
	@simpl_scalar String title; 
	@simpl_scalar String page_structure; 
	@simpl_scalar ParsedURL main_image_src; 
	
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) String query; 
	@simpl_composite Paragraphs paragraphs; 
	@simpl_composite Categories categories; 
	@simpl_composite Thumbinners thumbinners; 
}
