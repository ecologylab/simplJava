package ecologylab.standalone.researchnotebook.compositionTS;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_scalar;

public class Paragraph extends ElementState{
	@simpl_scalar String paragraph_text; 
	@simpl_composite Anchors anchors; 	
}
