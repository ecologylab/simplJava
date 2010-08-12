package ecologylab.standalone.researchnotebook.compositionTS;

import ecologylab.serialization.ElementState;

public class Paragraph extends ElementState{
	@simpl_scalar String paragraph_text; 
	@simpl_composite Anchors anchors; 	
}
