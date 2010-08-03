package ecologylab.standalone.researchnotebook;

import java.util.Date;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.Hint;

// TestChannel.java 
public class TestItem extends ElementState{
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) String title; 
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) ParsedURL link;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) String description; 
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) String guid; 
	@xml_tag("pubDate") @simpl_scalar @simpl_hints(Hint.XML_LEAF) String pubDate; 
}
