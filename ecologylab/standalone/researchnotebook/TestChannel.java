package ecologylab.standalone.researchnotebook;

import java.util.ArrayList;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.Hint;

public class TestChannel extends ElementState{
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) String title;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) ParsedURL link;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) String description; 
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) String language; 
	
	@simpl_nowrap
	@simpl_collection("test_item") ArrayList<TestItem> items = new ArrayList<TestItem>(); 
}
