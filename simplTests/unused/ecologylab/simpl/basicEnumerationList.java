package ecologylab.simpl;

import java.util.ArrayList;
import java.util.List;

import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.primaryScenarioEnum;



public class basicEnumerationList {
	
	@simpl_collection("collect")
	public List<primaryScenarioEnum> ourList;
	
	public basicEnumerationList(){ ourList = new ArrayList<primaryScenarioEnum>(); }
}
