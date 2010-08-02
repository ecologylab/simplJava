package ecologylab.standalone;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;

public class TestItems extends ElementState
{
	@simpl_nowrap
	@simpl_collection("country") ArrayList<String> countries = new ArrayList<String>(); 
}
