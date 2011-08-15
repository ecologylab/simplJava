package ecologylab.standalone.researchnotebook.compositionTS;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_nowrap;

public class Paragraphs extends ElementState{
	@simpl_nowrap @simpl_collection("paragraph") ArrayList<Paragraph>	paragraph = new ArrayList<Paragraph>(); 

}
