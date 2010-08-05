package ecologylab.standalone.researchnotebook;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.Hint;

public class CfTextToken extends ElementState{
	@simpl_scalar String delims_before; 
	@simpl_scalar int style_plus_one; 
	@simpl_scalar int eol; 
	
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) String string; 
}
