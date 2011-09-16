package ecologylab.standalone.researchnotebook.testxml;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.Hint;
import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_hints;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

public class ImageResult extends ElementState{
	@simpl_tag("mms:Title") @simpl_scalar @simpl_hints(Hint.XML_LEAF) String title; 
	@simpl_tag("mms:MediaUrl") @simpl_scalar @simpl_hints(Hint.XML_LEAF) ParsedURL mediaUrl;
	@simpl_tag("mms:Url") @simpl_scalar @simpl_hints(Hint.XML_LEAF) ParsedURL url;
	@simpl_tag("mms:DisplayUrl") @simpl_scalar @simpl_hints(Hint.XML_LEAF) ParsedURL displayUrl;
	@simpl_tag("mms:Width") @simpl_scalar @simpl_hints(Hint.XML_LEAF)	int width; 
	@simpl_tag("mms:Height") @simpl_scalar @simpl_hints(Hint.XML_LEAF)	int height; 
	@simpl_tag("mms:FileSize") @simpl_scalar @simpl_hints(Hint.XML_LEAF)	int filesize;
	@simpl_tag("mms:ContentType")	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	String type; 
	
	@simpl_tag("mms:Thumbnail") @simpl_composite Thumbnail thumbnail;
}
