package ecologylab.standalone.researchnotebook.compositionTS;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_nowrap;

public class SeedSet extends ElementState{
	@simpl_nowrap @simpl_collection("search") ArrayList<Search> search = new ArrayList<Search>();
	
	public ArrayList<Search> getSearch(){
		return search; 
	}
}
