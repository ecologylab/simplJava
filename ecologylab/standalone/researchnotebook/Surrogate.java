package ecologylab.standalone.researchnotebook;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.Hint;

public class Surrogate extends ElementState{
	@xml_tag("containerURL") @simpl_scalar ParsedURL containerURL; 
	@simpl_scalar int history_num; 
	
	@simpl_composite @simpl_hints(Hint.XML_LEAF) ImageElement image_element; 
	@simpl_composite @simpl_hints(Hint.XML_LEAF) TextElement text_element; 
	@simpl_composite @simpl_hints(Hint.XML_LEAF) Visual visual; 
}
