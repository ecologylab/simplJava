package ecologylab.standalone.researchnotebook.testxml;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_nowrap;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

public class TestItems extends ElementState
{
	
	@simpl_nowrap @simpl_tag("test_sub_item")
	@simpl_collection("test_sub_item") ArrayList<String>	country_name = new ArrayList<String>();
	
	@simpl_nowrap @simpl_tag("test_sub_item")
	@simpl_collection("test_sub_item") ArrayList<TestSubItem> countries = new ArrayList<TestSubItem>();
	
	@simpl_nowrap
	@simpl_collection("test_sub_item2") ArrayList<TestSubItem2> sub = new ArrayList<TestSubItem2>(); 
	
	@simpl_scalar String name; 
	
	@simpl_scalar String priority; 
}
