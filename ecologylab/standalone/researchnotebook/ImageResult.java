package ecologylab.standalone.researchnotebook;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.Hint;

public class ImageResult extends ElementState{
	@xml_tag("mms:Title") @simpl_scalar @simpl_hints(Hint.XML_LEAF) String title; 
	@xml_tag("mms:MediaUrl") @simpl_scalar @simpl_hints(Hint.XML_LEAF) ParsedURL mediaUrl;
	@xml_tag("mms:Url") @simpl_scalar @simpl_hints(Hint.XML_LEAF) ParsedURL url;
	@xml_tag("mms:DisplayUrl") @simpl_scalar @simpl_hints(Hint.XML_LEAF) ParsedURL displayUrl;
	@xml_tag("mms:Width") @simpl_scalar @simpl_hints(Hint.XML_LEAF)	int width; 
	@xml_tag("mms:Height") @simpl_scalar @simpl_hints(Hint.XML_LEAF)	int height; 
	@xml_tag("mms:FileSize") @simpl_scalar @simpl_hints(Hint.XML_LEAF)	int filesize;
	@xml_tag("mms:ContentType")	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	String type; 
	
	@xml_tag("mms:Thumbnail") @simpl_composite Thumbnail thumbnail;
}
