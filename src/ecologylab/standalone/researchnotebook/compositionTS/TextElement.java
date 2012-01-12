package ecologylab.standalone.researchnotebook.compositionTS;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_scalar;

public class TextElement extends ElementState{
	@simpl_scalar float bias; 
	@simpl_scalar String stroke_color; 
	@simpl_scalar String font_color; 
	
	@simpl_composite Text text; 
	@simpl_composite TextChunk text_chunk; 	
}
