package ecologylab.standalone.researchnotebook.testxml;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_nowrap;
import ecologylab.serialization.annotations.simpl_tag;

public class Results extends ElementState{
	@simpl_nowrap @simpl_tag("mms:ImageResult") @simpl_collection("mms:ImageResult") ArrayList<ImageResult> result = new ArrayList<ImageResult>(); 

}
