package ecologylab.fundamental;

import java.util.ArrayList;
import java.util.List;

import simpl.annotations.dbal.simpl_collection;

import ecologylab.serialization.primaryScenarioEnum;



public class basicEnumerationList {
	
	@simpl_collection("collect")
	public List<primaryScenarioEnum> ourList;
	
	public basicEnumerationList(){ ourList = new ArrayList<primaryScenarioEnum>(); }
}
