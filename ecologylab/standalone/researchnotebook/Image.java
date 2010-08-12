package ecologylab.standalone.researchnotebook;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.Hint;

public class Image extends ElementState{
	@xml_tag("xmlns:mms") @simpl_scalar @simpl_hints(Hint.XML_ATTRIBUTE) ParsedURL xmlns;
	@xml_tag("mms:Total") @simpl_scalar @simpl_hints(Hint.XML_LEAF) int total;
	@xml_tag("mms:Offset") @simpl_scalar @simpl_hints(Hint.XML_LEAF) int offset;
	
	@xml_tag("mms:Results") @simpl_composite Results results; 
	
}
