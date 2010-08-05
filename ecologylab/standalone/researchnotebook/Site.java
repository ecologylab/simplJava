package ecologylab.standalone.researchnotebook;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;

public class Site extends ElementState{
	//TODO parsedURL is not working below ('cause of omission of 'http'?)
	@simpl_scalar String domain; 
	@simpl_scalar float stroke_hue; 
	@simpl_scalar int font_index; 
}
