package ecologylab.standalone.researchnotebook.compositionTS;

import ecologylab.serialization.ElementState;

public class Site extends ElementState{
	//TODO parsedURL is not working below ('cause of omission of 'http'?)
	@simpl_scalar String domain; 
	@simpl_scalar float stroke_hue; 
	@simpl_scalar int font_index; 
}
