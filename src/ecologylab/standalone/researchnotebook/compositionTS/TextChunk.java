package ecologylab.standalone.researchnotebook.compositionTS;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_nowrap;

public class TextChunk extends ElementState{
	@simpl_nowrap @simpl_collection("cf_text_token") ArrayList<CfTextToken> cf_text_token = new ArrayList<CfTextToken>();
	@simpl_composite NamedStyle named_style; 
}
