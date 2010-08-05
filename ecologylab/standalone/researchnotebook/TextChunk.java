package ecologylab.standalone.researchnotebook;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.Hint;

public class TextChunk extends ElementState{
	@simpl_nowrap @simpl_collection("cf_text_token") ArrayList<CfTextToken> cf_text_token = new ArrayList<CfTextToken>();
	@simpl_composite NamedStyle named_style; 
}
