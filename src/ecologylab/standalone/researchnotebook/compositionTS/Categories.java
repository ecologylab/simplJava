package ecologylab.standalone.researchnotebook.compositionTS;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_nowrap;

public class Categories extends ElementState{
	@simpl_nowrap @simpl_collection("category") ArrayList<Category> category = new ArrayList<Category>(); 
}
