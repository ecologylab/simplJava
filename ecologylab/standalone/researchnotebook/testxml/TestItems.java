package ecologylab.standalone.researchnotebook.testxml;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;

public class TestItems extends ElementState
{
	
	@simpl_nowrap @xml_tag("test_sub_item")
	@simpl_collection("test_sub_item") ArrayList<String>	country_name = new ArrayList<String>();
	
	@simpl_nowrap @xml_tag("test_sub_item")
	@simpl_collection("test_sub_item") ArrayList<TestSubItem> countries = new ArrayList<TestSubItem>();
	
	@simpl_nowrap
	@simpl_collection("test_sub_item2") ArrayList<TestSubItem2> sub = new ArrayList<TestSubItem2>(); 
	
	@simpl_scalar String name; 
	
	@simpl_scalar String priority; 
}
