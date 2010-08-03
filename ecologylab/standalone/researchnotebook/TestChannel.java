package ecologylab.standalone.researchnotebook;

import java.util.ArrayList;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.Hint;

public class TestChannel extends ElementState{
	@simpl_scalar String title;
//	@simpl_scalar ParsedURL link;
//	@simpl_scalar String description; 
//	@simpl_scalar String language; 
	
	@simpl_nowrap
	@simpl_collection("test_item") ArrayList<TestItem> items = new ArrayList<TestItem>(); 
}
