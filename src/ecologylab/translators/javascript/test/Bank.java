package ecologylab.translators.javascript.test;

import java.util.HashMap;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.ElementState.simpl_map;
import ecologylab.serialization.ElementState.simpl_nowrap;

public class Bank extends ElementState{
	@simpl_nowrap
	@simpl_map("items")
	@simpl_map_key_field("ownerName")
	HashMap<String, Item> itemMap;
	//
	//@simpl_map("item")
	//@simpl_map_key_field("id")
	//Map<Integer, Item> items;
	//
	//
	//
	
	public Bank(HashMap<String, Item> itemMap) {
		super();
		this.itemMap = itemMap;
	}
}
