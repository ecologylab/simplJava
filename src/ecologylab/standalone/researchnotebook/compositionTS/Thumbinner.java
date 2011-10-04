package ecologylab.standalone.researchnotebook.compositionTS;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_scalar;

public class Thumbinner extends ElementState{
	@simpl_scalar String thumb_img_caption; 
	@simpl_scalar ParsedURL thumb_img_src; 
}
