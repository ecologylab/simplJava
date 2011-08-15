package ecologylab.standalone.researchnotebook.testxml;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.Hint;
import ecologylab.serialization.annotations.simpl_hints;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

public class Thumbnail extends ElementState{
	@simpl_tag("mms:Url") @simpl_scalar @simpl_hints(Hint.XML_LEAF) ParsedURL url;
	@simpl_tag("mms:ContentType") @simpl_scalar @simpl_hints(Hint.XML_LEAF) String type;
	@simpl_tag("mms:Width") @simpl_scalar @simpl_hints(Hint.XML_LEAF) int width;
	@simpl_tag("mms:Height") @simpl_scalar @simpl_hints(Hint.XML_LEAF) int height; 
	@simpl_tag("mms:FileSize") @simpl_scalar @simpl_hints(Hint.XML_LEAF) int size;
	
}
