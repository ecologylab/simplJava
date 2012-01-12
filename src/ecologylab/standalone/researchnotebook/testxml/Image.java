package ecologylab.standalone.researchnotebook.testxml;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.Hint;
import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_hints;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

public class Image extends ElementState{
	@simpl_tag("xmlns:mms") @simpl_scalar @simpl_hints(Hint.XML_ATTRIBUTE) ParsedURL xmlns;
	@simpl_tag("mms:Total") @simpl_scalar @simpl_hints(Hint.XML_LEAF) int total;
	@simpl_tag("mms:Offset") @simpl_scalar @simpl_hints(Hint.XML_LEAF) int offset;
	
	@simpl_tag("mms:Results") @simpl_composite Results results; 
	
}
