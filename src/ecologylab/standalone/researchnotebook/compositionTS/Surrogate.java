package ecologylab.standalone.researchnotebook.compositionTS;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.Hint;
import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_hints;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

public class Surrogate extends ElementState{
	@simpl_tag("containerURL") @simpl_scalar ParsedURL containerURL; 
	@simpl_scalar int history_num; 
	
	@simpl_composite @simpl_hints(Hint.XML_LEAF) ImageElement image_element; 
	@simpl_composite @simpl_hints(Hint.XML_LEAF) TextElement text_element; 
	@simpl_composite @simpl_hints(Hint.XML_LEAF) Visual visual; 
}
