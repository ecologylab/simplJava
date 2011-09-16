package ecologylab.standalone.researchnotebook.compositionTS;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.Hint;
import ecologylab.serialization.annotations.simpl_hints;
import ecologylab.serialization.annotations.simpl_scalar;

public class CfTextToken extends ElementState{
	@simpl_scalar String delims_before; 
	@simpl_scalar int style_plus_one; 
	@simpl_scalar int eol; 
	
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) String string; 
}
