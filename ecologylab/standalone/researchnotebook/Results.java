package ecologylab.standalone.researchnotebook;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;

public class Results extends ElementState{
	@simpl_nowrap @xml_tag("mms:ImageResult") @simpl_collection("mms:ImageResult") ArrayList<ImageResult> result = new ArrayList<ImageResult>(); 

}
