package ecologylab.standalone.researchnotebook.testxml;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.Hint;

public class Thumbnail extends ElementState{
	@xml_tag("mms:Url") @simpl_scalar @simpl_hints(Hint.XML_LEAF) ParsedURL url;
	@xml_tag("mms:ContentType") @simpl_scalar @simpl_hints(Hint.XML_LEAF) String type;
	@xml_tag("mms:Width") @simpl_scalar @simpl_hints(Hint.XML_LEAF) int width;
	@xml_tag("mms:Height") @simpl_scalar @simpl_hints(Hint.XML_LEAF) int height; 
	@xml_tag("mms:FileSize") @simpl_scalar @simpl_hints(Hint.XML_LEAF) int size;
	
}
