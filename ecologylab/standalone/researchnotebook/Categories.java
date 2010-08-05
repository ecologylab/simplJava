package ecologylab.standalone.researchnotebook;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;

public class Categories extends ElementState{
	@simpl_nowrap @simpl_collection("category") ArrayList<Category> category = new ArrayList<Category>(); 
}
